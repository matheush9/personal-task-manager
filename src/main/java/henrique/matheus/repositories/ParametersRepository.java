package henrique.matheus.repositories;

import henrique.matheus.models.ParametersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParametersRepository extends JpaRepository<ParametersModel, UUID> {
    Optional<ParametersModel> findFirstByOrderByIdAsc();
}
