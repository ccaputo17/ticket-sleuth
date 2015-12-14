package com.walmart.ticketsleuth.dao.impl;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseDaoImpl<I>
{
  @Autowired
  private SessionFactory sessionFactory;
  private Class<I> interfaceClass;
  private Class<? extends I> concreteClass;

  public BaseDaoImpl( Class<I> interfaceClass, Class<? extends I> concreteClass )
  {
    this.interfaceClass = interfaceClass;
    this.concreteClass = concreteClass;
  }
  
  @SuppressWarnings( "unchecked" )
  @Transactional( readOnly = true )
  public final I loadById( final Long id )
  {
    if ( id == null )
    {
      throw new PersistenceException( String.format( "Invalid id. Cannot load '%s' with null id.", interfaceClass.getSimpleName() ) );
    }

    return (I) sessionFactory.getCurrentSession().get( concreteClass, id );
  }

  @Transactional( readOnly = true )
  public final I findOneByCriteria( Criterion... criterion )
  {
    List<I> results = findByCriteria( criterion );

    if ( results != null && results.size() > 1 )
    {
      throw new PersistenceException( String.format( "Invalid result. Expected one matching '%s', but found '%d'.", interfaceClass.getSimpleName(), results.size() ) );
    }

    return ( results != null && !results.isEmpty() ) ? results.get( 0 ) : null;
  }

  @SuppressWarnings( "unchecked" )
  @Transactional( readOnly = true )
  public final List<I> loadAll()
  {
    return sessionFactory.getCurrentSession().createQuery( "from " + interfaceClass.getSimpleName() ).list();
  }

  @SuppressWarnings( "unchecked" )
  @Transactional( readOnly = true )
  public final List<I> findByCriteria( Criterion... criterion )
  {
    Criteria criteria = getCurrentSession().createCriteria( interfaceClass );

    for ( Criterion crit : criterion )
    {
      if ( crit == null )
      {
        throw new PersistenceException( String.format( "Invalid criterion. Cannot find '%s' using  null criterion.", interfaceClass.getSimpleName() ) );
      }

      criteria.add( crit );
    }
    return criteria.list();
  }

  @Transactional( propagation = REQUIRED )
  public final void save( final I entity )
  {
    if ( entity == null )
    {
      throw new PersistenceException( String.format( "Invalid entity. Cannot save null '%s'.", interfaceClass.getSimpleName() ) );
    }

    beforeSave( entity );
    
    sessionFactory.getCurrentSession().saveOrUpdate( entity );
  }

  @Transactional( propagation = REQUIRED )
  public final void deleteById( final Long id )
  {
    if ( id == null )
    {
      throw new PersistenceException( String.format( "Invalid id. Cannot delete '%s' for null id.", interfaceClass.getSimpleName() ) );
    }

    I entity = loadById( id );
    delete( entity );
  }

  @Transactional( propagation = REQUIRED )
  public final void delete( final I entity )
  {
    if ( entity == null )
    {
      throw new PersistenceException( String.format( "Invalid entity. Cannot delete null '%s'.", interfaceClass.getSimpleName() ) );
    }

    getCurrentSession().delete( entity );
  }

  @Transactional( propagation = REQUIRED )
  public final void deleteAll( final List<I> entities )
  {
    if ( entities == null )
    {
      throw new PersistenceException( String.format( "Invalid argument. Cannot delete null '%s' list.", interfaceClass.getSimpleName() ) );
    }

    for ( I entity : entities )
    {
      getCurrentSession().delete( entity );
    }
  }

  protected final Session getCurrentSession()
  {
    return sessionFactory.getCurrentSession();
  }

  protected final Criteria createCriteria()
  {
    return sessionFactory.getCurrentSession().createCriteria( interfaceClass );
  }
  
  protected void beforeSave( final I entity )
  {
  }
}