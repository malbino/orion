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
public enum TipoEmpresa {
    EMPRESA_UNIPERSONAL("EMPRESA UNIPERSONAL"),
    SOCIEDAD_RESPONSABILIDAD_LIMITADA("SOCIEDAD DE RESPONSABILIDAD LIMITADA"),
    SOCIEDAD_ANONIMA("SOCIEDAD ANONIMA"),
    INSTITUCION_PUBLICA("INSTITUCION PUBLICA"),
    OTROS("OTROS");

    private String nombre;

    private TipoEmpresa(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre;
    }
}
