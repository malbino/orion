/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.enums;

/**
 *
 * @author Martin
 */
public enum Departamento {
    BENI("BENI"),
    CHUQUISACA("CHUQUISACA"),
    COCHABAMBA("COCHABAMBA"),
    LA_PAZ("LA PAZ"),
    ORURO("ORURO"),
    PANDO("PANDO"),
    POTOSI("POTOSI"),
    SANTA_CRUZ("SANTA CRUZ"),
    TARIJA("TARIJA");

    private String nombre;

    private Departamento(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre;
    }
}
