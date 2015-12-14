package com.walmart.ticketsleuth.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.core.style.ToStringCreator;

import com.walmart.ticketsleuth.model.Level;

@Entity( name = "Level" )
@Table( name = "LEVEL", uniqueConstraints = { @UniqueConstraint( columnNames = "name" ) } )

@SequenceGenerator( name = "LEVEL_SEQ", sequenceName = "LEVEL_SEQ" )
public class LevelImpl implements Level
{
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "LEVEL_SEQ" )
  private Long id;

  @Column( nullable = false )
  private String name;

  @Column( nullable = false )
  private Double price;

  @Column( nullable = false )
  private Integer rows;

  @Column( name = "seats_per_row", nullable = false )
  private Integer seatsPerRow;
  
  public LevelImpl()
  {
  }

  public LevelImpl( String name, Double price, Integer rows, Integer seatsPerRow )
  {
    this.name = name;
    this.price = price;
    this.rows = rows;
    this.seatsPerRow = seatsPerRow;
  }

  @Override
  public final Long getId()
  {
    return id;
  }

  @Override
  public final void setId( Long id )
  {
    this.id = id;
  }

  @Override
  public final String getName()
  {
    return name;
  }

  @Override
  public final void setName( String name )
  {
    this.name = name;
  }

  @Override
  public final Double getPrice()
  {
    return price;
  }

  @Override
  public final void setPrice( Double price )
  {
    this.price = price;
  }

  @Override
  public final Integer getRows()
  {
    return rows;
  }

  @Override
  public final void setRows( Integer rows )
  {
    this.rows = rows;
  }

  @Override
  public final Integer getSeatsPerRow()
  {
    return seatsPerRow;
  }

  @Override
  public final void setSeatsPerRow( Integer seatsPerRow )
  {
    this.seatsPerRow = seatsPerRow;
  }

  @Override
  public final String toString()
  {
    ToStringCreator strValue = new ToStringCreator(this);
    strValue.append( "id", id );
    strValue.append( "name", name );
    strValue.append( "price", price );
    strValue.append( "rows", rows );
    strValue.append( "seatsPerRow", seatsPerRow );

    return strValue.toString();
  }
}
