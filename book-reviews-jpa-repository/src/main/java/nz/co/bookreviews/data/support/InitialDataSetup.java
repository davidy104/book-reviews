package nz.co.bookreviews.data.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nz.co.bookreviews.data.entity.AuthorEntity;
import nz.co.bookreviews.data.entity.BookAuthorEntity;
import nz.co.bookreviews.data.entity.BookEntity;
import nz.co.bookreviews.data.entity.CustomerEntity;
import nz.co.bookreviews.data.entity.PublicationEntity;
import nz.co.bookreviews.data.entity.PublisherEntity;
import nz.co.bookreviews.data.entity.UserEntity;
import nz.co.bookreviews.data.entity.VoteEntity;
import nz.co.bookreviews.data.util.AuthorBuilder;
import nz.co.bookreviews.data.util.BookBuilder;
import nz.co.bookreviews.data.util.CustomerBuilder;
import nz.co.bookreviews.data.util.EntityBuilder.EntityBuilderManager;
import nz.co.bookreviews.data.util.PublisherBuilder;
import nz.co.bookreviews.data.util.UserBuilder;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class InitialDataSetup {
	private final TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	Random rand = new Random();

	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	private UserEntity user1;
	private UserEntity user2;
	private UserEntity user3;
	private UserEntity user4;

	private CustomerEntity cust1;
	private CustomerEntity cust2;

	private AuthorEntity author1;
	private AuthorEntity author2;

	private PublisherEntity publisher;

	private PublicationEntity publication1;
	private PublicationEntity publication2;

	private BookEntity book1;
	private BookEntity book2;

	public InitialDataSetup(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void initialize() {
		EntityBuilderManager.setEntityManager(this.entityManager);
		this.transactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				// initial Users
				{
					user1 = new UserBuilder() {
						{
							this.create(
									"jordan",
									getDateFromFormat(DATE_FORMAT, "2013-01-01"));
						}
					}.build();

					user2 = new UserBuilder() {
						{
							this.create(
									"joen",
									getDateFromFormat(DATE_FORMAT, "2013-06-01"));
						}
					}.build();

					user3 = new UserBuilder() {
						{
							this.create(
									"chinuaAchebe",
									getDateFromFormat(DATE_FORMAT, "2014-04-01"));
						}
					}.build();

					user4 = new UserBuilder() {
						{
							this.create(
									"HansChristianAndersen",
									getDateFromFormat(DATE_FORMAT, "2014-01-01"));
						}
					}.build();

				}
				// initial Customers
				{
					cust1 = new CustomerBuilder() {
						{
							this.create(
									"Mike",
									"Jordan",
									"mike.jordan@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1970-01-01"))
									.setUser(user1);
						}
					}.build();

					cust2 = new CustomerBuilder() {
						{
							this.create(
									"Ni",
									"Joe",
									"ni.joe@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1990-01-01"))
									.setUser(user2);
						}
					}.build();
				}
				// bind user with customer
				{
					user1.setPerson(cust1);
					user2.setPerson(cust2);
				}

				// initial Authors
				{
					author1 = new AuthorBuilder() {
						{
							this.create("Achebe", "Chinua",
									"Chinua.Achebe@gmail.com", null).setUser(
									user3);
						}
					}.build();

					author2 = new AuthorBuilder() {
						{
							this.create("Andersen", "Hans Christian",
									"HansChristian.Andersen@gmail.com", null)
									.setUser(user4);
						}
					}.build();
				}

				// bind user with authors
				{
					user3.setPerson(author1);
					user4.setPerson(author2);
				}

				// initial publisher
				{
					publisher = new PublisherBuilder() {
						{
							this.create("Packt Publishing");
						}
					}.build();
				}

				{
					new BookBuilder() {
						{
							this.create("Things Fall Apart", 345, "novel",
									publication2)
									.addPublication(
											PublicationEntity.getBuilder(2012,
													publisher).build())
									.addBookAuthors(
											BookAuthorEntity.getBuilder(book1,
													author1).build())
									.addVotes(
											VoteEntity
													.getBuilder(
															book1,
															user1,
															5,
															getDateFromFormat(
																	TIME_FORMAT,
																	"2014-04-02 14:12:21"))
													.build(),
											VoteEntity
													.getBuilder(
															book1,
															user2,
															4,
															getDateFromFormat(
																	TIME_FORMAT,
																	"2014-05-02 10:32:21"))
													.build());
						}
					}.build();

					new BookBuilder() {
						{
							this.create("Fairy tales", 234, null, publication1)
									.addPublication(
											PublicationEntity.getBuilder(2013,
													publisher).build())
									.addBookAuthors(
											BookAuthorEntity.getBuilder(book2,
													author2).build())
									.addVotes(
											VoteEntity
													.getBuilder(
															book2,
															user2,
															3,
															getDateFromFormat(
																	TIME_FORMAT,
																	"2014-06-06 16:22:21"))
													.build());
						}
					}.build();
				}
				return null;
			}
		});
		EntityBuilderManager.clearEntityManager();
	}

	private static Date getDateFromFormat(DateFormat dateFormatter,
			String dateStr) {
		Date date = null;
		try {
			date = dateFormatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
