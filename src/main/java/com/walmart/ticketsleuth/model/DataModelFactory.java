package com.walmart.ticketsleuth.model;

import java.util.Set;

import org.joda.time.DateTime;

public interface DataModelFactory
{
  Level newLevel( String name, Double price, Integer rows, Integer seatsPerRow );
  
  Seat newSeat( Level level, Integer row, Integer seatNumber );
  
  Seat newSeat( Level level, Integer row, Integer seatNumber, Double purchasePrice, String confirmationNumber );
  
  SeatHold newSeatHold( Set<Seat> seats, DateTime expiration, String customerEmail );
}
