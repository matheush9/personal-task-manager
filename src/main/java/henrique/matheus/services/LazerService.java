package henrique.matheus.services;

import henrique.matheus.repositories.LazerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LazerService {
    private final LazerRepository lazerRepository;

    public LazerService(LazerRepository lazerRepository) {
        this.lazerRepository = lazerRepository;
    }

    public void addMinutes(Integer position, LocalDate date, Integer minutes) {
        var lazer = lazerRepository.findByDateAndPosition(date, position);
        if (lazer.isPresent()) {
            lazer.get().setDurationInSeconds(
                    Optional.ofNullable(lazer.get().getDurationInSeconds())
                            .orElse(0L) + (60L * minutes)
            );
            lazerRepository.save(lazer.get());
        }
    }
}
