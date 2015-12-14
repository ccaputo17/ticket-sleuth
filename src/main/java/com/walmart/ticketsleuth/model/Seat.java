package com.walmart.ticketsleuth.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.walmart.ticketsleuth.model.impl.SeatImpl;

@JsonDeserialize( as = SeatImpl.class)
public interface Seat
{
  Long getId();
  void setId( Long id );

  Level getLevel();
  void setLevel( Level level );

  Integer getRow();
  void setRow( Integer row );

  Integer getSeatNumber();
  void setSeatNumber( Integer seatNumber );

  Double getPurchasePrice();
  void setPurchasePrice( Double purchasePrice );

  String getConfirmationNumber();
  void setConfirmationNumber( String confirmationNumber );
}
