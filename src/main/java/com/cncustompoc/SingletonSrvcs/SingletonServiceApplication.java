package com.cncustompoc.SingletonSrvcs;

import com.cncustompoc.SingletonSrvcs.common.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan( basePackages =  CommonParams.BASEPATH)
@ComponentScan(basePackages = CommonParams.BASEPATH)
@PropertySource(value = { "WEB-INF/application.properties" })
public class SingletonServiceApplication extends SpringBootServletInitializer {
	static Logger logger= LoggerFactory.getLogger(SingletonServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SingletonServiceApplication.class, args);
	}

}