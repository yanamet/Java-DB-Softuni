package softuni.exam.instagraphlite.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootPostDTO {

    @XmlElement(name = "post")
    List<ImportPostDTO> posts;

    public List<ImportPostDTO> getPosts() {
        return posts;
    }
}
