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
public enum Concepto {
    MATRICULA("MATRICULA", "MA", 0, null),
    CUOTA1_SEMESTRAL("CUOTA 1", "C1", 1, Regimen.SEMESTRAL),
    CUOTA2_SEMESTRAL("CUOTA 2", "C2", 2, Regimen.SEMESTRAL),
    CUOTA3_SEMESTRAL("CUOTA 3", "C3", 3, Regimen.SEMESTRAL),
    CUOTA4_SEMESTRAL("CUOTA 4", "C4", 4, Regimen.SEMESTRAL),
    CUOTA5_SEMESTRAL("CUOTA 5", "C5", 5, Regimen.SEMESTRAL),
    CUOTA1_ANUAL("CUOTA 1", "C1", 1, Regimen.ANUAL),
    CUOTA2_ANUAL("CUOTA 2", "C2", 2, Regimen.ANUAL),
    CUOTA3_ANUAL("CUOTA 3", "C3", 3, Regimen.ANUAL),
    CUOTA4_ANUAL("CUOTA 4", "C4", 4, Regimen.ANUAL),
    CUOTA5_ANUAL("CUOTA 5", "C5", 5, Regimen.ANUAL),
    CUOTA6_ANUAL("CUOTA 6", "C6", 6, Regimen.ANUAL),
    CUOTA7_ANUAL("CUOTA 7", "C7", 7, Regimen.ANUAL),
    CUOTA8_ANUAL("CUOTA 8", "C8", 8, Regimen.ANUAL),
    CUOTA9_ANUAL("CUOTA 9", "C9", 9, Regimen.ANUAL),
    CUOTA10_ANUAL("CUOTA 10", "C10", 10, Regimen.ANUAL),
    ADMISION("ADMISIÓN", "AD", 0, null),;

    private String nombre;
    private String codigo;
    private Integer numero;
    private Regimen regimen;

    private Concepto(String nombre, String abreviatura, Integer numero, Regimen regimen) {
        this.nombre = nombre;
        this.codigo = abreviatura;
        this.numero = numero;
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
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public static Concepto[] values(Regimen regimen) {
        return Arrays.stream(Concepto.values()).filter(concepto -> concepto.getRegimen() != null && concepto.getRegimen().equals(regimen)).toArray(Concepto[]::new);
    }

    public static Concepto[] valuesSinRegimen() {
        return Arrays.stream(Concepto.values()).filter(concepto -> concepto.getRegimen() == null).toArray(Concepto[]::new);
    }
}
