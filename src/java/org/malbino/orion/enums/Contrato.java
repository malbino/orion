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
public enum Contrato {
    TIEMPO_COMPLETO("TIEMPO COMPLETO"),
    MEDIO_TIEMPO("MEDIO TIEMPO"),
    CONTRATISTA("CONTRATISTA"),
    PASANTÍA("PASANTÍA"),
    TEMPORAL("TEMPORAL");

    private String nombre;

    private Contrato(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
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

}
