package nz.co.bookreviews.data.repository;

import java.util.List;

import nz.co.bookreviews.data.entity.CustomerEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends
		BaseRepository<CustomerEntity, Long> {
	// Method-Name Query
	List<CustomerEntity> findByFirstNameStartingWithOrLastNameStartingWith(
			String firstName, String lastName);

	@Query("SELECT c, u FROM CustomerEntity c, UserEntity u WHERE c.user = u AND (LOWER(c.firstName) LIKE LOWER(:searchTerm) OR LOWER(c.lastName) LIKE LOWER(:searchTerm))")
//	@Query("SELECT c FROM CustomerEntity c WHERE LOWER(c.firstName) LIKE LOWER(:searchTerm) OR LOWER(c.lastName) LIKE LOWER(:searchTerm)")
	List<CustomerEntity> findCustomers(@Param("searchTerm") String searchTerm);
}
