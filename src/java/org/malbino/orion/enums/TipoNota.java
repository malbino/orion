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
    PRIMER_PARCIAL_SEMESTRAL_2P("PRIMER PARCIAL", ModalidadEvaluacion.SEMESTRAL_2P),
    SEGUNDO_PARCIAL_SEMESTRAL_2P("SEGUNDO PARCIAL", ModalidadEvaluacion.SEMESTRAL_2P),
    RECUPERATORIO_SEMESTRAL_2P("RECUPERATORIO", ModalidadEvaluacion.SEMESTRAL_2P),
    PRIMER_PARCIAL_SEMESTRAL_3P("PRIMER PARCIAL", ModalidadEvaluacion.SEMESTRAL_3P),
    SEGUNDO_PARCIAL_SEMESTRAL_3P("SEGUNDO PARCIAL", ModalidadEvaluacion.SEMESTRAL_3P),
    TERCER_PARCIAL_SEMESTRAL_3P("TERCER PARCIAL", ModalidadEvaluacion.SEMESTRAL_3P),
    RECUPERATORIO_SEMESTRAL_3P("RECUPERATORIO", ModalidadEvaluacion.SEMESTRAL_3P),
    PRIMER_PARCIAL_ANUAL_4P("PRIMER PARCIAL", ModalidadEvaluacion.ANUAL_4P),
    SEGUNDO_PARCIAL_ANUAL_4P("SEGUNDO PARCIAL", ModalidadEvaluacion.ANUAL_4P),
    TERCER_PARCIAL_ANUAL_4P("TERCER PARCIAL", ModalidadEvaluacion.ANUAL_4P),
    CUARTO_PARCIAL_ANUAL_4P("CUARTO PARCIAL", ModalidadEvaluacion.ANUAL_4P),
    RECUPERATORIO_ANUAL_4P("RECUPERATORIO", ModalidadEvaluacion.ANUAL_4P);

    private String nombre;
    private ModalidadEvaluacion modalidadEvaluacion;

    private TipoNota(String nombre, ModalidadEvaluacion modalidadEvaluacion) {
        this.nombre = nombre;
        this.modalidadEvaluacion = modalidadEvaluacion;
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
     * @return the modalidadEvaluacion
     */
    public ModalidadEvaluacion getModalidadEvaluacion() {
        return modalidadEvaluacion;
    }

    /**
     * @param modalidadEvaluacion the modalidadEvaluacion to set
     */
    public void setModalidadEvaluacion(ModalidadEvaluacion modalidadEvaluacion) {
        this.modalidadEvaluacion = modalidadEvaluacion;
    }

    public static TipoNota[] values(ModalidadEvaluacion modalidadEvaluacion) {
        return Arrays.stream(TipoNota.values()).filter(tipoNota -> tipoNota.getModalidadEvaluacion() != null && tipoNota.getModalidadEvaluacion().equals(modalidadEvaluacion)).toArray(TipoNota[]::new);
    }
}
