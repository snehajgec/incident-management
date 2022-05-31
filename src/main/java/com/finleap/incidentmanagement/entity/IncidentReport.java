package com.finleap.incidentmanagement.entity;

import lombok.Data;
import lombok.Singular;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "incident")
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_id")
    private Integer incidentId;

    private String title;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Singular
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "incident_id")
    private Set<IncidentUser> incidentUsers;

    private String description;

    private String comments;

    public Set<IncidentUser> setIncidentUsers(Set<IncidentUser> incidentUsers) {
        if(this.incidentUsers == null || this.incidentUsers.isEmpty()) {
            this.incidentUsers = new HashSet<>();
        }
        this.incidentUsers.addAll(incidentUsers);
        return this.incidentUsers;
    }
}
