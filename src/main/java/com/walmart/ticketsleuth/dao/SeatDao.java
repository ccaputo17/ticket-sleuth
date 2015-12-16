package com.walmart.ticketsleuth.dao;

import java.util.List;

import com.walmart.ticketsleuth.model.Seat;

public interface SeatDao
{
  List<Seat> loadAll();

  List<Seat> loadByLevel( long levelId );

  List<Seat> loadByMinAndMaxLevel( Long minLevel, Long maxLevel );

  Seat loadById( Long id );

  void save( Seat seat );

  void delete( Seat seat );

  void deleteById( Long id );
}
