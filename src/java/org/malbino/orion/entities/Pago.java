/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.malbino.orion.enums.Concepto;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "pago", catalog = "orion", schema = "public")
public class Pago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    private Concepto concepto;
    private Integer monto;
    
    @JoinColumn(name = "id_inscrito")
    @ManyToOne
    private Inscrito inscrito; 

    public Pago() {
    }

    public Pago(Concepto concepto, Integer monto, Inscrito inscrito) {
        this.concepto = concepto;
        this.monto = monto;
        this.inscrito = inscrito;
    }
    
    

    /**
     * @return the id_pago
     */
    public Integer getId_pago() {
        return id_pago;
    }

    /**
     * @param id_pago the id_pago to set
     */
    public void setId_pago(Integer id_pago) {
        this.id_pago = id_pago;
    }

    /**
     * @return the concepto
     */
    public Concepto getConcepto() {
        return concepto;
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    /**
     * @return the monto
     */
    public Integer getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    /**
     * @return the inscrito
     */
    public Inscrito getInscrito() {
        return inscrito;
    }

    /**
     * @param inscrito the inscrito to set
     */
    public void setInscrito(Inscrito inscrito) {
        this.inscrito = inscrito;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id_pago);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pago other = (Pago) obj;
        if (!Objects.equals(this.id_pago, other.id_pago)) {
            return false;
        }
        return true;
    }

    
}