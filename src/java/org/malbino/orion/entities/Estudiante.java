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
@Table(name = "estudiante", catalog = "orion", schema = "public")

@PrimaryKeyJoinColumn(name = "id_persona")
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario implements Serializable {

    @Column(unique = true)
    private String codigo;
    private Boolean tituloBachiller;

    public Estudiante() {
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
     * @return the tituloBachiller
     */
    public Boolean getTituloBachiller() {
        return tituloBachiller;
    }

    /**
     * @param tituloBachiller the tituloBachiller to set
     */
    public void setTituloBachiller(Boolean tituloBachiller) {
        this.tituloBachiller = tituloBachiller;
    }
}
