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
    private Long numeroParalelos;
    private Long totalInscritos;
    
    private Long varones;
    private Long mujeres;
    
    private Long efectivos;
    private Long retirados;
    
    private Long aprobados;
    private Long reprobados;

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
     * @return the numeroParalelos
     */
    public Long getNumeroParalelos() {
        return numeroParalelos;
    }

    /**
     * @param numeroParalelos the numeroParalelos to set
     */
    public void setNumeroParalelos(Long numeroParalelos) {
        this.numeroParalelos = numeroParalelos;
    }

    /**
     * @return the totalInscritos
     */
    public Long getTotalInscritos() {
        return totalInscritos;
    }

    /**
     * @param totalInscritos the totalInscritos to set
     */
    public void setTotalInscritos(Long totalInscritos) {
        this.totalInscritos = totalInscritos;
    }

    /**
     * @return the varones
     */
    public Long getVarones() {
        return varones;
    }

    /**
     * @param varones the varones to set
     */
    public void setVarones(Long varones) {
        this.varones = varones;
    }

    /**
     * @return the mujeres
     */
    public Long getMujeres() {
        return mujeres;
    }

    /**
     * @param mujeres the mujeres to set
     */
    public void setMujeres(Long mujeres) {
        this.mujeres = mujeres;
    }

    /**
     * @return the efectivos
     */
    public Long getEfectivos() {
        return efectivos;
    }

    /**
     * @param efectivos the efectivos to set
     */
    public void setEfectivos(Long efectivos) {
        this.efectivos = efectivos;
    }

    /**
     * @return the retirados
     */
    public Long getRetirados() {
        return retirados;
    }

    /**
     * @param retirados the retirados to set
     */
    public void setRetirados(Long retirados) {
        this.retirados = retirados;
    }

    /**
     * @return the aprobados
     */
    public Long getAprobados() {
        return aprobados;
    }

    /**
     * @param aprobados the aprobados to set
     */
    public void setAprobados(Long aprobados) {
        this.aprobados = aprobados;
    }

    /**
     * @return the reprobados
     */
    public Long getReprobados() {
        return reprobados;
    }

    /**
     * @param reprobados the reprobados to set
     */
    public void setReprobados(Long reprobados) {
        this.reprobados = reprobados;
    }
}
