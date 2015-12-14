package com.walmart.ticketsleuth.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil implements InitializingBean
{
  private static Logger logger = LoggerFactory.getLogger( MessageUtil.class );

  @Autowired
  private MessageSource messageSource;

  @Autowired( required = false )
  @Qualifier( "defaultLocale" )
  private Locale defaultLocale;

  public String getMessage( String code )
  {
    return getMessage( code, defaultLocale, (Object[])null );
  }

  public String getMessage( String code, Object... args )
  {
    return getMessage( code, defaultLocale, args );
  }

  public String getMessage( String code, Locale locale )
  {
    return getMessage( code, locale, (Object[])null );
  }

  public String getMessage( String code, Locale locale, Object... args )
  {
    if ( StringUtils.isBlank( code ) )
    {
      return null;
    }
    
    try
    {
      return messageSource.getMessage( code, args, locale );
    }
    catch ( NoSuchMessageException e )
    {
      logger.warn( "No message found for code '{}' and locale '{}'", code, locale.toLanguageTag() );
      return code;
    }
  }
  
  @Override
  public void afterPropertiesSet() throws Exception
  {
    if ( defaultLocale == null )
    {
      defaultLocale = Locale.getDefault();
    }
  }
}
