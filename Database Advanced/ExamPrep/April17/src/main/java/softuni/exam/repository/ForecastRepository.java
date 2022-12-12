package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByCityAndDayOfWeek(City city, DaysOfWeek dayOfWeek);

    //•	Filter only forecasts from sunday and from cities with less than 150000 citizens,
    // order them by max temperature in descending order, then by the forecast id in ascending order.

    List<Forecast> findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc
            (DaysOfWeek daysOfWeek, int population);

}
