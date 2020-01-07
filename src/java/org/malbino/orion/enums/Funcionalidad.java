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
    INSCRIPCION("Inscripcion"),
    INSCRIPCION_INTERNET("Inscripcion por Internet"),
    REGISTRO_NOTAS_PRIMER_PARCIAL("Registro notas primer parcial"),
    REGISTRO_NOTAS_SEGUNDO_PARCIAL("Registro notas segundo parcial"),
    REGISTRO_NOTAS_TERCER_PARCIAL("Registro notas tercer parcial"),
    REGISTRO_NOTAS_CUARTO_PARCIAL("Registro notas cuarto parcial"),
    REGISTRO_NOTAS_PRUEBA_RECUPERACION("Registro notas prueba recuperacion");
    
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
