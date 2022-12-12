package softuni.exam.domain.entities.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootTeamDTO {

    @XmlElement(name = "team")
    List<ImportTeamDTO> teams;

    public List<ImportTeamDTO> getTeams() {
        return teams;
    }
}
