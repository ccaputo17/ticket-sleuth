package com.walmart.ticketsleuth.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.ticketsleuth.dao.LevelDao;
import com.walmart.ticketsleuth.dao.SeatDao;
import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.DataModelFactory;
import com.walmart.ticketsleuth.model.Level;
import com.walmart.ticketsleuth.model.Seat;
import com.walmart.ticketsleuth.model.SeatHold;
import com.walmart.ticketsleuth.model.impl.DataModelFactoryImpl;
import com.walmart.ticketsleuth.model.impl.SeatHoldImpl;
import com.walmart.ticketsleuth.model.impl.SeatImpl;
import com.walmart.ticketsleuth.service.impl.TicketServiceImpl;

@ContextConfiguration( locations = { "classpath:/applicationContext-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
//@RunWith( MockitoJUnitRunner.class )
public class TicketServiceTest
{
  @InjectMocks
  private TicketService ticketService = new TicketServiceImpl();

  @Mock
  private VenueService venueService;

  @Mock
  private LevelDao levelDao;

  @Mock
  private SeatDao seatDao;

  @Mock
  private SeatHoldDao seatHoldDao;

  @Mock
  private DataModelFactory dataModelFactory;

  @Resource( name = "venueLevels" )
  private List<Level> venueLevels;

  @Resource( name = "unavailableSeats" )
  private List<Seat> unavailableSeats;

  @Resource( name = "availableSeats" )
  private List<Seat> availableSeats;

  @Autowired
  @Qualifier( "customerEmail" )
  private String customerEmail;

  @Before
  public void setup() 
  {
    dataModelFactory = new DataModelFactoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void testNumSeatsAvailable()
  {
    long minLevelId = venueLevels.get( 0 ).getId();
    long maxLevelId = venueLevels.get( venueLevels.size() - 1 ).getId();
    
    when( seatDao.loadByMinAndMaxLevel( minLevelId, maxLevelId ) ).thenReturn( unavailableSeats );
    when( venueService.getSeatingCapacity( minLevelId, maxLevelId ) ).thenReturn( 250 );
    assertEquals( ticketService.numSeatsAvailable( minLevelId, maxLevelId ), 244 );
    
    verify(venueService, times( 1 ) ).getSeatingCapacity( minLevelId, maxLevelId );
    verify(seatDao, times( 1 ) ).loadByMinAndMaxLevel( minLevelId, maxLevelId );
  }

  @Test
  public void testFindAndHoldSeats()
  {
    Set<Seat> availableSeatsSet = new HashSet<Seat>(availableSeats);
    Level minLevel = venueLevels.get( 0 );
    
    when( levelDao.loadByMinAndMaxLevel( minLevel.getId(), minLevel.getId() ) ).thenReturn( Arrays.asList( new Level[] { minLevel } ) );
    when( seatDao.loadByLevel( minLevel.getId() ) ).thenReturn( unavailableSeats );
    when( dataModelFactory.newSeat( minLevel, 1, 4 ) ).thenReturn( availableSeats.get( 0 ) );
    when( dataModelFactory.newSeat( minLevel, 1, 5 ) ).thenReturn( availableSeats.get( 1 ) );
    when( dataModelFactory.newSeat( minLevel, 1, 9 ) ).thenReturn( availableSeats.get( 2 ) );
    when( dataModelFactory.newSeat( minLevel, 1, 10 ) ).thenReturn( availableSeats.get( 3 ) );
    when( dataModelFactory.newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) ) ).thenReturn( new SeatHoldImpl( availableSeatsSet, null, customerEmail ) );
    
    SeatHold seatHold = ticketService.findAndHoldSeats( 4, minLevel.getId(), minLevel.getId(), customerEmail );

    assertNotNull( seatHold );
    assertEquals( seatHold.getCustomerEmail(), customerEmail );
    assertEquals( seatHold.getSeats().size(), 4 );
    
    verify(levelDao, times( 1 ) ).loadByMinAndMaxLevel( minLevel.getId(), minLevel.getId() );
    verify(seatDao, times( 1 ) ).loadByLevel( minLevel.getId() );
    verify(dataModelFactory, times( 4 ) ).newSeat( any( Level.class ), anyInt(), anyInt() );
    verify(dataModelFactory, times( 1 ) ).newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) );
  }

  @Test
  public void testFindAndHoldSeatsTooMany()
  {
    Level minLevel = venueLevels.get( 0 );
    
    when( levelDao.loadByMinAndMaxLevel( minLevel.getId(), minLevel.getId() ) ).thenReturn( Arrays.asList( new Level[] { minLevel } ) );
    when( seatDao.loadByLevel( minLevel.getId() ) ).thenReturn( unavailableSeats );
    when( dataModelFactory.newSeat( any( Level.class ), anyInt(), anyInt() ) ).thenAnswer( new Answer<Seat>() {
      @Override
      public Seat answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatImpl( (Level)args[0], (int)args[1], (int)args[2] );
      }
    });
    when( dataModelFactory.newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) ) ).thenAnswer( new Answer<SeatHold>() {
      @SuppressWarnings( "unchecked" )
      @Override
      public SeatHold answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatHoldImpl( (Set<Seat>)args[0], (DateTime)args[1], customerEmail );
      }
    });
    
    SeatHold seatHold = ticketService.findAndHoldSeats( 50, minLevel.getId(), minLevel.getId(), customerEmail );

    assertNotNull( seatHold );
    assertEquals( seatHold.getCustomerEmail(), customerEmail );
    assertNull( seatHold.getSeats() );
    
    verify(levelDao, times( 1 ) ).loadByMinAndMaxLevel( minLevel.getId(), minLevel.getId() );
    verify(seatDao, times( 1 ) ).loadByLevel( minLevel.getId() );
    verify(dataModelFactory, times( 44 ) ).newSeat( any( Level.class ), anyInt(), anyInt() );
    verify(dataModelFactory, times( 1 ) ).newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) );
  }

  @Test
  public void testFindAndHoldSeatsMultipleLevels()
  {
    Level minLevel = venueLevels.get( 0 );
    Level maxLevel = venueLevels.get( 1 );
    
    when( levelDao.loadByMinAndMaxLevel( minLevel.getId(), maxLevel.getId() ) ).thenReturn( Arrays.asList( new Level[] { minLevel, maxLevel } ) );
    when( seatDao.loadByLevel( minLevel.getId() ) ).thenReturn( unavailableSeats );
    when( seatDao.loadByLevel( maxLevel.getId() ) ).thenReturn( Collections.<Seat> emptyList() );
    when( dataModelFactory.newSeat( any( Level.class ), anyInt(), anyInt() ) ).thenAnswer( new Answer<Seat>() {
      @Override
      public Seat answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatImpl( (Level)args[0], (int)args[1], (int)args[2] );
      }
    });
    when( dataModelFactory.newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) ) ).thenAnswer( new Answer<SeatHold>() {
      @SuppressWarnings( "unchecked" )
      @Override
      public SeatHold answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatHoldImpl( (Set<Seat>)args[0], (DateTime)args[1], customerEmail );
      }
    });
    
    SeatHold seatHold = ticketService.findAndHoldSeats( 50, minLevel.getId(), maxLevel.getId(), customerEmail );

    assertNotNull( seatHold );
    assertEquals( seatHold.getCustomerEmail(), customerEmail );
    assertNotNull( seatHold.getSeats() );
    
    verify(levelDao, times( 1 ) ).loadByMinAndMaxLevel( minLevel.getId(), maxLevel.getId() );
    verify(seatDao, times( 2 ) ).loadByLevel( anyLong() );
    verify(dataModelFactory, times( 50 ) ).newSeat( any( Level.class ), anyInt(), anyInt() );
    verify(dataModelFactory, times( 1 ) ).newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) );
  }

  @Test
  public void testFindAndHoldSeatsSwapMinMax()
  {
    Level minLevel = venueLevels.get( 1 );
    Level maxLevel = venueLevels.get( 0 );
    
    when( levelDao.loadByMinAndMaxLevel( maxLevel.getId(), minLevel.getId() ) ).thenReturn( Arrays.asList( new Level[] { maxLevel, minLevel } ) );
    when( seatDao.loadByLevel( minLevel.getId() ) ).thenReturn( Collections.<Seat> emptyList() );
    when( seatDao.loadByLevel( maxLevel.getId() ) ).thenReturn( unavailableSeats );
    when( dataModelFactory.newSeat( any( Level.class ), anyInt(), anyInt() ) ).thenAnswer( new Answer<Seat>() {
      @Override
      public Seat answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatImpl( (Level)args[0], (int)args[1], (int)args[2] );
      }
    });
    when( dataModelFactory.newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) ) ).thenAnswer( new Answer<SeatHold>() {
      @SuppressWarnings( "unchecked" )
      @Override
      public SeatHold answer(InvocationOnMock invocation) throws Throwable 
      {
        Object[] args = invocation.getArguments();
        return new SeatHoldImpl( (Set<Seat>)args[0], (DateTime)args[1], customerEmail );
      }
    });
    
    SeatHold seatHold = ticketService.findAndHoldSeats( 50, minLevel.getId(), maxLevel.getId(), customerEmail );

    assertNotNull( seatHold );
    assertEquals( seatHold.getCustomerEmail(), customerEmail );
    assertNotNull( seatHold.getSeats() );
    
    verify(levelDao, never() ).loadByMinAndMaxLevel( minLevel.getId(), maxLevel.getId() );
    verify(levelDao, times( 1 ) ).loadByMinAndMaxLevel( maxLevel.getId(), minLevel.getId() );
    verify(seatDao, times( 2 ) ).loadByLevel( anyLong() );
    verify(dataModelFactory, times( 50 ) ).newSeat( any( Level.class ), anyInt(), anyInt() );
    verify(dataModelFactory, times( 1 ) ).newSeatHold( anySetOf( Seat.class ), any( DateTime.class ), eq( customerEmail ) );
  }

  @Test
  public void testReserveSeats()
  {
    SeatHold seatHold = new SeatHoldImpl( new HashSet<Seat>(availableSeats), new DateTime().plusMinutes( 5 ), customerEmail );
    seatHold.setId( new Long( 1 ) );
    
    when( seatHoldDao.loadById( seatHold.getId() ) ).thenReturn( seatHold );
    
    String confirmationNumber = ticketService.reserveSeats( seatHold.getId(), customerEmail );

    assertNotNull( confirmationNumber );
    assertTrue( confirmationNumber.trim().length() > 0 );
    
    verify(seatHoldDao, times( 1 ) ).loadById( seatHold.getId() );
    verify(seatDao, times( 4 ) ).save( any( Seat.class ) );
    verify(seatHoldDao, times( 1 ) ).delete( seatHold );
  }

  @Test
  public void testReserveSeatsUnknownId()
  {
    when( seatHoldDao.loadById( anyLong() ) ).thenReturn( null );
    
    String confirmationNumber = ticketService.reserveSeats( new Long( 1 ), customerEmail );

    assertNull( confirmationNumber );
    
    verify(seatHoldDao, times( 1 ) ).loadById(anyLong() );
    verify(seatDao, never() ).save( any( Seat.class ) );
    verify(seatHoldDao, never() ).delete( any( SeatHold.class ) );
  }

  @Test
  public void testReleaseSeats()
  {
    SeatHold seatHold = new SeatHoldImpl( new HashSet<Seat>(availableSeats), new DateTime().plusMinutes( 5 ), customerEmail );
    seatHold.setId( new Long( 1 ) );
    
    when( seatHoldDao.loadById( seatHold.getId() ) ).thenReturn( seatHold );
    
    ticketService.releaseSeats( seatHold.getId() );
    
    verify(seatHoldDao, times( 1 ) ).loadById( seatHold.getId() );
    verify(seatDao, times( 4 ) ).delete( any( Seat.class ) );
    verify(seatHoldDao, times( 1 ) ).delete( seatHold );
  }

  @Test
  public void testReleaseSeatsUnknownId()
  {
    when( seatHoldDao.loadById( anyLong() ) ).thenReturn( null );
    
    ticketService.releaseSeats( new Long( 1 ) );
    
    verify(seatHoldDao, times( 1 ) ).loadById(anyLong() );
    verify(seatDao, never() ).delete( any( Seat.class ) );
    verify(seatHoldDao, never() ).delete( any( SeatHold.class ) );
  }
}
