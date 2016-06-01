package plumbeer.app.development.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Mensaje.
 */
@Entity
@Table(name = "mensaje")
public class Mensaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "titulo")
    private String titulo;
    
    @Lob
    @Column(name = "cuerpo")
    private String cuerpo;
    
    @Column(name = "fecha")
    private ZonedDateTime fecha;
    
    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private User receptor;

    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private User emisor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }
    
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public User getReceptor() {
        return receptor;
    }

    public void setReceptor(User user) {
        this.receptor = user;
    }

    public User getEmisor() {
        return emisor;
    }

    public void setEmisor(User user) {
        this.emisor = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mensaje mensaje = (Mensaje) o;
        if(mensaje.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mensaje.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mensaje{" +
            "id=" + id +
            ", titulo='" + titulo + "'" +
            ", cuerpo='" + cuerpo + "'" +
            ", fecha='" + fecha + "'" +
            '}';
    }
}
