package plumbeer.app.development.domain;

import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Trabajo.
 */
@Entity
@Table(name = "trabajo")
public class Trabajo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "asunto")
    private String asunto;
    
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "fecha")
    private LocalDate fecha;
    
    @Column(name = "visible")
    private Boolean visible;
    
    @Column(name = "abierto")
    private Boolean abierto;
    
    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private User tecnico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getVisible() {
        return visible;
    }
    
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getAbierto() {
        return abierto;
    }
    
    public void setAbierto(Boolean abierto) {
        this.abierto = abierto;
    }

    public User getTecnico() {
        return tecnico;
    }

    public void setTecnico(User user) {
        this.tecnico = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trabajo trabajo = (Trabajo) o;
        if(trabajo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, trabajo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Trabajo{" +
            "id=" + id +
            ", asunto='" + asunto + "'" +
            ", descripcion='" + descripcion + "'" +
            ", fecha='" + fecha + "'" +
            ", visible='" + visible + "'" +
            ", abierto='" + abierto + "'" +
            '}';
    }
}
