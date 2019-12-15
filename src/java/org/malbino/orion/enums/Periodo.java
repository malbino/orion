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
public enum Periodo {
    I("I", 1),
    II("II", 2);

    private String periodoRomano;
    private Integer periodoEntero;

    private Periodo(String periodoRomano, Integer periodoEntero) {
        this.periodoRomano = periodoRomano;
        this.periodoEntero = periodoEntero;
    }

    /**
     * @return the periodoRomano
     */
    public String getPeriodoRomano() {
        return periodoRomano;
    }

    /**
     * @param periodoRomano the periodoRomano to set
     */
    public void setPeriodoRomano(String periodoRomano) {
        this.periodoRomano = periodoRomano;
    }

    /**
     * @return the periodoEntero
     */
    public Integer getPeriodoEntero() {
        return periodoEntero;
    }

    /**
     * @param periodoEntero the periodoEntero to set
     */
    public void setPeriodoEntero(Integer periodoEntero) {
        this.periodoEntero = periodoEntero;
    }

    @Override
    public String toString() {
        return periodoRomano;
    }

}
