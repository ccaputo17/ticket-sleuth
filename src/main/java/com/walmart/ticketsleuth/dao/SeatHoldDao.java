package com.walmart.ticketsleuth.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.walmart.ticketsleuth.model.SeatHold;

public interface SeatHoldDao
{
  List<SeatHold> loadAll();

  List<SeatHold> loadByExpirationDate( DateTime dateTime, boolean expired );

  SeatHold loadById( Long id );

  void save( SeatHold seatHold );

  void delete( SeatHold seatHold );
}
