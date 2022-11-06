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
public enum Evaluacion {
    EXCELENTE("EXCELENTE", 100),
    MUY_BUENO("MUY BUENO", 75),
    BUENO("BUENO", 50),
    REGULAR("REGULAR", 25),
    INSUFICIENTE("INSUFICIENTE", 0);

    private String notaCualitativa;
    private Integer notaCuantitativa;

    private Evaluacion(String notaCualitativa, Integer notaCuantitativa) {
        this.notaCualitativa = notaCualitativa;
        this.notaCuantitativa = notaCuantitativa;
    }

    /**
     * @return the notaCualitativa
     */
    public String getNotaCualitativa() {
        return notaCualitativa;
    }

    /**
     * @param notaCualitativa the notaCualitativa to set
     */
    public void setNotaCualitativa(String notaCualitativa) {
        this.notaCualitativa = notaCualitativa;
    }

    /**
     * @return the notaCuantitativa
     */
    public Integer getNotaCuantitativa() {
        return notaCuantitativa;
    }

    /**
     * @param notaCuantitativa the notaCuantitativa to set
     */
    public void setNotaCuantitativa(Integer notaCuantitativa) {
        this.notaCuantitativa = notaCuantitativa;
    }

    
}
