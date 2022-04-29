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
public enum Turno {
    MAÑANA("MAÑANA", "M", 1),
    TARDE("TARDE", "T", 2),
    NOCHE("NOCHE", "N", 3);

    private String nombre;
    private String inicial;
    private Integer numero;

    private Turno(String nombre, String inicial, Integer numero) {
        this.nombre = nombre;
        this.inicial = inicial;
        this.numero = numero;
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
     * @return the numero
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
