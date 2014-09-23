package nz.co.bookreviews.ds.jpa.test;

import javax.annotation.Resource

import nz.co.bookreviews.config.InfrastructureContextConfiguration
import nz.co.bookreviews.data.support.InitialDataSetup

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Configuration
@Import(value = [InfrastructureContextConfiguration.class ])
@ComponentScan("nz.co.bookreviews.ds.jpa")
public class JpaDsTestContextConfiguration {

	@Resource
	PlatformTransactionManager transactionManager;

	@Bean(initMethod = "initialize")
	InitialDataSetup initialDataSetup() {
		return new InitialDataSetup(new TransactionTemplate(transactionManager))
	}
}
