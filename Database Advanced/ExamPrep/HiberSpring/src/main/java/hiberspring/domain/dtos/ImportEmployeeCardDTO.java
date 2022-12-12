package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;

public class ImportEmployeeCardDTO {

    @NotNull
    private String number;

    public String getNumber() {
        return number;
    }
}
