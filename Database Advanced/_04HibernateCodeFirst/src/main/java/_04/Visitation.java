package _04;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "_04_visitations")
public class Visitation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "diagnose_id", referencedColumnName = "id")
    private Diagnose diagnose;

    @ManyToMany
    @JoinTable(name = "visitation_medicament",
    joinColumns = @JoinColumn(name = "visitation_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "medicament_id", referencedColumnName = "id")
    )
    private Set<Medicament> medicaments;

    private LocalDate date;

    private String comments;

    public Visitation() {}

    public Visitation(Patient patient, Diagnose diagnose, LocalDate date, String comments) {
        this.patient = patient;
        this.diagnose = diagnose;
        this.date = date;
        this.comments = comments;
        this.medicaments = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Diagnose getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
    }

    public Set<Medicament> getMedicaments() {
        return Collections.unmodifiableSet(medicaments);
    }

    public void addMedicament(Medicament medicament) {
        this.medicaments.add(medicament);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    //The GP should also keep history about all his visitations,
    // diagnoses and prescribed medicaments. Each visitation has date and comments.

}
