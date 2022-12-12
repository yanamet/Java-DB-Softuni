package com.example.modelmapping.entities.users;

public class LoginDTO {

    private String email;
    private String password;

    /**
     * command[0] is skipped because it is the name of the command
     * which is not needed
     *
     * @param commands holds login information
     */
    public LoginDTO(String[] commands) {
        this.email = commands[1];
        this.password = commands[2];
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
}
