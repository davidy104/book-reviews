package nz.co.bookreviews.data.repository;

import java.io.Serializable;

import nz.co.bookreviews.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Predicate;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {

	/**
	 * Deletes an entity
	 * 
	 * @param id
	 *            The id of the deleted entity.
	 * @return The deleted entity
	 * @throws NotFoundException
	 *             if an entity is not found with the given id.'
	 */
	public T deleteById(ID id) throws NotFoundException;

	/**
	 * Returns a {@link org.springframework.data.domain.Page} of entities
	 * matching the given {@link com.mysema.query.types.Predicate}. This also
	 * uses provided projections ( can be JavaBean or constructor or anything
	 * supported by QueryDSL
	 * 
	 * @param factoryExpression
	 *            this constructor expression will be used for transforming
	 *            query results
	 * @param predicate
	 * @param pageable
	 * @return
	 */
	Page<T> findAll(FactoryExpression<T> factoryExpression,
			Predicate predicate, Pageable pageable);

	/**
	 * factoryExpression can be used to filter out not needed data
	 * @param factoryExpression 
	 * @param predicate
	 * @return
	 */
	T findOne(FactoryExpression<T> factoryExpression, Predicate predicate);

}
