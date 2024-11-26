package henrique.matheus.repositories;

import henrique.matheus.models.DayModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DayRepository extends JpaRepository<DayModel, LocalDate> {
}
