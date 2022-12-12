package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportTownDTO {

    @Size(min = 2)
    private String name;

    @Positive
    private int population;

    private String guide;

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public String getGuide() {
        return guide;
    }
}
