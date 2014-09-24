package nz.co.bookreviews.config;

import groovy.json.JsonSlurper

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.filter.LoggingFilter
import com.sun.jersey.api.json.JSONConfiguration

@Configuration
public class ApplicationContextConfiguration {

	@Bean(destroyMethod = "destroy")
	Client jerseyClient() {
		com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig()
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE)
		Client client = Client.create(config)
		client.setConnectTimeout(10000)
		client.setReadTimeout(10000)
		client.addFilter(new LoggingFilter(System.out))
		return client
	}

	@Bean
	static PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer()
		ClassPathResource[] resources =  [
			new ClassPathResource('neo4j.properties')
		]
		ppc.setLocations(resources)
		ppc.setIgnoreUnresolvablePlaceholders(true)
		return ppc
	}
	
	@Bean
	JsonSlurper jsonSlurper(){
		return new JsonSlurper()
	}
}
