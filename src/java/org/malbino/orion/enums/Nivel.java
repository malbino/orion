/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.enums;

import java.util.Arrays;

/**
 *
 * @author Martin
 */
public enum Nivel {
    PRIMER_SEMESTRE("Primer Semestre", "1S", 1, Regimen.SEMESTRAL, "Primero"),
    SEGUNDO_SEMESTRE("Segundo Semestre", "2S", 2, Regimen.SEMESTRAL, "Segundo"),
    TERCER_SEMESTRE("Tercer Semestre", "3S", 3, Regimen.SEMESTRAL, "Tercero"),
    CUARTO_SEMESTRE("Cuarto Semestre", "4S", 4, Regimen.SEMESTRAL, "Cuarto"),
    QUITO_SEMESTRE("Quinto Semestre", "5S", 5, Regimen.SEMESTRAL, "Quinto"),
    SEXTO_SEMESTRE("Sexto Semestre", "6S", 6, Regimen.SEMESTRAL, "Sexto"),
    PRIMER_AÑO("Primer Año", "1A", 1, Regimen.ANUAL, "Primero"),
    SEGUNDO_AÑO("Segundo Año", "2A", 2, Regimen.ANUAL, "Segundo"),
    TERCER_AÑO("Tercer Año", "3A", 3, Regimen.ANUAL, "Tercero");

    private String nombre;
    private String abreviatura;
    private Integer nivel;
    private Regimen regimen;
    private String ordinal;

    private Nivel(String nombre, String abreviatura, Integer nivel, Regimen regimen, String ordinal) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.nivel = nivel;
        this.regimen = regimen;
        this.ordinal = ordinal;
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

    /**
     * @return the ordinal
     */
    public String getOrdinal() {
        return ordinal;
    }

    /**
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static Nivel[] values(Regimen regimen) {
        return Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(regimen)).toArray(Nivel[]::new);
    }
}
