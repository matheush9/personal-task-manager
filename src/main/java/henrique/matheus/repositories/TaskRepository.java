package henrique.matheus.repositories;

import henrique.matheus.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
     Optional<TaskModel> findByDescription(String description);
     Optional<List<TaskModel>> findByDate(LocalDate date);
     Optional<TaskModel> findByDescriptionAndDate(String description, LocalDate date);
     Integer countByDate(LocalDate date);
     Optional<List<TaskModel>> findByDateOrderByPositionAsc(LocalDate date);
     Optional<TaskModel> findByDateAndPosition(LocalDate date, Integer position);

     @Query("SELECT SUM(t.durationInSeconds) FROM TaskModel t WHERE t.durationInSeconds > 0 AND t.date = :date")
     Optional<Long> getTotalDurationInSeconds(@Param("date") LocalDate date);
}
