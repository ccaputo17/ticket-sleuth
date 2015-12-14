package com.walmart.ticketsleuth.dao;

import java.util.List;

import com.walmart.ticketsleuth.model.Level;

public interface LevelDao
{
  List<Level> loadAll();

  List<Level> loadByMinAndMaxLevel( Long minLevel, Long maxLevel );

  Level loadById( Long id );

  Level loadByName( String name );

  void save( Level level );

  void delete( Level level );
}
