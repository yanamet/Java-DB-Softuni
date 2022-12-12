package hiberspring.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportProductDTO {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private int clients;

    @XmlElement
    private String branch;

    public ImportProductDTO() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public int getClients() {
        return clients;
    }

    public String getBranch() {
        return branch;
    }
}
