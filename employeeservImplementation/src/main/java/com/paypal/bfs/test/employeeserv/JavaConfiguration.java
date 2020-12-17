package com.paypal.bfs.test.employeeserv;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class JavaConfiguration {
 //... other beans
  
 @Bean(initMethod="start",destroyMethod="stop")
 public org.h2.tools.Server h2WebConsonleServer () throws SQLException {
   return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8082");
 }
}