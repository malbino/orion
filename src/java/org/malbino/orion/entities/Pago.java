/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Concepto;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "pago", uniqueConstraints = @UniqueConstraint(columnNames = {"concepto", "id_inscrito"}))
public class Pago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    private Concepto concepto;
    private Integer monto;
    private Boolean pagado;

    @Transient
    private Integer montoAcumulado;

    @JoinColumn(name = "id_inscrito")
    @ManyToOne
    private Inscrito inscrito;
    
    @OneToMany(mappedBy = "pago", orphanRemoval = true)
    private List<Detalle> detalles;

    public Pago() {
    }

    public Pago(Concepto concepto, Integer monto, Boolean pagado, Inscrito inscrito) {
        this.concepto = concepto;
        this.monto = monto;
        this.pagado = pagado;
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
     * @return the pagado
     */
    public Boolean getPagado() {
        return pagado;
    }

    /**
     * @param pagado the pagado to set
     */
    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
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

    /**
     * @return the montoAcumulado
     */
    public Integer getMontoAcumulado() {
        return montoAcumulado;
    }

    /**
     * @param montoAcumulado the montoAcumulado to set
     */
    public void setMontoAcumulado(Integer montoAcumulado) {
        this.montoAcumulado = montoAcumulado;
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
    
    public String pagadoToString() {
        return pagado ? "Sí" : "No";
    }

    /**
     * @return the detalles
     */
    public List<Detalle> getDetalles() {
        return detalles;
    }

    /**
     * @param detalles the detalles to set
     */
    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
}
