package henrique.matheus.services;

import henrique.matheus.models.DayModel;
import henrique.matheus.repositories.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class DayService {
    @Autowired
    private DayRepository dayRepository;

    public void loadDay() {
        var dayExists = dayRepository.existsById(LocalDate.now());
        if (!dayExists) {
            var newDay = new DayModel();
            newDay.setDate(LocalDate.now());
            newDay.setStartDayTime(LocalTime.now());
            dayRepository.save(newDay);
        }
    }

    public Optional<DayModel> getCurrentDay() {
        return dayRepository.findById(LocalDate.now());
    }
}
