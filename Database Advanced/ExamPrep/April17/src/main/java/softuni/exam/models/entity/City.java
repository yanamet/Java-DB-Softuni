package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "city_name", nullable = false, unique = true)
    private String cityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int population;

    @ManyToOne
    private Country country;

    @OneToMany(mappedBy = "city", targetEntity = Forecast.class)
    private Set<Forecast> forecasts;

    public Set<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(Set<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public City() {}

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String name) {
        this.cityName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
