package plumbeer.app.development.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Planificacion.
 */
@Entity
@Table(name = "planificacion")
public class Planificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha")
    private ZonedDateTime fecha;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "planificacion")
    @JsonIgnore
    private Set<Trabajo> trabajos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Trabajo> getTrabajos() {
        return trabajos;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Planificacion planificacion = (Planificacion) o;
        if(planificacion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, planificacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Planificacion{" +
            "id=" + id +
            ", fecha='" + fecha + "'" +
            '}';
    }
}
