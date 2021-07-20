/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.pojos.centralizador;

/**
 *
 * @author Tincho
 */
public class EstudianteCentralizador {

    private String numero;
    private String nombre;
    private String ci;
    private String[] notas;
    private String observaciones;
    
    private int cantidadMaximaMaterias;

    public EstudianteCentralizador(String numero, String nombre, String ci, String observaciones, int cantidadMaximaMaterias) {
        this.numero = numero;
        this.nombre = nombre;
        this.ci = ci;
        this.notas = notas = new String[cantidadMaximaMaterias];
        this.observaciones = observaciones;
    }

    public EstudianteCentralizador(String numero, String nombre, String ci, int cantidadMaximaMaterias) {
        this.numero = numero;
        this.nombre = nombre;
        this.ci = ci;
        this.notas = notas = new String[cantidadMaximaMaterias];
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
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
     * @return the ci
     */
    public String getCi() {
        return ci;
    }

    /**
     * @param ci the ci to set
     */
    public void setCi(String ci) {
        this.ci = ci;
    }

    /**
     * @return the notas
     */
    public String[] getNotas() {
        return notas;
    }

    /**
     * @param notas the notas to set
     */
    public void setNotas(String[] notas) {
        this.notas = notas;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @return the cantidadMaximaMaterias
     */
    public int getCantidadMaximaMaterias() {
        return cantidadMaximaMaterias;
    }

    /**
     * @param cantidadMaximaMaterias the cantidadMaximaMaterias to set
     */
    public void setCantidadMaximaMaterias(int cantidadMaximaMaterias) {
        this.cantidadMaximaMaterias = cantidadMaximaMaterias;
    }
}
