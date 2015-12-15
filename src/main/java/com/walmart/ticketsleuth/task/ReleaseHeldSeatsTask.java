package com.walmart.ticketsleuth.task;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.walmart.ticketsleuth.dao.SeatHoldDao;
import com.walmart.ticketsleuth.model.SeatHold;
import com.walmart.ticketsleuth.service.TicketService;

@Component
public class ReleaseHeldSeatsTask
{
  private static Logger logger = LoggerFactory.getLogger( ReleaseHeldSeatsTask.class );

  @Value( "${release-held-ticket.delay:60000}")
  private long fixedDelay;
  
  @Autowired
  TicketService ticketService;
  
  @Autowired
  SeatHoldDao seatHoldDao;
  
  @Scheduled( fixedDelay = 60000 )
  public void executeTask()
  {
    logger.debug( "ReleaseHeldSeatsTask running... {}", new DateTime() );

    List<SeatHold> expired = seatHoldDao.loadByExpirationDate( new DateTime(), true );

    for ( SeatHold seatHold : expired )
    {
      logger.debug( "Deleting expired seatHold: {}", seatHold );
      ticketService.releaseSeats( seatHold.getId() );
    }
  }
}
