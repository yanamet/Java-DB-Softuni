package softuni.exam.domain.entities.dto;

import javax.validation.constraints.NotNull;

public class ImportPictureJsonDTO {

    @NotNull
    private String url;

    public ImportPictureJsonDTO() {}

    public String getUrl() {
        return url;
    }
}
