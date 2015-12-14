package com.walmart.ticketsleuth.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.SeatHold;
import com.walmart.ticketsleuth.model.impl.SeatHoldImpl;

@Component
@Transactional
public class SeatHoldDaoImpl extends BaseDaoImpl<SeatHold> implements SeatHoldDao
{
  public SeatHoldDaoImpl()
  {
    super( SeatHold.class, SeatHoldImpl.class );
  }

  @Override
  public List<SeatHold> loadByExpirationDate( DateTime dateTime, boolean expired )
  {
    if ( dateTime == null )
    {
      throw new PersistenceException( "Invalid date time. Parameter cannot be null." );
    }

//    if ( expired )
//    {
//      return findByCriteria( Restrictions.lt( "expiration", dateTime ) );
//    }
//    else // not expired
//    {
//      return findByCriteria( Restrictions.ge( "expiration", dateTime ) );
//    }

    // CSC - FIX - This is returning an instance of the SeatHold object for each seat held. This is probably b/c of the eager fetching of seat records
    // Unfortunately, this isn't something I have time to debug right now so instead I am filtering out duplicates here...
    List<SeatHold> seatHolds;
    
    if ( expired )
    {
      seatHolds = findByCriteria( Restrictions.lt( "expiration", dateTime ) );
    }
    else // not expired
    {
      seatHolds = findByCriteria( Restrictions.ge( "expiration", dateTime ) );
    }
    
    if ( seatHolds.isEmpty() )
    {
      return seatHolds;
    }
    
    // use a map to sift out duplicates
    Map<Long, SeatHold> map = new HashMap<Long, SeatHold>();
    for ( SeatHold seatHold : seatHolds )
    {
      map.put( seatHold.getId(), seatHold );
    }
    
    return new ArrayList<SeatHold>( map.values() );
  }
}
