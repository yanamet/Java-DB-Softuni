package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportCarDTO {

    @Size(min = 2, max = 30)
    private String carMake;

    @Size(min = 2, max = 30)
    private String carModel;

    @Positive
    private int year;

    @Size(min = 2, max = 30)
    private String plateNumber;

    @Positive
    private int kilometers;

    @DecimalMin("1.00")
    private double engine;

    private CarType carType;

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getYear() {
        return year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public int getKilometers() {
        return kilometers;
    }

    public double getEngine() {
        return engine;
    }

    public CarType getCarType() {
        return carType;
    }
}
