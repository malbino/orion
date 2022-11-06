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
public enum Indicador {
    CUMPLIMINETO_NORMAS(1, "Cumplimiento de la normas de la empresa", Categoria.DISPOSICION_TRABAJO),
    RESPONSABILIDAD_CUMPLIMIENTO(2, "Responsabilidad en el cumplimiento de la tareas asignadas", Categoria.DISPOSICION_TRABAJO),
    CUMPLIMIENTO_HORARIO(3, "Cumplimiento en el horario de trabajo", Categoria.DISPOSICION_TRABAJO),
    COOPERACION_TAREA(4, "Cooperación ante cualquier tarea", Categoria.DISPOSICION_TRABAJO),
    INICIATIVA_DESICIONES_IDEAS(5, "Iniciativa, toma de decisiones y aporte de ideas", Categoria.DISPOSICION_TRABAJO),
    RELACIONES_INTERPERSONALES(6, "Relaciones interpersonales", Categoria.DISPOSICION_TRABAJO),
    LENGUAJE_ORAL_ESCRITO(7, "Manejo del lenguaje oral y escrito", Categoria.DISPOSICION_TRABAJO),
    METODOS_PROCEDIMIENTOS_TECNICAS(8, "Manejo de métodos, procedimientos y técnicas de trabajo", Categoria.DISPOSICION_TRABAJO),
    MANEJO_INSTRUMENTOS_HERRAMIENTAS(9, "Manejo y conservación de instrumentos y herramientas de trabajo", Categoria.NIVEL_DESEMPEÑO),
    PLANIFICACION_ORGANIZACION_TAREAS(10, "Planificación y organización de las tareas", Categoria.NIVEL_DESEMPEÑO),
    CRITERIO_SOLUCION_PROBLEMAS(11, "Criterio para la solución de problemas", Categoria.NIVEL_DESEMPEÑO),
    EJECUCION_TAREAS_ENCOMENDADAS(12, "Ejecución de las tareas encomendadas", Categoria.NIVEL_DESEMPEÑO),
    ACTUACION_SITUACIONES_IMPROVISTAS(13, "Actuación ante situaciones imprevistas", Categoria.NIVEL_DESEMPEÑO),
    DOMINIO_TEORICO_PRACTICO(14, "Muestra dominio teórico-práctico en el área profesional", Categoria.NIVEL_DESEMPEÑO);

    private Integer numero;
    private String nombre;
    private Categoria categoria;

    private Indicador(Integer numero, String nombre, Categoria categoria) {
        this.numero = numero;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nombre;
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
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}
