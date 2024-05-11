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

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "egresado", uniqueConstraints = @UniqueConstraint(columnNames = {"id_persona", "id_carrera", "id_mencion", "id_gestionacademica"}))
public class Egresado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_egresado;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    @JoinColumn(name = "id_mencion")
    @ManyToOne
    private Mencion mencion;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    public Egresado() {
    }

    public Egresado(Estudiante estudiante, Carrera carrera, Mencion mencion, GestionAcademica gestionAcademica) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.mencion = mencion;
        this.gestionAcademica = gestionAcademica;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id_egresado);
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
        final Egresado other = (Egresado) obj;
        return Objects.equals(this.id_egresado, other.id_egresado);
    }

    /**
     * @return the id_egresado
     */
    public Integer getId_egresado() {
        return id_egresado;
    }

    /**
     * @param id_egresado the id_egresado to set
     */
    public void setId_egresado(Integer id_egresado) {
        this.id_egresado = id_egresado;
    }

    /**
     * @return the estudiante
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * @return the carrera
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    /**
     * @return the mencion
     */
    public Mencion getMencion() {
        return mencion;
    }

    /**
     * @param mencion the mencion to set
     */
    public void setMencion(Mencion mencion) {
        this.mencion = mencion;
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

}
