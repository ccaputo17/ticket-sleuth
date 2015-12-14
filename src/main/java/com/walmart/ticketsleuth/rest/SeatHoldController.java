package com.walmart.ticketsleuth.rest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.SeatHold;

@RestController
@CrossOrigin( origins = { "http://localhost:8888", "http://localhost:8080" } )
@RequestMapping("/holds")
public class SeatHoldController
{
  private static Logger logger = LoggerFactory.getLogger( SeatHoldController.class );

  @Autowired
  SeatHoldDao seatHoldDao;
  
  @RequestMapping( method = GET )
  public List<SeatHold> getSeatHolds()
  {
    return seatHoldDao.loadAll();
  }

  @RequestMapping( value = "/{id}", method = GET )
  public SeatHold getSeatHold( @PathVariable( "id" ) Long id )
  {
    logger.debug( "id={}", id );
    
    return seatHoldDao.loadById( id );
  }

  @RequestMapping( method = POST )
  @ResponseStatus( value = OK )
  public void createSeatHold( @RequestBody SeatHold seatHold )
  {
    logger.debug( "SeatHold={}", seatHold );
    seatHoldDao.save( seatHold );
  }

  @RequestMapping( method = PUT )
  @ResponseStatus( value = OK )
  public void updateSeatHold( @RequestBody SeatHold seatHold )
  {
    logger.debug( "SeatHold={}", seatHold );
    seatHoldDao.save( seatHold );
  }
}
