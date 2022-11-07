/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Turno;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "grupopasantia", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "turno", "id_gestionacademica", "id_pasantia"}))
public class GrupoPasantia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_grupopasantia;

    private String codigo;
    private Integer capacidad;
    private Turno turno;
    private Boolean abierto;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    @JoinColumn(name = "id_pasantia")
    @ManyToOne
    private Pasantia pasantia;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Empleado empleado;

    public GrupoPasantia() {
    }

    public GrupoPasantia(String codigo, Integer capacidad, Turno turno, Boolean abierto, GestionAcademica gestionAcademica, Pasantia pasantia) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.turno = turno;
        this.abierto = abierto;
        this.gestionAcademica = gestionAcademica;
        this.pasantia = pasantia;
    }

    /**
     * @return the id_grupopasantia
     */
    public Integer getId_grupopasantia() {
        return id_grupopasantia;
    }

    /**
     * @param id_grupopasantia the id_grupopasantia to set
     */
    public void setId_grupopasantia(Integer id_grupopasantia) {
        this.id_grupopasantia = id_grupopasantia;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the capacidad
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * @param capacidad the capacidad to set
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * @return the turno
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * @param turno the turno to set
     */
    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    /**
     * @return the abierto
     */
    public Boolean getAbierto() {
        return abierto;
    }

    /**
     * @param abierto the abierto to set
     */
    public void setAbierto(Boolean abierto) {
        this.abierto = abierto;
    }

    /**
     * @return the gestionAcademica
     */
    public GestionAcademica getGestionAcademica() {
        return gestionAcademica;
    }

    /**
     * @param gestionAcademica the gestionAcademica to set
     */
    public void setGestionAcademica(GestionAcademica gestionAcademica) {
        this.gestionAcademica = gestionAcademica;
    }

    /**
     * @return the pasantia
     */
    public Pasantia getPasantia() {
        return pasantia;
    }

    /**
     * @param pasantia the pasantia to set
     */
    public void setPasantia(Pasantia pasantia) {
        this.pasantia = pasantia;
    }

    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String abiertoToString() {
        return abierto ? "Sí" : "No";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id_grupopasantia);
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
        final GrupoPasantia other = (GrupoPasantia) obj;
        if (!Objects.equals(this.id_grupopasantia, other.id_grupopasantia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo + " [" + turno.getNombre() + "]";
    }

    public int getId_grupo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
