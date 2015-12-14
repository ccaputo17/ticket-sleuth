package com.walmart.ticketsleuth.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class Initialize
{
  @Autowired
  private ObjectMapper objectMapper;
  
  @PostConstruct
  public void initializeObjectMapper()
  {
    objectMapper.registerModule(new JodaModule());
  }
}
