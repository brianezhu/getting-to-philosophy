package philosophy.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RunRepository extends CrudRepository<Run, Long> {

    List<Run> findByStartingPath(String startingPath);

}