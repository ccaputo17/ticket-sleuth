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

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.model.Level;

@RestController
@CrossOrigin( origins = { "http://localhost:8888", "http://localhost:8080" } )
@RequestMapping( "/levels" )
public class LevelController
{
  private static Logger logger = LoggerFactory.getLogger( LevelController.class );

  @Autowired
  LevelDao levelDao;
  
  @RequestMapping( method = GET )
  public List<Level> getLevels()
  {
    return levelDao.loadAll();
  }

  @RequestMapping( value = "/{id}", method = GET )
  public Level getLevel( @PathVariable( "id" ) Long id )
  {
    logger.debug( "id={}", id );
    
    return levelDao.loadById( id );
  }

  @RequestMapping( method = POST )
  @ResponseStatus( value = OK )
  public void createLevel( @RequestBody Level level )
  {
    logger.debug( "Level={}", level );
    levelDao.save( level );
  }

  @RequestMapping( method = PUT )
  @ResponseStatus( value = OK )
  public void updateLevel( @RequestBody Level level )
  {
    logger.debug( "Level={}", level );
    levelDao.save( level );
  }
}
