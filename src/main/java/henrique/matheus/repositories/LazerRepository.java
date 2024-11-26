package henrique.matheus.repositories;

import henrique.matheus.models.LazerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LazerRepository extends JpaRepository<LazerModel, UUID> {
    Optional<LazerModel> findByDescriptionAndDate(String description, LocalDate date);
    Optional<List<LazerModel>> findByDateOrderByPositionAsc(LocalDate date);
    Optional<LazerModel> findByDateAndPosition(LocalDate date, Integer position);
    Integer countByDate(LocalDate date);

    @Query("SELECT SUM(l.durationInSeconds) FROM LazerModel l WHERE l.durationInSeconds > 0 AND l.date = :date")
    Optional<Long> getTotalDurationInSeconds(@Param("date") LocalDate date);
}
