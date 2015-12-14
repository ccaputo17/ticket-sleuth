package com.walmart.ticketsleuth.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.walmart.ticketsleuth.model.impl.LevelImpl;

@JsonDeserialize( as = LevelImpl.class)
public interface Level
{
  Long getId();
  void setId( Long id );

  String getName();
  void setName( String name );

  Double getPrice();
  void setPrice( Double price );

  Integer getRows();
  void setRows( Integer rows );

  Integer getSeatsPerRow();
  void setSeatsPerRow( Integer seatsPerRow );
}
