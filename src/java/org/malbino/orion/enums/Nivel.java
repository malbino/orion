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
public enum Nivel {
    PRIMER_SEMESTRE("Primer Semestre", "1S", 1, Regimen.SEMESTRAL),
    SEGUNDO_SEMESTRE("Segundo Semestre", "2S", 2, Regimen.SEMESTRAL),
    TERCER_SEMESTRE("Tercer Semestre", "3S", 3, Regimen.SEMESTRAL),
    CUARTO_SEMESTRE("Cuarto Semestre", "4S", 4, Regimen.SEMESTRAL),
    QUITO_SEMESTRE("Quinto Semestre", "5S", 5, Regimen.SEMESTRAL),
    SEXTO_SEMESTRE("Sexto Semestre", "6S", 6, Regimen.SEMESTRAL),
    PRIMER_AÑO("Primer Año", "1A", 1, Regimen.ANUAL),
    SEGUNDO_AÑO("Segundo Año", "2A", 2, Regimen.ANUAL),
    TERCER_AÑO("Tercer Año", "3A", 3, Regimen.ANUAL);

    private String nombre;
    private String abreviatura;
    private Integer nivel;
    private Regimen regimen;

    private Nivel(String nombre, String abreviatura, Integer nivel, Regimen regimen) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.nivel = nivel;
        this.regimen = regimen;
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
     * @return the abreviatura
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * @param abreviatura the abreviatura to set
     */
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    /**
     * @return the nivel
     */
    public Integer getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the regimen
     */
    public Regimen getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
