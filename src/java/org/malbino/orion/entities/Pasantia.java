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
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "pasantia", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "id_carrera", "id_mencion"}))
public class Pasantia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pasantia;

    private String codigo;
    private String nombre;
    private Nivel nivel;
    private Integer horas;
    private Integer creditajePasantia;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    @JoinColumn(name = "id_mencion")
    @ManyToOne
    private Mencion mencion;

    public Pasantia() {
    }

    /**
     * @return the id_pasantia
     */
    public Integer getId_pasantia() {
        return id_pasantia;
    }

    /**
     * @param id_pasantia the id_pasantia to set
     */
    public void setId_pasantia(Integer id_pasantia) {
        this.id_pasantia = id_pasantia;
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
     * @return the nivel
     */
    public Nivel getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the horas
     */
    public Integer getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    /**
     * @return the creditajePasantia
     */
    public Integer getCreditajePasantia() {
        return creditajePasantia;
    }

    /**
     * @param creditajePasantia the creditajePasantia to set
     */
    public void setCreditajePasantia(Integer creditajePasantia) {
        this.creditajePasantia = creditajePasantia;
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
        hash = 97 * hash + Objects.hashCode(this.id_pasantia);
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
        final Pasantia other = (Pasantia) obj;
        if (!Objects.equals(this.id_pasantia, other.id_pasantia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
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
}
