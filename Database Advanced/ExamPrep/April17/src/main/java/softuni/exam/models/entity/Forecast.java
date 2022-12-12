package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "forecasts")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "day_of_week", nullable = false)
    private DaysOfWeek dayOfWeek;

    @Column(name = "max_temperature", nullable = false)
    private float maxTemperature;

    @Column(name = "min_temperature", nullable = false)
    private float minTemperature;

    @Column(nullable = false)
    private LocalTime sunrise;

    @Column(nullable = false)
    private LocalTime sunset;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToOne(optional = false)
    private City city;

    public Forecast() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DaysOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DaysOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return String.format("City: %s:\n" +
                "-min temperature: %.2f\n" +
                "--max temperature: %.2f\n" +
                "---sunrise: %s\n" +
                "----sunset: %s", this.getCity().getCityName(),
                this.getMinTemperature(), this.getMaxTemperature(),
                this.sunrise.toString(), this.sunset.toString());
    }
}
