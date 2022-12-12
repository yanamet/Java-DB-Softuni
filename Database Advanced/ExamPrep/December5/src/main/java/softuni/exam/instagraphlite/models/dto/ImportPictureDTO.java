package softuni.exam.instagraphlite.models.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class ImportPictureDTO {

    @NotNull
    private String path;

    @DecimalMin("500")
    @DecimalMax("60000")
    private float size;

    public String getPath() {
        return path;
    }

    public float getSize() {
        return size;
    }
}
