package com.walmart.ticketsleuth.service;

import java.util.List;

import com.walmart.ticketsleuth.model.Level;

public interface VenueService
{
  List<Level> getSeatingLevels();
  int getSeatingCapacity( Long minLevel, Long maxLevel );
}
