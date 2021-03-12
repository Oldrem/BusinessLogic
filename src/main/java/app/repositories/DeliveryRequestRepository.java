package app.repositories;

import app.model.DeliveryRequest;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRequestRepository extends CrudRepository<DeliveryRequest, Long> {
}
