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
public enum Funcionalidad {
    INSCRIPCION("INSCRIPCIÓN"),
    INSCRIPCION_INTERNET("INSCRIPCIÓN POR INTERNET"),
    REGISTRO_NOTAS_PRIMER_PARCIAL("REGISTRO NOTAS PRIMER PARCIAL"),
    REGISTRO_NOTAS_SEGUNDO_PARCIAL("REGISTRO NOTAS SEGUNDO PARCIAL"),
    REGISTRO_NOTAS_TERCER_PARCIAL("REGISTRO NOTAS TERCER PARCIAL"),
    REGISTRO_NOTAS_CUARTO_PARCIAL("REGISTRO NOTAS CUARTO PARCIAL"),
    REGISTRO_NOTAS_PRUEBA_RECUPERACION("REGISTRO NOTAS PRUEBA RECUPERACIÓN"),
    ADMISION("ADMISIÓN");

    private String nombre;

    private Funcionalidad(String nombre) {
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
