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
public enum Modalidad {
    REGULAR("REGULAR", "R", true),
    CONVALIDACION("CONVALIDACIÓN", "C", false),
    TRASPASO("TRASPASO", "T", false),
    MIGRACION("MIGRACIÓN", "M", false);
    
    private String nombre;
    private String abreviatura;
    private Boolean regular;

    private Modalidad(String nombre, String abreviatura, Boolean regular) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.regular = regular;
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
     * @return the regular
     */
    public Boolean getRegular() {
        return regular;
    }

    /**
     * @param regular the regular to set
     */
    public void setRegular(Boolean regular) {
        this.regular = regular;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    public static Modalidad[] values(Boolean regular){
        return Arrays.stream(Modalidad.values()).filter(modalidad -> !modalidad.regular).toArray(Modalidad[]::new);
    }
}
