package com.example.modelmapping.entities.users;


import com.example.modelmapping.exceprions.ValidationException;

public class RegistrationDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;

    /**
     *
     *
     * commandParts[0] is skipped because it contains the name of the command
     * @param commandParts  reads all the data from the console
     */
    public RegistrationDTO(String[] commandParts) {
        this.email = commandParts[1];
        this.password = commandParts[2];
        this.confirmPassword = commandParts[3];
        this.fullName = commandParts[4];
        this.validate();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void validate() throws ValidationException {
        int indexOfAt = email.indexOf('@');
        int indexOfDot = email.lastIndexOf('.');
        if(indexOfAt < 0 || indexOfDot < 0 || indexOfAt > indexOfDot){
            throw new ValidationException("Email must contain [@] and [.].");
        }

        if (!password.equals(confirmPassword)){
            throw new ValidationException("Password and confirm password must match.");
        }

    }
}
