package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootTaskDTO {

    @XmlElement(name = "task")
    private List<ImportTaskDTO> tasks;

    public List<ImportTaskDTO> getTasks() {
        return tasks;
    }

}
