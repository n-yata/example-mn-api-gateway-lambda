package example.micronaut.repository;

import example.micronaut.domain.Mutter;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MutterRepository extends CrudRepository<Mutter, Integer> {

    Mutter findById(int id);

    Mutter findByName(String name);
}
