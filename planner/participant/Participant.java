package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "particpants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    public Object get;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Participant(String email, Trip trip) {
        this.email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }

    public void setIdConfirmed() {
    }

    public void setName() {
    }

    public boolean getId() {
        return false;
    }

    public Object getName() {
        return null;
    }

    public Object getEmail() {
        return null;
    }

    public Object getisConfirmed() {
        return null;
    }

    public Object setEmail() {
        return null;
    }
}
