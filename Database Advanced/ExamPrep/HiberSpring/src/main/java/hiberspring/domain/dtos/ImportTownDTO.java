package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;

public class ImportTownDTO {

    @NotNull
    private String name;

    @NotNull
    private int population;

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
}
