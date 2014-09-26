package nz.co.bookreviews.config;

import javax.annotation.Resource

import nz.co.bookreviews.data.support.InitialDataSetup

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Configuration
@ComponentScan("nz.co.bookreviews")
class ApplicationContextConfiguration {
	@Resource
	PlatformTransactionManager transactionManager

	@Bean(initMethod = "initialize")
	InitialDataSetup initialDataSetup() {
		return new InitialDataSetup(new TransactionTemplate(transactionManager))
	}
}
