package nz.co.bookreviews.data.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import nz.co.bookreviews.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

public class GenericBaseRepository<T, ID extends Serializable> extends
		QueryDslJpaRepository<T, ID> implements BaseRepository<T, ID> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GenericBaseRepository.class);

	// All instance variables are available in super, but they are private
	private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
	private final EntityPath<T> path;
	private final PathBuilder<T> builder;
	private final Querydsl querydsl;

	public GenericBaseRepository(JpaEntityInformation<T, ID> entityMetadata,
			EntityManager entityManager) {
		super(entityMetadata, entityManager);
		this.path = DEFAULT_ENTITY_PATH_RESOLVER.createPath(entityMetadata
				.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
		this.querydsl = new Querydsl(entityManager, builder);
	}

	@Override
	public T deleteById(ID id) throws NotFoundException {
		LOGGER.debug("Deleting an entity with id: {}", id);
		T deleted = findOne(id);
		if (deleted == null) {
			LOGGER.debug("No entity found with id: {}", id);
			throw new NotFoundException("No entity found with id: " + id);
		}
		delete(deleted);
		LOGGER.debug("Deleted entity: {}", deleted);
		return deleted;
	}

	@Override
	public Page<T> findAll(FactoryExpression<T> factoryExpression,
			Predicate predicate, Pageable pageable) {
		JPQLQuery countQuery = createQuery(predicate);
		JPQLQuery query = querydsl.applyPagination(pageable,
				createQuery(predicate));
		Long total = countQuery.count();
		List<T> content = total > pageable.getOffset() ? query
				.list(factoryExpression) : Collections.<T> emptyList();
		return new PageImpl<T>(content, pageable, total);
	}

	@Override
	public T findOne(FactoryExpression<T> factoryExpression, Predicate predicate) {
		JPQLQuery query = createQuery(predicate);
		return query.singleResult(factoryExpression);
	}

}
