package softuni.exam.domain.entities.dto;

import javax.validation.constraints.NotNull;

public class TeamNamePictureDTO {


    @NotNull
    private String name;

    private ImportPictureJsonDTO picture;

    public TeamNamePictureDTO() {}

    public String getName() {
        return name;
    }

    public ImportPictureJsonDTO getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(ImportPictureJsonDTO picture) {
        this.picture = picture;
    }
}
