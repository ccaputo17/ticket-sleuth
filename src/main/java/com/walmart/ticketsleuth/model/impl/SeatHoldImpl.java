package com.walmart.ticketsleuth.model.impl;

import static javax.persistence.FetchType.EAGER;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.core.style.ToStringCreator;

import com.walmart.ticketsleuth.model.Seat;
import com.walmart.ticketsleuth.model.SeatHold;

@Entity( name = "SeatHold" )
@Table( name = "SEAT_HOLD" )
@SequenceGenerator( name = "SEAT_HOLD_SEQ", sequenceName = "SEAT_HOLD_SEQ", allocationSize = 1 )
public class SeatHoldImpl implements SeatHold
{
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEAT_HOLD_SEQ" )
  private Long id;

  @OneToMany( fetch = EAGER, targetEntity = SeatImpl.class )
  @JoinColumn(name="seat_hold_id")
  private Set<Seat> seats;

  @Column( name = "expiration_date", nullable = false )
  @Type( type="org.jadira.usertype.dateandtime.joda.PersistentDateTime" )
  private DateTime expiration;

  @Column( name = "customer_email", nullable = false )
  private String customerEmail;
  
  public SeatHoldImpl()
  {
  }

  public SeatHoldImpl( Set<Seat> seats, DateTime expiration, String customerEmail )
  {
    this.seats = seats;
    this.expiration = expiration;
    this.customerEmail = customerEmail;
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
  public final Set<Seat> getSeats()
  {
    return seats;
  }

  @Override
  public final void setSeats( Set<Seat> seats )
  {
    this.seats = seats;
  }

  @Override
  public final DateTime getExpiration()
  {
    return expiration;
  }

  @Override
  public final void setExpiration( DateTime expiration )
  {
    this.expiration = expiration;
  }

  @Override
  public final String getCustomerEmail()
  {
    return customerEmail;
  }

  @Override
  public final void setCustomerEmail( String customerEmail )
  {
    this.customerEmail = customerEmail;
  }

  @Override
  public final String toString()
  {
    ToStringCreator strValue = new ToStringCreator(this);
    strValue.append( "id", id );
    strValue.append( "seats", seats );
    strValue.append( "expiration", expiration );
    strValue.append( "customerEmail", customerEmail );

    return strValue.toString();
  }
}
