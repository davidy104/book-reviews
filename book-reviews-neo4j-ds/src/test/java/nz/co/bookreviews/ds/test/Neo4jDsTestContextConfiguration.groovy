package nz.co.bookreviews.ds.test;

import nz.co.bookreviews.config.ApplicationContextConfiguration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [ApplicationContextConfiguration.class ])
@ComponentScan("nz.co.bookreviews.ds")
public class Neo4jDsTestContextConfiguration {
}
