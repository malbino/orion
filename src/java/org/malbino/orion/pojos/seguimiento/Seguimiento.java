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
public class Seguimiento {
    
    private String instituto;
    private String gestion;
    private String carrera;
    private String nivel;
    private String turno;
    private String paralelo;

    private String[] materiasSeguimiento;
    private EstudianteSeguimiento[] estudiantesSeguimiento;

    public Seguimiento() {
    } 

    /**
     * @return the instituto
     */
    public String getInstituto() {
        return instituto;
    }

    /**
     * @param instituto the instituto to set
     */
    public void setInstituto(String instituto) {
        this.instituto = instituto;
    }

    /**
     * @return the gestion
     */
    public String getGestion() {
        return gestion;
    }

    /**
     * @param gestion the gestion to set
     */
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }

    /**
     * @return the carrera
     */
    public String getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    /**
     * @return the nivel
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the turno
     */
    public String getTurno() {
        return turno;
    }

    /**
     * @param turno the turno to set
     */
    public void setTurno(String turno) {
        this.turno = turno;
    }

    /**
     * @return the paralelo
     */
    public String getParalelo() {
        return paralelo;
    }

    /**
     * @param paralelo the paralelo to set
     */
    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }

    /**
     * @return the materiasSeguimiento
     */
    public String[] getMateriasSeguimiento() {
        return materiasSeguimiento;
    }

    /**
     * @param materiasSeguimiento the materiasSeguimiento to set
     */
    public void setMateriasSeguimiento(String[] materiasSeguimiento) {
        this.materiasSeguimiento = materiasSeguimiento;
    }

    /**
     * @return the estudiantesSeguimiento
     */
    public EstudianteSeguimiento[] getEstudiantesSeguimiento() {
        return estudiantesSeguimiento;
    }

    /**
     * @param estudiantesSeguimiento the estudiantesSeguimiento to set
     */
    public void setEstudiantesSeguimiento(EstudianteSeguimiento[] estudiantesSeguimiento) {
        this.estudiantesSeguimiento = estudiantesSeguimiento;
    }

}
