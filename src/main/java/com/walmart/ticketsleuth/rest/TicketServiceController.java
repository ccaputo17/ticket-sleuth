package com.walmart.ticketsleuth.rest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketsleuth.model.SeatHold;
import com.walmart.ticketsleuth.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketServiceController
{
  private static Logger logger = LoggerFactory.getLogger( TicketServiceController.class );

  @Autowired
  TicketService ticketService;
  
  @RequestMapping( value = "/{levelId}/available", method = GET )
  public Integer numSeatsAvailable( @PathVariable Long levelId )
  {
    logger.debug( "numSeatsAvailable: levelId={}", levelId );
    
    return ticketService.numSeatsAvailable( levelId, levelId );
  }

  @RequestMapping( value = "/hold", method = POST )
  public SeatHold findAndHoldSeats( @RequestParam Integer numSeats,
                                    @RequestParam Long minLevel,
                                    @RequestParam Long maxLevel,
                                    @RequestParam String customerEmail )
  {
    logger.debug( "findAndHoldSeats: numSeats={},minLevel={},maxLevel={},customerEmail={}", numSeats, minLevel, maxLevel, customerEmail );
    
    return ticketService.findAndHoldSeats( numSeats, minLevel, maxLevel, customerEmail );
  }

  @RequestMapping( value = "/reserve", method = POST )
  public ResponseEntity<String> reserveSeats( @RequestParam Long seatHoldId, @RequestParam String customerEmail )
  {
    logger.debug( "reserveSeats: seatHoldId={},customerEmail={}", seatHoldId, customerEmail );
    
    String confirmationNumber = ticketService.reserveSeats( seatHoldId, customerEmail );

    // CSC - Using a ResponseEntity b/c the latest version of Angular was trying to parse the string as JSON 
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
    return new ResponseEntity<String>(confirmationNumber, responseHeaders, HttpStatus.CREATED);
  }

  @RequestMapping( value = "/reserve/{seatHoldId}", method = DELETE )
  @ResponseStatus( value = OK )
  public void releaseSeats( @PathVariable Long seatHoldId )
  {
    logger.debug( "releaseSeats: seatHoldId={}", seatHoldId );
    
    ticketService.releaseSeats( seatHoldId );
  }
}
