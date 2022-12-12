package softuni.exam.domain.entities.dto;

import softuni.exam.domain.entities.Position;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportPlayerDTO {

    //  {
    //    "firstName": "Kiril",
    //    "lastName": "Despodov",
    //    "number": 32,
    //    "salary": 150000.00,
    //    "position": "Invalid",
    //    "picture": {
    //      "url": "google.pictures#1"
    //    },
    //    "team": {
    //      "name": "West Valley",
    //      "picture": {
    //        "url": "fc_pictures_1"
    //      }
    //    }
    //  },

    @NotNull
    private String firstName;

    @NotNull
    @Size(min = 3, max = 15)
    private String lastName;

    @Min(1)
    @Max(99)
    private int number;

    @NotNull
    @PositiveOrZero
    private BigDecimal salary;

    @NotNull
    private Position position;

    @NotNull
    private ImportPictureJsonDTO picture;

    private TeamNamePictureDTO team;

    public ImportPlayerDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNumber() {
        return number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Position getPosition() {
        return position;
    }

    public ImportPictureJsonDTO getPicture() {
        return picture;
    }

    public TeamNamePictureDTO getTeam() {
        return team;
    }
}
