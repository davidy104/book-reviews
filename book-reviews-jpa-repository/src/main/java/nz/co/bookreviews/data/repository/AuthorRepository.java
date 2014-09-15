package nz.co.bookreviews.data.repository;

import java.util.List;

import nz.co.bookreviews.data.entity.AuthorEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends BaseRepository<AuthorEntity, Long> {

	@Query(value = "SELECT * FROM T_AUTHOR a,T_PERSON p WHERE a.PERSON_ID = p.PERSON_ID AND (LOWER(p.first_name) LIKE LOWER(:searchTerm) OR LOWER(p.last_name) LIKE LOWER(:searchTerm)) ORDER BY p.last_name ASC, p.first_name ASC", nativeQuery = true)
	public List<AuthorEntity> findAuthorsByLastNameOrFirstName(
			@Param("searchTerm") String searchTerm);
}
