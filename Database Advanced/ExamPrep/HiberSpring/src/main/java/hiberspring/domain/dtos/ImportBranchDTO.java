package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;

public class ImportBranchDTO {

    @NotNull
    private String name;

    @NotNull
    private String town;

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }
}
