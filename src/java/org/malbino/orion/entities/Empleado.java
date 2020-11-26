/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "empleado")

@PrimaryKeyJoinColumn(name = "id_persona")
@DiscriminatorValue("Empleado")
public class Empleado extends Usuario implements Serializable {

    private String codigo;
    private String abreviaturaProfesion;

    public Empleado() {
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
     * @return the abreviaturaProfesion
     */
    public String getAbreviaturaProfesion() {
        return abreviaturaProfesion;
    }

    /**
     * @param abreviaturaProfesion the abreviaturaProfesion to set
     */
    public void setAbreviaturaProfesion(String abreviaturaProfesion) {
        this.abreviaturaProfesion = abreviaturaProfesion;
    }

}
