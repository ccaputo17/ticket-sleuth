package com.walmart.ticketsleuth.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.walmart.ticketsleuth.dao.SeatDao;
import com.walmart.ticketsleuth.model.Seat;
import com.walmart.ticketsleuth.model.impl.SeatImpl;

@Component
@Transactional
public class SeatDaoImpl extends BaseDaoImpl<Seat> implements SeatDao
{
  public SeatDaoImpl()
  {
    super( Seat.class, SeatImpl.class );
  }
  
  @Override
  public List<Seat> loadByLevel( long levelId )
  {
    return findByCriteria( Restrictions.eq( "level.id", levelId ) );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Seat> loadByMinAndMaxLevel( Long minLevel, Long maxLevel )
  {
    Criteria criteria = createCriteria();

    if ( minLevel != null )
    {
      criteria.add( Restrictions.ge( "level.id", minLevel ) );
    }
    
    if ( maxLevel != null )
    {
      criteria.add( Restrictions.le( "level.id", maxLevel ) );
    }
    criteria.addOrder( Order.asc( "level.id" ) );

    return criteria.list();
  }
}
