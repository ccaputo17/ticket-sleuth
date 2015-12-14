package com.walmart.ticketsleuth.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.service.impl.VenueServiceImpl;

@ContextConfiguration( locations = { "classpath:/applicationContext-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
//@RunWith( MockitoJUnitRunner.class )
public class VenueServiceTest
{
  @InjectMocks
  private VenueService venueService = new VenueServiceImpl();

  @Mock
  private LevelDao levelDao;

  @Resource( name = "venueLevels" )
  private List<Level> venueLevels;

  @Before
  public void setup() 
  {
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void testGetSeatingLevels()
  {
    when( levelDao.loadAll() ).thenReturn( venueLevels );

    assertEquals( venueService.getSeatingLevels(), venueLevels );
    verify(levelDao, times( 1 ) ).loadAll();
  }

  @Test
  public void testGetSeatingCapacity()
  {
    long minLevelId = venueLevels.get( 0 ).getId();
    long maxLevelId = venueLevels.get( venueLevels.size() - 1 ).getId();
    
    when( levelDao.loadByMinAndMaxLevel( minLevelId, maxLevelId ) ).thenReturn( venueLevels );
    assertEquals( venueService.getSeatingCapacity( minLevelId, maxLevelId ), 250 );
    
    verify(levelDao, times( 1 ) ).loadByMinAndMaxLevel( minLevelId, maxLevelId );
  }
}
