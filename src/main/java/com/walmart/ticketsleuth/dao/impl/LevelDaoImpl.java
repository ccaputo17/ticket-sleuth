package com.walmart.ticketsleuth.dao.impl;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.model.impl.LevelImpl;

@Component
@Transactional
public class LevelDaoImpl extends BaseDaoImpl<Level> implements LevelDao
{
  public LevelDaoImpl()
  {
    super( Level.class, LevelImpl.class );
  }
  
  @Override
  public Level loadByName( String name )
  {
    if ( StringUtils.isBlank( name ) )
    {
      throw new PersistenceException( "Invalid name. Name parameter cannot be null or empty." );
    }

    return findOneByCriteria( Restrictions.eq( "name", name ) );
  }
  
  @SuppressWarnings( "unchecked" )
  @Override
  public List<Level> loadByMinAndMaxLevel( Long minLevel, Long maxLevel )
  {
    Criteria criteria = createCriteria();

    if ( minLevel != null )
    {
      criteria.add( Restrictions.ge( "id", minLevel ) );
    }
    
    if ( maxLevel != null )
    {
      criteria.add( Restrictions.le( "id", maxLevel ) );
    }
    criteria.addOrder( Order.asc( "id" ) );

    return criteria.list();
  }
}
