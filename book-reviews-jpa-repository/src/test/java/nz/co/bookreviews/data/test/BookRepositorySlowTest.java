package nz.co.bookreviews.data.test;

import java.util.List;

import javax.annotation.Resource;

import nz.co.bookreviews.data.entity.BookEntity;
import nz.co.bookreviews.data.repository.BookRepository;
import nz.co.bookreviews.data.test.util.RepositoryTestContextConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositorySlowTest {
	@Resource
	private BookRepository bookRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookRepositorySlowTest.class);

	@Test
	public void testFindAll() {
		List<BookEntity> books = bookRepository.findAll();
		LOGGER.info("books size: {} ", books.size());
	}

}
