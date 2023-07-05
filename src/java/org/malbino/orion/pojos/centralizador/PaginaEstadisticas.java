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
    private int cantidadNoSePresento;
    private int porcentajeNoSePresento;

    public PaginaEstadisticas(int cantidadInscritos, int porcentajeInscritos, int cantidadAprobados, int porcentajeAprobados, int cantidadReprobados, int porcentajeReprobados, int cantidadNoSePresento, int porcentajeNoSePresento) {
        this.cantidadInscritos = cantidadInscritos;
        this.porcentajeInscritos = porcentajeInscritos;
        this.cantidadAprobados = cantidadAprobados;
        this.porcentajeAprobados = porcentajeAprobados;
        this.cantidadReprobados = cantidadReprobados;
        this.porcentajeReprobados = porcentajeReprobados;
        this.cantidadNoSePresento = cantidadNoSePresento;
        this.porcentajeNoSePresento = porcentajeNoSePresento;
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
     * @return the cantidadNoSePresento
     */
    public int getCantidadNoSePresento() {
        return cantidadNoSePresento;
    }

    /**
     * @param cantidadNoSePresento the cantidadNoSePresento to set
     */
    public void setCantidadNoSePresento(int cantidadNoSePresento) {
        this.cantidadNoSePresento = cantidadNoSePresento;
    }

    /**
     * @return the porcentajeNoSePresento
     */
    public int getPorcentajeNoSePresento() {
        return porcentajeNoSePresento;
    }

    /**
     * @param porcentajeNoSePresento the porcentajeNoSePresento to set
     */
    public void setPorcentajeNoSePresento(int porcentajeNoSePresento) {
        this.porcentajeNoSePresento = porcentajeNoSePresento;
    }
    
    
}
