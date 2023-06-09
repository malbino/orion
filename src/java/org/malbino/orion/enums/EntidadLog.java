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
public enum EntidadLog {

    ACTIVIDAD("Actividad"),
    ASISTENCIA("Asistencia"),
    CAMPUS("Campus"),
    CARRERA("Carrera"),
    CARRERA_ESTUDIANTE("Carrera Estudiante"),
    COMPROBANTE("Comprobante"),
    DETALLE("Detalle"),
    EMPLEADO("Empleado"),
    EMPRESA("Empresa"),
    ESTUDIANTE("Estudiante"),
    GESTION_ACADEMICA("Gestión Académica"),
    GRUPO("Grupo"),
    GRUPO_PASANTIA("Grupo Pasantía"),
    INDICADOR_PASANTIA("Indicador Pasantía"),
    INSCRITO("Inscrito"),
    INSTITUTO("Instituto"),
    MATERIA("Materia"),
    MENCION("Mención"),
    NOTA("Nota"),
    NOTA_PASANTIA("Nota Pasantía"),
    PAGO("Pago"),
    PASANTIA("Pasantía"),
    POSTULANTE("Postulante"),
    RECURSO("Recurso"),
    ROL("Rol"),
    USUARIO("Usuario");

    private String nombre;

    private EntidadLog(String nombre) {
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
