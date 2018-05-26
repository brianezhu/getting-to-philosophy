package philosophy.models;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface PageRepository extends CrudRepository<Page, Long> {
	
	Collection<Page> findByRunIdOrderByCreatedDateAsc(Long id);
	
	Optional<Page> findById(Long id);

}