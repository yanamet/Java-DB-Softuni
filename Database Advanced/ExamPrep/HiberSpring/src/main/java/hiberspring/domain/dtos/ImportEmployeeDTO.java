package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportEmployeeDTO {

    // <employee first-name="Rick" last-name="Sanchez" position="Head Scientist">
    //        <card>65RrK-NRzLZ-pJLZN-Chp3q-tovmA</card>
    //        <branch>Headquarters</branch>
    //    </employee>

    @XmlAttribute(name = "first-name")
    @NotNull
    private String firstName;

    @XmlAttribute(name = "last-name")
    @NotNull
    private String lastName;

    @XmlAttribute(name = "position")
    @NotNull
    private String position;

    @XmlElement
    @NotNull
    private String card;

    @XmlElement
    @NotNull
    private String branch;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public String getCard() {
        return card;
    }

    public String getBranch() {
        return branch;
    }
}
