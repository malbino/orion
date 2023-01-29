/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author Tincho
 */
@Entity
@Table(name = "cursa")
public class CarreraEstudiante implements Serializable {

    @EmbeddedId
    private CarreraEstudianteId carreraEstudianteId;

    @JoinColumn(name = "id_mencion")
    @ManyToOne
    private Mencion mencion;

    private Nivel nivelInicio;

    @Transient
    private Carrera carrera;

    public CarreraEstudiante() {
    }

    public CarreraEstudiante(CarreraEstudianteId carreraEstudianteId, Mencion mencion) {
        this.carreraEstudianteId = carreraEstudianteId;
        this.mencion = mencion;
    }

    /**
     * @return the carreraEstudianteId
     */
    public CarreraEstudianteId getCarreraEstudianteId() {
        return carreraEstudianteId;
    }

    /**
     * @param carreraEstudianteId the carreraEstudianteId to set
     */
    public void setCarreraEstudianteId(CarreraEstudianteId carreraEstudianteId) {
        this.carreraEstudianteId = carreraEstudianteId;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.carreraEstudianteId);
        hash = 89 * hash + Objects.hashCode(this.mencion);
        hash = 89 * hash + Objects.hashCode(this.nivelInicio);
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
        final CarreraEstudiante other = (CarreraEstudiante) obj;
        if (!Objects.equals(this.carreraEstudianteId, other.carreraEstudianteId)) {
            return false;
        }
        if (!Objects.equals(this.mencion, other.mencion)) {
            return false;
        }
        if (this.nivelInicio != other.nivelInicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = carrera.toString();
        if (mencion != null) {
            s += " [" + mencion.getNombre() + "]";
        }
        if (nivelInicio != null) {
            s += " -> " + nivelInicio.getAbreviatura();
        }
        return s;
    }

    @Embeddable
    public static class CarreraEstudianteId implements Serializable {

        private Integer id_carrera;
        private Integer id_persona;

        public CarreraEstudianteId() {
        }

        public CarreraEstudianteId(Integer id_carrera, Integer id_estudiante) {
            this.id_carrera = id_carrera;
            this.id_persona = id_estudiante;
        }

        /**
         * @return the id_carrera
         */
        public Integer getId_carrera() {
            return id_carrera;
        }

        /**
         * @param id_carrera the id_carrera to set
         */
        public void setId_carrera(Integer id_carrera) {
            this.id_carrera = id_carrera;
        }

        /**
         * @return the id_persona
         */
        public Integer getId_persona() {
            return id_persona;
        }

        /**
         * @param id_persona the id_persona to set
         */
        public void setId_persona(Integer id_persona) {
            this.id_persona = id_persona;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + Objects.hashCode(this.id_carrera);
            hash = 11 * hash + Objects.hashCode(this.id_persona);
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
            final CarreraEstudianteId other = (CarreraEstudianteId) obj;
            if (!Objects.equals(this.id_carrera, other.id_carrera)) {
                return false;
            }
            if (!Objects.equals(this.id_persona, other.id_persona)) {
                return false;
            }
            return true;
        }

    }

    /**
     * @return the nivelInicio
     */
    public Nivel getNivelInicio() {
        return nivelInicio;
    }

    /**
     * @param nivelInicio the nivelInicio to set
     */
    public void setNivelInicio(Nivel nivelInicio) {
        this.nivelInicio = nivelInicio;
    }
}
