package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTicketDTO {

    @XmlElement(name = "serial-number")
    @Size(min = 2)
    private String serialNumber;

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement(name = "take-off")
    private String takeOff;

    @XmlElement(name = "from-town")
    private TownNameDTO fromTown;

    @XmlElement(name = "to-town")
    private TownNameDTO toTown;

    @XmlElement(name = "passenger")
    private PassengerEmailDTO passenger;

    @XmlElement(name = "plane")
    private PlaneRegisterNumberDTO plane;

    public String getSerialNumber() {
        return serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTakeOff() {
        return takeOff;
    }

    public TownNameDTO getFromTown() {
        return fromTown;
    }

    public TownNameDTO getToTown() {
        return toTown;
    }

    public PassengerEmailDTO getPassenger() {
        return passenger;
    }

    public PlaneRegisterNumberDTO getPlane() {
        return plane;
    }
}
