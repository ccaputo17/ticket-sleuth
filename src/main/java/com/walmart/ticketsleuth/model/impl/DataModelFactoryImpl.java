package com.walmart.ticketsleuth.model.impl;

import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.walmart.ticketsleuth.model.DataModelFactory;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.model.Seat;
import com.walmart.ticketsleuth.model.SeatHold;

@Component
public class DataModelFactoryImpl implements DataModelFactory
{
  @Override
  public Level newLevel( String name, Double price, Integer rows, Integer seatsPerRow )
  {
    return new LevelImpl( name, price, rows, seatsPerRow );
  }

  @Override
  public Seat newSeat( Level level, Integer row, Integer seatNumber )
  {
    return new SeatImpl( level, row, seatNumber );
  }

  @Override
  public Seat newSeat( Level level, Integer row, Integer seatNumber, Double purchasePrice, String confirmationNumber )
  {
    return new SeatImpl( level, row, seatNumber, purchasePrice, confirmationNumber );
  }

  @Override
  public SeatHold newSeatHold( Set<Seat> seats, DateTime expiration, String customerEmail )
  {
    return new SeatHoldImpl( seats, expiration, customerEmail );
  }
}
