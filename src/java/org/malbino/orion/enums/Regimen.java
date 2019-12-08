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
public enum Regimen {
    SEMESTRAL("Semestral", 5),
    ANUAL("Anual", 10);

    private String nombre;
    private Integer cuotas;

    private Regimen(String nombre, Integer cuotas) {
        this.nombre = nombre;
        this.cuotas = cuotas;
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
     * @return the cuotas
     */
    public Integer getCuotas() {
        return cuotas;
    }

    /**
     * @param cuotas the cuotas to set
     */
    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
