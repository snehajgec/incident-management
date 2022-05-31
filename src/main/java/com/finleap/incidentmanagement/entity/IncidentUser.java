package com.finleap.incidentmanagement.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "incident_user")
public class IncidentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_user_id")
    private Integer incidentUserId;

    @ManyToOne
//    @MapsId("incidentId")
    @JoinColumn(name = "incident_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private IncidentReport incident;

    @ManyToOne
//    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public void setIncident(IncidentReport incidentReport) {

    }
}
