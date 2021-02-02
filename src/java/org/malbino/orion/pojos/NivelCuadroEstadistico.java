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
    private Integer numeroParalelos;
    private Long varonesInscritos;
    private Long mujeresInscritos;
    private Long totalInscritos;

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
    public Integer getNumeroParalelos() {
        return numeroParalelos;
    }

    /**
     * @param numeroParalelos the numeroParalelos to set
     */
    public void setNumeroParalelos(Integer numeroParalelos) {
        this.numeroParalelos = numeroParalelos;
    }

    /**
     * @return the varonesInscritos
     */
    public Long getVaronesInscritos() {
        return varonesInscritos;
    }

    /**
     * @param varonesInscritos the varonesInscritos to set
     */
    public void setVaronesInscritos(Long varonesInscritos) {
        this.varonesInscritos = varonesInscritos;
    }

    /**
     * @return the mujeresInscritos
     */
    public Long getMujeresInscritos() {
        return mujeresInscritos;
    }

    /**
     * @param mujeresInscritos the mujeresInscritos to set
     */
    public void setMujeresInscritos(Long mujeresInscritos) {
        this.mujeresInscritos = mujeresInscritos;
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
    
    
}
