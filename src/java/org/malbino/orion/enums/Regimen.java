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
    SEMESTRAL("SEMESTRAL", "S", 5),
    ANUAL("ANUAL", "A", 10);

    private String nombre;
    private String inicial;
    private Integer cuotas;

    private Regimen(String nombre, String inicial, Integer cuotas) {
        this.nombre = nombre;
        this.inicial = inicial;
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
     * @return the inicial
     */
    public String getInicial() {
        return inicial;
    }

    /**
     * @param inicial the inicial to set
     */
    public void setInicial(String inicial) {
        this.inicial = inicial;
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
