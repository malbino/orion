/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.pojos.centralizador;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tincho
 */
public class Centralizador {

    private String ubicacion;
    private String institucion;
    private String resolucionMinisterial;
    private String caracter;

    private List<PaginaCentralizador> paginasCentralizador;

    public Centralizador(String ubicacion, String institucion, String resolucionMinisterial, String caracter) {
        this.ubicacion = ubicacion;
        this.institucion = institucion;
        this.resolucionMinisterial = resolucionMinisterial;
        this.caracter = caracter;

        this.paginasCentralizador = new ArrayList();
    }

    /**
     * @return the ubicacion
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * @param ubicacion the ubicacion to set
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * @return the institucion
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * @param institucion the institucion to set
     */
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    /**
     * @return the resolucionMinisterial
     */
    public String getResolucionMinisterial() {
        return resolucionMinisterial;
    }

    /**
     * @param resolucionMinisterial the resolucionMinisterial to set
     */
    public void setResolucionMinisterial(String resolucionMinisterial) {
        this.resolucionMinisterial = resolucionMinisterial;
    }

    /**
     * @return the caracter
     */
    public String getCaracter() {
        return caracter;
    }

    /**
     * @param caracter the caracter to set
     */
    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    /**
     * @return the paginasCentralizador
     */
    public List<PaginaCentralizador> getPaginasCentralizador() {
        return paginasCentralizador;
    }

    /**
     * @param paginasCentralizador the paginasCentralizador to set
     */
    public void setPaginasCentralizador(List<PaginaCentralizador> paginasCentralizador) {
        this.paginasCentralizador = paginasCentralizador;
    }
}
