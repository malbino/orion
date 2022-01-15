/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "comprobante")
public class Comprobante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_comprobante;

    @Column(unique = true)
    private Integer codigo;
    private String deposito;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private Boolean valido;

    @JoinColumn(name = "id_inscrito")
    @ManyToOne
    private Inscrito inscrito;

    @JoinColumn(name = "id_postulante")
    @ManyToOne
    private Postulante postulante;

    @JoinColumn(name = "id_usuario")
    @ManyToOne
    private Usuario usuario;

    public Comprobante() {
    }

    /**
     * @return the id_comprobante
     */
    public Integer getId_comprobante() {
        return id_comprobante;
    }

    /**
     * @param id_comprobante the id_comprobante to set
     */
    public void setId_comprobante(Integer id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the valido
     */
    public Boolean getValido() {
        return valido;
    }

    /**
     * @param valido the valido to set
     */
    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    /**
     * @return the inscrito
     */
    public Inscrito getInscrito() {
        return inscrito;
    }

    /**
     * @param inscrito the inscrito to set
     */
    public void setInscrito(Inscrito inscrito) {
        this.inscrito = inscrito;
    }

    /**
     * @return the deposito
     */
    public String getDeposito() {
        return deposito;
    }

    /**
     * @param deposito the deposito to set
     */
    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    /**
     * @return the postulante
     */
    public Postulante getPostulante() {
        return postulante;
    }

    /**
     * @param postulante the postulante to set
     */
    public void setPostulante(Postulante postulante) {
        this.postulante = postulante;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id_comprobante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comprobante other = (Comprobante) obj;
        if (!Objects.equals(this.id_comprobante, other.id_comprobante)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo + " [" + fecha_ddMMyyyy() + "]";
    }

    public String fecha_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fecha);
    }

    public String validoToString() {
        return valido ? "Sí" : "No";
    }

    public String estudiantePostulante() {
        String s = "";
        if (inscrito != null) {
            s = inscrito.getEstudiante().toString();
        }
        if (postulante != null) {
            s = postulante.toString();
        }
        return s;
    }

    public String carrera() {
        String s = "";
        if (inscrito != null) {
            s = inscrito.getCarrera().toString();
        }
        if (postulante != null) {
            s = postulante.getCarrera().toString();
        }
        return s;
    }

    public String gestionAcademica() {
        String s = "";
        if (inscrito != null) {
            s = inscrito.getGestionAcademica().toString();
        }
        if (postulante != null) {
            s = postulante.getGestionAcademica().toString();
        }
        return s;
    }
}
