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
    SEMESTRAL("SEMESTRAL", "S", 5, 2, 3, 61, 40),
    ANUAL("ANUAL", "A", 10, 3, 4, 61, 40);

    private String nombre;
    private String inicial;
    private Integer cuotas;
    private Integer cantidadMaximaReprobaciones;
    private Integer cantidadParciales;
    private Integer notaMinimaAprobacion;
    private Integer notaMinimmaPruebaRecuperacion;

    private Regimen(String nombre, String inicial, Integer cuotas, Integer cantidadMaximaReprobaciones, Integer cantidadParciales, Integer notaMinimaAprobacion, Integer notaMinimmaPruebaRecuperacion) {
        this.nombre = nombre;
        this.inicial = inicial;
        this.cuotas = cuotas;
        this.cantidadMaximaReprobaciones = cantidadMaximaReprobaciones;
        this.cantidadParciales = cantidadParciales;
        this.notaMinimaAprobacion = notaMinimaAprobacion;
        this.notaMinimmaPruebaRecuperacion = notaMinimmaPruebaRecuperacion;
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

    /**
     * @return the cantidadParciales
     */
    public Integer getCantidadParciales() {
        return cantidadParciales;
    }

    /**
     * @param cantidadParciales the cantidadParciales to set
     */
    public void setCantidadParciales(Integer cantidadParciales) {
        this.cantidadParciales = cantidadParciales;
    }

    /**
     * @return the notaMinimaAprobacion
     */
    public Integer getNotaMinimaAprobacion() {
        return notaMinimaAprobacion;
    }

    /**
     * @param notaMinimaAprobacion the notaMinimaAprobacion to set
     */
    public void setNotaMinimaAprobacion(Integer notaMinimaAprobacion) {
        this.notaMinimaAprobacion = notaMinimaAprobacion;
    }

    /**
     * @return the notaMinimmaPruebaRecuperacion
     */
    public Integer getNotaMinimmaPruebaRecuperacion() {
        return notaMinimmaPruebaRecuperacion;
    }

    /**
     * @param notaMinimmaPruebaRecuperacion the notaMinimmaPruebaRecuperacion to
     * set
     */
    public void setNotaMinimmaPruebaRecuperacion(Integer notaMinimmaPruebaRecuperacion) {
        this.notaMinimmaPruebaRecuperacion = notaMinimmaPruebaRecuperacion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
