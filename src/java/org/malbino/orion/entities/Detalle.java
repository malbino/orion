/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Concepto;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "detalle", uniqueConstraints = @UniqueConstraint(columnNames = {"concepto", "id_comprobante"}))
public class Detalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    private Concepto concepto;
    private Integer monto;

    @JoinColumn(name = "id_comprobante")
    @ManyToOne(cascade = {CascadeType.REMOVE})
    private Comprobante comprobante;

    @JoinColumn(name = "id_pago")
    @ManyToOne
    private Pago pago;

    public Detalle() {
    }

    public Detalle(Concepto concepto, Integer monto, Comprobante comprobante, Pago pago) {
        this.concepto = concepto;
        this.monto = monto;
        this.comprobante = comprobante;
        this.pago = pago;
    }

    

    /**
     * @return the id_detalle
     */
    public Integer getId_detalle() {
        return id_detalle;
    }

    /**
     * @param id_detalle the id_detalle to set
     */
    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
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
     * @return the comprobante
     */
    public Comprobante getComprobante() {
        return comprobante;
    }

    /**
     * @param comprobante the comprobante to set
     */
    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    /**
     * @return the pago
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
