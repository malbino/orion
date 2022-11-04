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
public enum TipoNota {
    PRIMER_PARCIAL_SEMESTRAL("PRIMER PARCIAL", Regimen.SEMESTRAL),
    SEGUNDO_PARCIAL_SEMESTRAL("SEGUNDO PARCIAL", Regimen.SEMESTRAL),
    TERCER_PARCIAL_SEMESTRAL("TERCER PARCIAL", Regimen.SEMESTRAL),
    RECUPERATORIO_SEMESTRAL("RECUPERATORIO", Regimen.SEMESTRAL),
    PRIMER_PARCIAL_ANUAL("PRIMER PARCIAL", Regimen.ANUAL),
    SEGUNDO_PARCIAL_ANUAL("SEGUNDO PARCIAL", Regimen.ANUAL),
    TERCER_PARCIAL_ANUAL("TERCER PARCIAL", Regimen.ANUAL),
    CUARTO_PARCIAL_ANUAL("CUARTO PARCIAL", Regimen.ANUAL),
    RECUPERATORIO_ANUAL("RECUPERATORIO", Regimen.ANUAL);

    private String nombre;
    private Regimen regimen;

    private TipoNota(String nombre, Regimen regimen) {
        this.nombre = nombre;
        this.regimen = regimen;
    }

    @Override
    public String toString() {
        return nombre;
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
    
    public static TipoNota[] values(Regimen regimen) {
        return Arrays.stream(TipoNota.values()).filter(tipoNota -> tipoNota.getRegimen() != null && tipoNota.getRegimen().equals(regimen)).toArray(TipoNota[]::new);
    }
}
