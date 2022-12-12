package softuni.exam.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(optional = false)
    private Mechanic mechanic;

    @ManyToOne(optional = false)
    private Part part;

    @ManyToOne(optional = false)
    private Car car;

    public Task() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return String.format("Car %s %s with %dkm\n" +
                "-Mechanic: %s %s - task №%d:¬¬\n" +
                "--Engine: %.2f\n" +
                "---Price: %.2f$",
                this.getCar().getCarMake(), this.getCar().getCarModel(), this.getCar().getKilometers(),
                this.getMechanic().getFirstName(), this.getMechanic().getLastName(), this.getId(),
                this.getCar().getEngine(),
                this.getPrice());
    }
}
