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
    SEMESTRAL("Semestral", "S", 5, 2),
    ANUAL("Anual", "A", 10, 3);

    private String nombre;
    private String inicial;
    private Integer cuotas;
    private Integer cantidadMaximaReprobaciones;

    private Regimen(String nombre, String inicial, Integer cuotas, Integer cantidadMaximaReprobaciones) {
        this.nombre = nombre;
        this.inicial = inicial;
        this.cuotas = cuotas;
        this.cantidadMaximaReprobaciones = cantidadMaximaReprobaciones;
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

    /**
     * @return the cantidadMaximaReprobaciones
     */
    public Integer getCantidadMaximaReprobaciones() {
        return cantidadMaximaReprobaciones;
    }

    /**
     * @param cantidadMaximaReprobaciones the cantidadMaximaReprobaciones to set
     */
    public void setCantidadMaximaReprobaciones(Integer cantidadMaximaReprobaciones) {
        this.cantidadMaximaReprobaciones = cantidadMaximaReprobaciones;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
