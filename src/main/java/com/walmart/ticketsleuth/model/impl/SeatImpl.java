package com.walmart.ticketsleuth.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.core.style.ToStringCreator;

import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.model.Seat;

@Entity( name = "Seat" )
@Table( name = "SEAT", uniqueConstraints = { @UniqueConstraint( columnNames = { "level_id", "row", "seat_number" } ) } )
@SequenceGenerator( name = "SEAT_SEQ", sequenceName = "SEAT_SEQ", allocationSize = 1 )
public class SeatImpl implements Seat
{
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEAT_SEQ" )
  private Long id;

  @OneToOne( targetEntity = LevelImpl.class )
  @JoinColumn( name = "level_id", nullable = false )
  private Level level;

  @Column( name = "row", nullable = false )
  private Integer row;

  @Column( name = "seat_number", nullable = false )
  private Integer seatNumber;

  @Column( name = "purchase_price" )
  private Double purchasePrice;
  
  @Column( name = "confirmation_number" )
  private String confirmationNumber;
  
  public SeatImpl()
  {
  }

  public SeatImpl( Level level, Integer row, Integer seatNumber )
  {
    this( level, row, seatNumber, null, null );
  }
  
  public SeatImpl( Level level, Integer row, Integer seatNumber, Double purchasePrice, String confirmationNumber )
  {
    this.level = level;
    this.row = row;
    this.seatNumber = seatNumber;
    this.purchasePrice = purchasePrice;
    this.confirmationNumber = confirmationNumber;
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

  public final Level getLevel()
  {
    return level;
  }

  public final void setLevel( Level level )
  {
    this.level = level;
  }

  @Override
  public final Integer getRow()
  {
    return row;
  }

  @Override
  public final void setRow( Integer row )
  {
    this.row = row;
  }

  public final Integer getSeatNumber()
  {
    return seatNumber;
  }

  public final void setSeatNumber( Integer seatNumber )
  {
    this.seatNumber = seatNumber;
  }

  @Override
  public final Double getPurchasePrice()
  {
    return purchasePrice;
  }

  @Override
  public final void setPurchasePrice( Double purchasePrice )
  {
    this.purchasePrice = purchasePrice;
  }

  @Override
  public final String getConfirmationNumber()
  {
    return confirmationNumber;
  }

  @Override
  public final void setConfirmationNumber( String confirmationNumber )
  {
    this.confirmationNumber = confirmationNumber;
  }

  @Override
  public final String toString()
  {
    ToStringCreator strValue = new ToStringCreator(this);
    strValue.append( "id", id );
    strValue.append( "level", level );
    strValue.append( "row", row );
    strValue.append( "seatNumber", seatNumber );
    strValue.append( "purchasePrice", purchasePrice );
    strValue.append( "confirmationNumber", confirmationNumber );

    return strValue.toString();
  }
}
