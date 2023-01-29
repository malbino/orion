/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.pojos;

import java.io.Serializable;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author Tincho
 */
public class NivelCuadroEstadistico implements Serializable {

    private String codigo;
    private String nombre;
    private Nivel nivel;
    private String paralelo;

    private Integer inscritosVarones;
    private Integer inscritosMujeres;
    private Integer inscritos;

    private Integer efectivosVarones;
    private Integer efectivosMujeres;
    private Integer efectivos;

    private Integer retiradosVarones;
    private Integer retiradosMujeres;
    private Integer retirados;

    private Integer aprobadosVarones;
    private Integer aprobadosMujeres;
    private Integer aprobados;

    private Integer reprobadosVarones;
    private Integer reprobadosMujeres;
    private Integer reprobados;

    public NivelCuadroEstadistico() {
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
     * @return the nivel
     */
    public Nivel getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the inscritosVarones
     */
    public Integer getInscritosVarones() {
        return inscritosVarones;
    }

    /**
     * @param inscritosVarones the inscritosVarones to set
     */
    public void setInscritosVarones(Integer inscritosVarones) {
        this.inscritosVarones = inscritosVarones;
    }

    /**
     * @return the inscritosMujeres
     */
    public Integer getInscritosMujeres() {
        return inscritosMujeres;
    }

    /**
     * @param inscritosMujeres the inscritosMujeres to set
     */
    public void setInscritosMujeres(Integer inscritosMujeres) {
        this.inscritosMujeres = inscritosMujeres;
    }

    /**
     * @return the inscritos
     */
    public Integer getInscritos() {
        return inscritos;
    }

    /**
     * @param inscritos the inscritos to set
     */
    public void setInscritos(Integer inscritos) {
        this.inscritos = inscritos;
    }

    /**
     * @return the efectivosVarones
     */
    public Integer getEfectivosVarones() {
        return efectivosVarones;
    }

    /**
     * @param efectivosVarones the efectivosVarones to set
     */
    public void setEfectivosVarones(Integer efectivosVarones) {
        this.efectivosVarones = efectivosVarones;
    }

    /**
     * @return the efectivosMujeres
     */
    public Integer getEfectivosMujeres() {
        return efectivosMujeres;
    }

    /**
     * @param efectivosMujeres the efectivosMujeres to set
     */
    public void setEfectivosMujeres(Integer efectivosMujeres) {
        this.efectivosMujeres = efectivosMujeres;
    }

    /**
     * @return the efectivos
     */
    public Integer getEfectivos() {
        return efectivos;
    }

    /**
     * @param efectivos the efectivos to set
     */
    public void setEfectivos(Integer efectivos) {
        this.efectivos = efectivos;
    }

    /**
     * @return the retiradosVarones
     */
    public Integer getRetiradosVarones() {
        return retiradosVarones;
    }

    /**
     * @param retiradosVarones the retiradosVarones to set
     */
    public void setRetiradosVarones(Integer retiradosVarones) {
        this.retiradosVarones = retiradosVarones;
    }

    /**
     * @return the retiradosMujeres
     */
    public Integer getRetiradosMujeres() {
        return retiradosMujeres;
    }

    /**
     * @param retiradosMujeres the retiradosMujeres to set
     */
    public void setRetiradosMujeres(Integer retiradosMujeres) {
        this.retiradosMujeres = retiradosMujeres;
    }

    /**
     * @return the retirados
     */
    public Integer getRetirados() {
        return retirados;
    }

    /**
     * @param retirados the retirados to set
     */
    public void setRetirados(Integer retirados) {
        this.retirados = retirados;
    }

    /**
     * @return the aprobadosVarones
     */
    public Integer getAprobadosVarones() {
        return aprobadosVarones;
    }

    /**
     * @param aprobadosVarones the aprobadosVarones to set
     */
    public void setAprobadosVarones(Integer aprobadosVarones) {
        this.aprobadosVarones = aprobadosVarones;
    }

    /**
     * @return the aprobadosMujeres
     */
    public Integer getAprobadosMujeres() {
        return aprobadosMujeres;
    }

    /**
     * @param aprobadosMujeres the aprobadosMujeres to set
     */
    public void setAprobadosMujeres(Integer aprobadosMujeres) {
        this.aprobadosMujeres = aprobadosMujeres;
    }

    /**
     * @return the aprobados
     */
    public Integer getAprobados() {
        return aprobados;
    }

    /**
     * @param aprobados the aprobados to set
     */
    public void setAprobados(Integer aprobados) {
        this.aprobados = aprobados;
    }

    /**
     * @return the reprobadosVarones
     */
    public Integer getReprobadosVarones() {
        return reprobadosVarones;
    }

    /**
     * @param reprobadosVarones the reprobadosVarones to set
     */
    public void setReprobadosVarones(Integer reprobadosVarones) {
        this.reprobadosVarones = reprobadosVarones;
    }

    /**
     * @return the reprobadosMujeres
     */
    public Integer getReprobadosMujeres() {
        return reprobadosMujeres;
    }

    /**
     * @param reprobadosMujeres the reprobadosMujeres to set
     */
    public void setReprobadosMujeres(Integer reprobadosMujeres) {
        this.reprobadosMujeres = reprobadosMujeres;
    }

    /**
     * @return the reprobados
     */
    public Integer getReprobados() {
        return reprobados;
    }

    /**
     * @param reprobados the reprobados to set
     */
    public void setReprobados(Integer reprobados) {
        this.reprobados = reprobados;
    }

    /**
     * @return the paralelo
     */
    public String getParalelo() {
        return paralelo;
    }

    /**
     * @param paralelo the paralelo to set
     */
    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }
}
