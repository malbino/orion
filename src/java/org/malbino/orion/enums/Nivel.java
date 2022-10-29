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
    PRIMER_SEMESTRE(0, "PRIMER SEMESTRE", "1S", 1, Regimen.SEMESTRAL, "PRIMERO"),
    SEGUNDO_SEMESTRE(1, "SEGUNDO SEMESTRE", "2S", 2, Regimen.SEMESTRAL, "SEGUNDO"),
    TERCER_SEMESTRE(2, "TERCER SEMESTRE", "3S", 3, Regimen.SEMESTRAL, "TERCERO"),
    CUARTO_SEMESTRE(3, "CUARTO SEMESTRE", "4S", 4, Regimen.SEMESTRAL, "CUARTO"),
    QUITO_SEMESTRE(4, "QUINTO SEMESTRE", "5S", 5, Regimen.SEMESTRAL, "QUINTO"),
    SEXTO_SEMESTRE(5, "SEXTO SEMESTRE", "6S", 6, Regimen.SEMESTRAL, "SEXTO"),
    PRIMER_AÑO(6, "PRIMER AÑO", "1A", 1, Regimen.ANUAL, "PRIMERO"),
    SEGUNDO_AÑO(7, "SEGUNDO AÑO", "2A", 2, Regimen.ANUAL, "SEGUNDO"),
    TERCER_AÑO(8, "TERCER AÑO", "3A", 3, Regimen.ANUAL, "TERCERO");

    private Integer id;
    private String nombre;
    private String abreviatura;
    private Integer nivel;
    private Regimen regimen;
    private String ordinal;

    private Nivel(Integer id, String nombre, String abreviatura, Integer nivel, Regimen regimen, String ordinal) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.nivel = nivel;
        this.regimen = regimen;
        this.ordinal = ordinal;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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

    public static Nivel getById(Integer id) {
        for (Nivel n : values()) {
            if (n.id.equals(id)) {
                return n;
            }
        }
        return null;
    }
}
