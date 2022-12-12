package hiberspring.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String position;

    //•	Card – an EmployeeCard, could be any EmployeeCard. Must be UNIQUE though.
    //•	Branch – a Branch, could be any Branch.

    @OneToOne(optional = false)
    private EmployeeCard card;

    @ManyToOne(optional = false)
    private Branch branch;

    public Employee() {}

    public EmployeeCard getCard() {
        return card;
    }

    public void setCard(EmployeeCard card) {
        this.card = card;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n" +
                "Position: %s\n" +
                "Card Number: %s\n",
                this.getFirstName(), this.getLastName(),
                this.getPosition(), this.getCard().getNumber());
    }
}
