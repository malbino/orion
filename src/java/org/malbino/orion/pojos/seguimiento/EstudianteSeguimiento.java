/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.pojos.seguimiento;

/**
 *
 * @author Tincho
 */
public class EstudianteSeguimiento {

    private Integer numero;
    private String estudiante;
    private NotaSeguimiento[] notasSeguimiento;
    private String observaciones;

    public EstudianteSeguimiento() {
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

    /**
     * @return the estudiante
     */
    public String getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * @return the notasSeguimiento
     */
    public NotaSeguimiento[] getNotasSeguimiento() {
        return notasSeguimiento;
    }

    /**
     * @param notasSeguimiento the notasSeguimiento to set
     */
    public void setNotasSeguimiento(NotaSeguimiento[] notasSeguimiento) {
        this.notasSeguimiento = notasSeguimiento;
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
    
    
}
