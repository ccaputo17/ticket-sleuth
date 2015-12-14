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

import com.walmart.ticketsleuth.dao.SeatDao;
import com.walmart.ticketsleuth.model.Seat;

@RestController
@CrossOrigin( origins = { "http://localhost:8888", "http://localhost:8080" } )
@RequestMapping("/seats")
public class SeatController
{
  private static Logger logger = LoggerFactory.getLogger( SeatController.class );

  @Autowired
  SeatDao seatDao;
  
  @RequestMapping( method = GET )
  public List<Seat> getSeats()
  {
    return seatDao.loadAll();
  }

  @RequestMapping( value = "/{id}", method = GET )
  public Seat getSeat( @PathVariable( "id" ) Long id )
  {
    logger.debug( "id={}", id );
    
    return seatDao.loadById( id );
  }

  @RequestMapping( method = POST )
  @ResponseStatus( value = OK )
  public void createSeat( @RequestBody Seat seat )
  {
    logger.debug( "Seat={}", seat );
    seatDao.save( seat );
  }

  @RequestMapping( method = PUT )
  @ResponseStatus( value = OK )
  public void updateSeat( @RequestBody Seat seat )
  {
    logger.debug( "Seat={}", seat );
    seatDao.save( seat );
  }
}
