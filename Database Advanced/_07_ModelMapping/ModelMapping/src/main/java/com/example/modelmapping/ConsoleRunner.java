package com.example.modelmapping;

import com.example.modelmapping.exceprions.*;
import com.example.modelmapping.services.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final ExecutorService executorService;

    @Autowired
    public ConsoleRunner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("The program ends when the command \"End\" is entered.");

        Scanner scanner = new Scanner(System.in);

        String commandLine = scanner.nextLine();

        while (!commandLine.equals("End")) {
            try {
                System.out.println(this.executorService.execute(commandLine));
            } catch (ValidationException | UserNotLoggedInException | NoLoggedInUser
                     | UserAlreadyRegistered | InvalidGameParameters | UserIsNotAdministrator
                     | GameAlreadyAdded | NoSuchGameException err) {
                System.out.println(err.getMessage());
            }

            commandLine = scanner.nextLine();


        }


    }
}
