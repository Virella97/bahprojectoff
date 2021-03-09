package com.bah.project.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

	   @Bean
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }
	
	   
	    @Bean
	    public static JaegerTracer getTracer(){

		Configuration.SamplerConfiguration sampleConfiguration = 

		Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);

		Configuration.ReporterConfiguration reporterConfiguration = 

		Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);

		Configuration config = new Configuration("MSD Project-Auth Tracing").
		
		withSampler(sampleConfiguration).withReporter(reporterConfiguration);

		return config.getTracer();

	    }
}
