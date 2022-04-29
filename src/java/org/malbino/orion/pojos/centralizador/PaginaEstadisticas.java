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
public class PaginaEstadisticas extends PaginaCentralizador{
    
    private int cantidadInscritos;
    private int porcentajeInscritos;
    private int cantidadAprobados;
    private int porcentajeAprobados;
    private int cantidadReprobados;
    private int porcentajeReprobados;
    private int cantidadAbandonos;
    private int porcentajeAbandonos;

    public PaginaEstadisticas(int cantidadInscritos, int porcentajeInscritos, int cantidadAprobados, int porcentajeAprobados, int cantidadReprobados, int porcentajeReprobados, int cantidadAbandonos, int porcentajeAbandonos) {
        this.cantidadInscritos = cantidadInscritos;
        this.porcentajeInscritos = porcentajeInscritos;
        this.cantidadAprobados = cantidadAprobados;
        this.porcentajeAprobados = porcentajeAprobados;
        this.cantidadReprobados = cantidadReprobados;
        this.porcentajeReprobados = porcentajeReprobados;
        this.cantidadAbandonos = cantidadAbandonos;
        this.porcentajeAbandonos = porcentajeAbandonos;
    }

    /**
     * @return the cantidadInscritos
     */
    public int getCantidadInscritos() {
        return cantidadInscritos;
    }

    /**
     * @param cantidadInscritos the cantidadInscritos to set
     */
    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
    }

    /**
     * @return the porcentajeInscritos
     */
    public int getPorcentajeInscritos() {
        return porcentajeInscritos;
    }

    /**
     * @param porcentajeInscritos the porcentajeInscritos to set
     */
    public void setPorcentajeInscritos(int porcentajeInscritos) {
        this.porcentajeInscritos = porcentajeInscritos;
    }

    /**
     * @return the cantidadAprobados
     */
    public int getCantidadAprobados() {
        return cantidadAprobados;
    }

    /**
     * @param cantidadAprobados the cantidadAprobados to set
     */
    public void setCantidadAprobados(int cantidadAprobados) {
        this.cantidadAprobados = cantidadAprobados;
    }

    /**
     * @return the porcentajeAprobados
     */
    public int getPorcentajeAprobados() {
        return porcentajeAprobados;
    }

    /**
     * @param porcentajeAprobados the porcentajeAprobados to set
     */
    public void setPorcentajeAprobados(int porcentajeAprobados) {
        this.porcentajeAprobados = porcentajeAprobados;
    }

    /**
     * @return the cantidadReprobados
     */
    public int getCantidadReprobados() {
        return cantidadReprobados;
    }

    /**
     * @param cantidadReprobados the cantidadReprobados to set
     */
    public void setCantidadReprobados(int cantidadReprobados) {
        this.cantidadReprobados = cantidadReprobados;
    }

    /**
     * @return the porcentajeReprobados
     */
    public int getPorcentajeReprobados() {
        return porcentajeReprobados;
    }

    /**
     * @param porcentajeReprobados the porcentajeReprobados to set
     */
    public void setPorcentajeReprobados(int porcentajeReprobados) {
        this.porcentajeReprobados = porcentajeReprobados;
    }

    /**
     * @return the cantidadAbandonos
     */
    public int getCantidadAbandonos() {
        return cantidadAbandonos;
    }

    /**
     * @param cantidadAbandonos the cantidadAbandonos to set
     */
    public void setCantidadAbandonos(int cantidadAbandonos) {
        this.cantidadAbandonos = cantidadAbandonos;
    }

    /**
     * @return the porcentajeAbandonos
     */
    public int getPorcentajeAbandonos() {
        return porcentajeAbandonos;
    }

    /**
     * @param porcentajeAbandonos the porcentajeAbandonos to set
     */
    public void setPorcentajeAbandonos(int porcentajeAbandonos) {
        this.porcentajeAbandonos = porcentajeAbandonos;
    }
    
    
}
