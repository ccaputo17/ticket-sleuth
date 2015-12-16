package com.walmart.ticketsleuth.rest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.SeatHold;

@RestController
@RequestMapping("/holds")
public class SeatHoldController
{
  private static Logger logger = LoggerFactory.getLogger( SeatHoldController.class );

  @Autowired
  SeatHoldDao seatHoldDao;
  
  @RequestMapping( method = GET )
  public List<SeatHold> getSeatHolds()
  {
    logger.debug( "getSeatHolds" );
    return seatHoldDao.loadAll();
  }

  @RequestMapping( value = "/{id}", method = GET )
  public SeatHold getSeatHold( @PathVariable( "id" ) Long id )
  {
    logger.debug( "getSeatHold: id={}", id );    
    return seatHoldDao.loadById( id );
  }

  @RequestMapping( method = POST )
  @ResponseStatus( value = OK )
  public void createSeatHold( @RequestBody SeatHold seatHold )
  {
    logger.debug( "createSeatHold: SeatHold={}", seatHold );
    seatHoldDao.save( seatHold );
  }

  @RequestMapping( method = PUT )
  @ResponseStatus( value = OK )
  public void updateSeatHold( @RequestBody SeatHold seatHold )
  {
    logger.debug( "updateSeatHold: SeatHold={}", seatHold );
    seatHoldDao.save( seatHold );
  }

  @RequestMapping( value = "/{id}", method = DELETE )
  public void deleteSeatHold( @PathVariable( "id" ) Long id )
  {
    logger.debug( "deleteSeatHold: id={}", id );    
    seatHoldDao.deleteById( id );
  }
}
