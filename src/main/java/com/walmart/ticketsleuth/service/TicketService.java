package com.walmart.ticketsleuth.service;

import com.walmart.ticketsleuth.model.SeatHold;

public interface TicketService
{
  /**
   * The number of seats in the requested level(s) that are neither held nor reserved
   *
   * @param minLevel the minimum venue level
   * @param maxLevel the maximum venue level
   * @return the number of tickets available on the provided level(s)
   */
  int numSeatsAvailable( Long minLevel, Long maxLevel );

  /**
   * Find and hold the best available seats for a customer
   *
   * @param numSeats the number of seats to find and hold
   * @param minLevel the minimum venue level
   * @param maxLevel the maximum venue level
   * @param customerEmail unique identifier for the customer
   * @return a SeatHold object identifying the specific seats and related information
   */
  SeatHold findAndHoldSeats( int numSeats, Long minLevel, Long maxLevel, String customerEmail );

  /**
   * Commit seats held for a specific customer
   *
   * @param seatHoldId the seat hold identifier
   * @param customerEmail the email address of the customer to which the seat hold is assigned
   * @return a reservation confirmation code
   */
  String reserveSeats( long seatHoldId, String customerEmail );
  
  /**
   * Release seats held for a specific customer
   *
   * @param seatHoldId the seat hold identifier
   */
  void releaseSeats( long seatHoldId );
 
}
