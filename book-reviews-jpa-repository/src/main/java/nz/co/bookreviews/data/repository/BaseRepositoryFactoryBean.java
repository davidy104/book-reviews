package nz.co.bookreviews.data.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {
		return new BaseRepositoryFactory(entityManager);
	}

	protected static class BaseRepositoryFactory<T, I extends Serializable>
			extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public BaseRepositoryFactory(EntityManager entityManager) {
			super(entityManager);

			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new GenericBaseRepository<T, I>(
					(JpaEntityInformation<T, I>) getEntityInformation(metadata
							.getDomainType()),
					entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return GenericBaseRepository.class;
		}
	}
}
