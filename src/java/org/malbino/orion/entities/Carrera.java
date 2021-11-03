/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.malbino.orion.enums.NivelAcademico;
import org.malbino.orion.enums.Regimen;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "carrera")
public class Carrera implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_carrera;

    @Column(unique = true)
    private String codigo;
    private String nombre;
    private Regimen regimen;
    private NivelAcademico nivelAcademico;
    private Integer version;
    private String resolucionMinisterial1;
    private String resolucionMinisterial2;
    private String resolucionMinisterial3;
    private Integer creditajeMatricula;
    private String mencion;

    @JoinColumn(name = "id_campus")
    @ManyToOne
    private Campus campus;

    @JoinColumn(name = "id_jefecarrera")
    @ManyToOne
    private Empleado jefeCarrera;

    public Carrera() {
    }

    public String idnumberMoodle() {
        return "c" + this.id_carrera;
    }

    public String nameMoodle() {
        return nombre + " v" + version;
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the regimen
     */
    public Regimen getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    /**
     * @return the nivelAcademico
     */
    public NivelAcademico getNivelAcademico() {
        return nivelAcademico;
    }

    /**
     * @param nivelAcademico the nivelAcademico to set
     */
    public void setNivelAcademico(NivelAcademico nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the resolucionMinisterial1
     */
    public String getResolucionMinisterial1() {
        return resolucionMinisterial1;
    }

    /**
     * @param resolucionMinisterial1 the resolucionMinisterial1 to set
     */
    public void setResolucionMinisterial1(String resolucionMinisterial1) {
        this.resolucionMinisterial1 = resolucionMinisterial1;
    }

    /**
     * @return the creditajeMatricula
     */
    public Integer getCreditajeMatricula() {
        return creditajeMatricula;
    }

    /**
     * @param creditajeMatricula the creditajeMatricula to set
     */
    public void setCreditajeMatricula(Integer creditajeMatricula) {
        this.creditajeMatricula = creditajeMatricula;
    }

    /**
     * @return the campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * @param campus the campus to set
     */
    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id_carrera);
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
        final Carrera other = (Carrera) obj;
        if (!Objects.equals(this.id_carrera, other.id_carrera)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
    }

    /**
     * @return the jefeCarrera
     */
    public Empleado getJefeCarrera() {
        return jefeCarrera;
    }

    /**
     * @param jefeCarrera the jefeCarrera to set
     */
    public void setJefeCarrera(Empleado jefeCarrera) {
        this.jefeCarrera = jefeCarrera;
    }

    /**
     * @return the resolucionMinisterial2
     */
    public String getResolucionMinisterial2() {
        return resolucionMinisterial2;
    }

    /**
     * @param resolucionMinisterial2 the resolucionMinisterial2 to set
     */
    public void setResolucionMinisterial2(String resolucionMinisterial2) {
        this.resolucionMinisterial2 = resolucionMinisterial2;
    }

    /**
     * @return the resolucionMinisterial3
     */
    public String getResolucionMinisterial3() {
        return resolucionMinisterial3;
    }

    /**
     * @param resolucionMinisterial3 the resolucionMinisterial3 to set
     */
    public void setResolucionMinisterial3(String resolucionMinisterial3) {
        this.resolucionMinisterial3 = resolucionMinisterial3;
    }

    /**
     * @return the mencion
     */
    public String getMencion() {
        return mencion;
    }

    /**
     * @param mencion the mencion to set
     */
    public void setMencion(String mencion) {
        this.mencion = mencion;
    }
}
