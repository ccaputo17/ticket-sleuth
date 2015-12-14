package com.walmart.ticketsleuth.service.impl;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.dao.SeatDao;
import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.DataModelFactory;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.model.Seat;
import com.walmart.ticketsleuth.model.SeatHold;
import com.walmart.ticketsleuth.service.TicketService;
import com.walmart.ticketsleuth.service.VenueService;

@Service
@Transactional
public final class TicketServiceImpl implements TicketService
{
  @Autowired
  VenueService venueService;

  @Autowired
  LevelDao levelDao;

  @Autowired
  SeatDao seatDao;

  @Autowired
  SeatHoldDao seatHoldDao;

  @Autowired
  DataModelFactory dataModelFactory;
  
  @Value( "${hold-tickets.period : 300000}" )
  int holdPeriod;
  
  public TicketServiceImpl()
  {
  }

  @Override
  @Transactional( readOnly = true )
  public int numSeatsAvailable( Long minLevel, Long maxLevel )
  {
    int seatingCapacity = venueService.getSeatingCapacity( minLevel, maxLevel );
    int unavailableSeats = seatDao.loadByMinAndMaxLevel( minLevel, maxLevel ).size(); // CSC - FIX 
    
    return seatingCapacity - unavailableSeats;
  }

  @Override
  @Transactional( propagation = REQUIRED )
  public SeatHold findAndHoldSeats( int numSeats, Long minLevel, Long maxLevel, String customerEmail )
  {
    Set<Seat> seats = new LinkedHashSet<Seat>( numSeats );
    List<Level> levels;
    SeatHold seatHold;

    if ( minLevel <= maxLevel )
    {
      levels = levelDao.loadByMinAndMaxLevel( minLevel, maxLevel );
    }
    else
    {
      levels = levelDao.loadByMinAndMaxLevel( maxLevel, minLevel );
    }
    
    for ( Level level : levels )
    {
      // load one level at a time
      List<Seat> unavailableSeats = seatDao.loadByLevel( level.getId() );
      Map<Integer, Map<Integer,Seat>> unavailableSeatMap = createUnavailableSeatMap( unavailableSeats );
      
      for ( int rowNumber=1; rowNumber<=level.getRows(); rowNumber++ )
      {
        for ( int seatNumber=1; seatNumber<=level.getSeatsPerRow(); seatNumber++ )
        {
          if ( isAvailable( unavailableSeatMap, rowNumber, seatNumber ) )
          {
            seats.add( dataModelFactory.newSeat( level, rowNumber, seatNumber ) );
            
            if ( seats.size() == numSeats )
            {
              // CSC - FIX - Not a big fan of setting the transaction on the service, but for now it works
              // Save all seat records before saving the hold record
              for ( Seat seat : seats )
              {
                seatDao.save( seat );
              }
              
              seatHold = dataModelFactory.newSeatHold( seats, new DateTime().plusMillis( holdPeriod ), customerEmail );
              seatHoldDao.save( seatHold );
              
              // CSC - FIX - By now some of the seats might no longer be available, but fixing that probably is not going to be part of my MVP
              return seatHold;
            }
          }
        }
      }
    }
    
    // Not enough tickets available to service the order, returning a SeatHold object with Seat records 
    return dataModelFactory.newSeatHold( null, null, customerEmail );
  }

  @Override
  @Transactional( propagation = REQUIRED )
  public String reserveSeats( long seatHoldId, String customerEmail )
  {
    SeatHold seatHold = seatHoldDao.loadById( seatHoldId );
    
    if ( seatHold == null )
    {
      // SeatHold object has already been purged by background process
      return null;
    }
    
    String confirmationNumber = UUID.randomUUID().toString();
    
    // store the purchase price and confirmation number in the seat records before removing the seat hold record
    for ( Seat seat : seatHold.getSeats() )
    {
      seat.setPurchasePrice( seat.getLevel().getPrice() );
      seat.setConfirmationNumber( confirmationNumber );
      seatDao.save( seat );
    }
    
    seatHoldDao.delete( seatHold );
    
    return confirmationNumber;
  }

  @Override
  @Transactional( propagation = REQUIRED )
  public void releaseSeats( long seatHoldId )
  {
    SeatHold seatHold = seatHoldDao.loadById( seatHoldId );
    
    if ( seatHold == null )
    {
      // SeatHold object has already been purged by background process
      return;
    }
    
    // remove each seat record before removing the seat hold record
    for ( Seat seat : seatHold.getSeats() )
    {
      seatDao.delete( seat );
    }
    
    seatHoldDao.delete( seatHold );
  }
  
  private Map<Integer, Map<Integer,Seat>> createUnavailableSeatMap( List<Seat> unavailableSeats )
  {
    Map<Integer, Map<Integer,Seat>> unavailableSeatMap = new TreeMap<Integer, Map<Integer,Seat>>();
    
    for ( Seat seat : unavailableSeats )
    {
      Map<Integer, Seat> rowMap = unavailableSeatMap.get( seat.getRow() );
      
      if ( rowMap == null )
      {
        rowMap = new TreeMap<Integer, Seat>();
        unavailableSeatMap.put( seat.getRow(), rowMap );
      }
      
      rowMap.put( seat.getSeatNumber(), seat );
    }
    
    return unavailableSeatMap;
  }
  
  private boolean isAvailable( Map<Integer, Map<Integer,Seat>> unavailableSeatMap, int rowNumber, int seatNumber )
  {
    // CSC - FIX - Not great, but for moment...
    Map<Integer, Seat> rowMap = unavailableSeatMap.get( rowNumber );
    
    if ( rowMap == null )
    {
      // no unavailable seats in the requested row
      return true;
    }
    else
    {
      // if no seat object is returned the seat is available
      return rowMap.get( seatNumber ) == null;
    }
  }
}
