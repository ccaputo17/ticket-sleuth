package com.walmart.ticketsleuth.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.service.VenueService;

@RestController
@RequestMapping("/venue")
public class VenueServiceController
{
  private static Logger logger = LoggerFactory.getLogger( VenueServiceController.class );

  @Autowired
  VenueService venueService;
  
  @RequestMapping( value = "/capacity", method = GET )
  public List<Level> getSeatingLevels()
  {
    logger.debug( "getSeatingLevels" );
    return venueService.getSeatingLevels();
  }

  @RequestMapping( value = "/{levelId}/capacity", method = GET )
  public Integer getSeatingCapacity( @PathVariable Long levelId )
  {
    logger.debug( "getSeatingCapacity: levelId={}", levelId );
    return venueService.getSeatingCapacity( levelId, levelId );
  }
}
