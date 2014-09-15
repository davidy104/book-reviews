package nz.co.bookreviews.data.test.util;

import javax.annotation.Resource;

import nz.co.bookreviews.config.InfrastructureContextConfiguration;
import nz.co.bookreviews.data.support.InitialDataSetup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@Import(value = { InfrastructureContextConfiguration.class })
public class RepositoryTestContextConfiguration {

	@Resource
	private PlatformTransactionManager transactionManager;

	@Bean(initMethod = "initialize")
	public InitialDataSetup initialDataSetup() {
		return new InitialDataSetup(new TransactionTemplate(transactionManager));
	}
}
