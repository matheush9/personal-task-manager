package henrique.matheus.repositories;

import henrique.matheus.models.TaskModel;
import henrique.matheus.models.TaskTimeBetweenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface TaskTimeBetweenRepository extends JpaRepository<TaskTimeBetweenModel, UUID> {
    Optional<TaskTimeBetweenModel> findByTask(TaskModel task);
    Optional<TaskTimeBetweenModel> findByTaskAndTimeFinished(TaskModel task, LocalTime timeFinished);
}
