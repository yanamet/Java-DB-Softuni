package softuni.exam.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class ImportMechanicDTO {

    @Email
    private String email;

    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    private String lastName;

    @Size(min = 2)
    private String phone;

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
}
