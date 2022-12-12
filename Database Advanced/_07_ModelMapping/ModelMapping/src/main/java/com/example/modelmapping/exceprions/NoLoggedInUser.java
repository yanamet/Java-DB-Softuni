package com.example.modelmapping.exceprions;

import javax.swing.plaf.SeparatorUI;

public class NoLoggedInUser extends RuntimeException {
    public NoLoggedInUser() {
        super("Cannot log out. No user is logged in.");
    }
}
