package com.walmart.ticketsleuth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.service.VenueService;

@Service
public class VenueServiceImpl implements VenueService
{
  @Autowired
  LevelDao levelDao;

  @Override
  public List<Level> getSeatingLevels()
  {
    return levelDao.loadAll();
  }

  @Override
  public int getSeatingCapacity( Long minLevel, Long maxLevel )
  {
    int seatingCapacity = 0;
    
    List<Level> levels = levelDao.loadByMinAndMaxLevel( minLevel, maxLevel );
    
    for ( Level level : levels )
    {
      seatingCapacity += calculateSeatingCapacityForLevel( level );
    }

    return seatingCapacity;
  }

  private int calculateSeatingCapacityForLevel( Level level )
  {
    return level.getRows() * level.getSeatsPerRow();
  }

}
