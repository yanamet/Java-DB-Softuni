package softuni.exam.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //•	id – integer number, primary identification field.
    //•	first_name – a string (required).
    //•	last_name – a string (required) between 3 and 15 characters.
    //•	number – a Integer (required) between 1 and 99.
    //•	salary – a Bigdecimal (required) min 0.
    //•	position – a ENUM (required).
    //•	picture – a Picture entity (required).
    //•	team – a Team entity (required).

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private int number;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne(optional = false)
    private Picture picture;

    @ManyToOne(optional = false)
    private Team team;

    public Player() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String playerTeamToString(){
        return String.format("\tPlayer name: %s %s - %s\n" +
                "\tNumber: %d",
                this.getFirstName(), this.getLastName(), this.getPosition().toString(),
                this.getNumber());
    }

    public String playerWithSalaryToString(){
        return String.format("Player name: %s %s\n" +
                "\tNumber: %d\n" +
                "\tSalary: %.2f\n" +
                "\tTeam: %s",
                this.getFirstName(), this.getLastName(),
                this.getNumber(),
                this.getSalary(),
                this.getTeam().getName());
    }











}
