package henrique.matheus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="DAYS")
public class DayModel {
    @Id
    private LocalDate date;

    private LocalTime startDayTime;

    private Boolean isWorkDay = false;

    public Boolean getWorkDay() {
        return isWorkDay;
    }

    public void setWorkDay(Boolean workDay) {
        isWorkDay = workDay;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartDayTime() {
        return startDayTime;
    }

    public void setStartDayTime(LocalTime startDayTime) {
        this.startDayTime = startDayTime;
    }
}
