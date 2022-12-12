package softuni.exam.models.dto;

import softuni.exam.models.entity.DaysOfWeek;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSingleForecastDTO {

    @XmlElement(name = "day_of_week")
    @NotNull
    private DaysOfWeek dayOfWeek;

    @XmlElement(name = "max_temperature")
    @Min(-20)
    @Max(60)
    private float maxTemperature;

    @XmlElement(name = "min_temperature")
    @Min(-50)
    @Max(40)
    private float minTemperature;

    @XmlElement
    @NotNull
    private String sunrise;

    @XmlElement
    @NotNull
    private String sunset;

    @XmlElement
    private long city;

    public ImportSingleForecastDTO() {}

    public DaysOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public long getCity() {
        return city;
    }
}
