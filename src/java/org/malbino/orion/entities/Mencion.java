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
@Table(name = "mencion", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "id_carrera"}))
public class Mencion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_mencion;

    private String codigo;
    private String nombre;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    public Mencion() {
    }

    /**
     * @return the id_mencion
     */
    public Integer getId_mencion() {
        return id_mencion;
    }

    /**
     * @param id_mencion the id_mencion to set
     */
    public void setId_mencion(Integer id_mencion) {
        this.id_mencion = id_mencion;
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
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id_mencion);
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
        final Mencion other = (Mencion) obj;
        if (!Objects.equals(this.id_mencion, other.id_mencion)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
    }

}
