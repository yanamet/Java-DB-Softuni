package com.example.football.models.dto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatsDTO {

    @XmlElement(name = "stat")
    List<ImportSingleStatDTO> stats;

    public ImportStatsDTO(List<ImportSingleStatDTO> stats) {
        this.stats = stats;
    }

    public ImportStatsDTO() {
    }

    public List<ImportSingleStatDTO> getStats() {
        return stats;
    }
}
