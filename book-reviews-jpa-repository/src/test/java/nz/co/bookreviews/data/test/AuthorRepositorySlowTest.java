package nz.co.bookreviews.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import nz.co.bookreviews.data.entity.AuthorEntity;
import nz.co.bookreviews.data.repository.AuthorRepository;
import nz.co.bookreviews.data.repository.UserRepository;
import nz.co.bookreviews.data.test.util.RepositoryTestContextConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositorySlowTest {
	@Resource
	private AuthorRepository authorRepository;

	@Resource
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorRepositorySlowTest.class);

	private static final String TEST_FIRST_NAME = "Chinua";
	private static final String TEST_LAST_NAME = "Achebe";

	@Test
	@Transactional(readOnly = true)
	public void testNativeQuery() {
		List<AuthorEntity> authors = this.authorRepository
				.findAuthorsByLastNameOrFirstName(TEST_FIRST_NAME);
		assertEquals(1, authors.size());
		AuthorEntity author = authors.get(0);
		assertNotNull(author);
		LOGGER.info("author: {} ", author);
		assertEquals(TEST_FIRST_NAME, author.getFirstName());
		assertEquals(TEST_LAST_NAME, author.getLastName());
	}

}
