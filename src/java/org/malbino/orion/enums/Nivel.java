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
    PRIMER_SEMESTRE("PRIMER SEMESTRE", "1S", 1, Regimen.SEMESTRAL, "PRIMERO"),
    SEGUNDO_SEMESTRE("SEGUNDO SEMESTRE", "2S", 2, Regimen.SEMESTRAL, "SEGUNDO"),
    TERCER_SEMESTRE("TERCER SEMESTRE", "3S", 3, Regimen.SEMESTRAL, "TERCERO"),
    CUARTO_SEMESTRE("CUARTO SEMESTRE", "4S", 4, Regimen.SEMESTRAL, "CUARTO"),
    QUITO_SEMESTRE("QUINTO SEMESTRE", "5S", 5, Regimen.SEMESTRAL, "QUINTO"),
    SEXTO_SEMESTRE("SEXTO SEMESTRE", "6S", 6, Regimen.SEMESTRAL, "SEXTO"),
    PRIMER_AÑO("PRIMER AÑO", "1A", 1, Regimen.ANUAL, "PRIMERO"),
    SEGUNDO_AÑO("SEGUNDO AÑO", "2A", 2, Regimen.ANUAL, "SEGUNDO"),
    TERCER_AÑO("TERCER AÑO", "3A", 3, Regimen.ANUAL, "TERCERO");

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
