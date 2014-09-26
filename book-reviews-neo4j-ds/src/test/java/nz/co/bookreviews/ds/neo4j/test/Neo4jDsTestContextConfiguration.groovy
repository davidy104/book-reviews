package nz.co.bookreviews.ds.neo4j.test;

import nz.co.bookreviews.config.SharedApplicationContextConfiguration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [SharedApplicationContextConfiguration.class ])
@ComponentScan("nz.co.bookreviews.ds.neo4j")
public class Neo4jDsTestContextConfiguration {
}
