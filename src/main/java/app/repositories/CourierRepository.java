package app.repositories;

import app.model.Courier;
import org.springframework.data.repository.CrudRepository;

public interface CourierRepository extends CrudRepository<Courier, Long>
{
}
