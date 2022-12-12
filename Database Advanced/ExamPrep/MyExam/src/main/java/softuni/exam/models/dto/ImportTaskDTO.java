package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTaskDTO {

    @XmlElement
    private String date;

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement(name = "car")
    private IdDTO car;

    @XmlElement
    private MechanicNameDTO mechanic;

    @XmlElement(name = "part")
    private IdDTO part;

    public String getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public IdDTO getCar() {
        return car;
    }

    public MechanicNameDTO getMechanic() {
        return mechanic;
    }

    public IdDTO getPart() {
        return part;
    }
}
