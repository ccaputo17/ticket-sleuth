package com.walmart.ticketsleuth.model;

import java.util.Set;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.walmart.ticketsleuth.model.impl.SeatHoldImpl;

@JsonDeserialize( as = SeatHoldImpl.class)
public interface SeatHold
{
  Long getId();
  void setId( Long id );

  Set<Seat> getSeats();
  void setSeats( Set<Seat> seats );

  DateTime getExpiration();
  void setExpiration( DateTime expiration );

  String getCustomerEmail();

  void setCustomerEmail( String customerEmail );
}
