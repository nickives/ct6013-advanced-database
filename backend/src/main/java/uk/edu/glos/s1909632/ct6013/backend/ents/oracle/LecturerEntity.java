package uk.edu.glos.s1909632.ct6013.backend.ents.oracle;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "LECTURER")
public class LecturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "lecturer")
    private Set<ModuleEntity> modules = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ModuleEntity> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleEntity> modules) {
        this.modules = modules;
    }

}