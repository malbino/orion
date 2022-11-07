/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "asistencia", uniqueConstraints = @UniqueConstraint(columnNames = {"ingreso", "salida", "id_notaPasantia"}))
public class Asistencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_asistencia;

    private Date ingreso;
    private Date salida;
    @Column(length = 4095)
    private String descripcion;

    @JoinColumn(name = "id_notaPasantia")
    @ManyToOne
    private NotaPasantia notaPasantia;

    public Asistencia() {
    }

    public Asistencia(Date ingreso, Date salida, String descripcion, NotaPasantia notaPasantia) {
        this.ingreso = ingreso;
        this.salida = salida;
        this.descripcion = descripcion;
        this.notaPasantia = notaPasantia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id_asistencia);
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
        final Asistencia other = (Asistencia) obj;
        if (!Objects.equals(this.id_asistencia, other.id_asistencia)) {
            return false;
        }
        return true;
    }

    public String ingreso_ddMMyyyyHHmm() {
        return Fecha.formatearFecha_ddMMyyyyHHmm(ingreso);
    }

    public String salida_ddMMyyyyHHmm() {
        return Fecha.formatearFecha_ddMMyyyyHHmm(salida);
    }

    public double horas() {
        double milisegundos = salida.getTime() - ingreso.getTime();

        return Redondeo.redondear_HALFUP(milisegundos / (60 * 60 * 1000), 2);
    }

    /**
     * @return the id_asistencia
     */
    public Integer getId_asistencia() {
        return id_asistencia;
    }

    /**
     * @param id_asistencia the id_asistencia to set
     */
    public void setId_asistencia(Integer id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    /**
     * @return the ingreso
     */
    public Date getIngreso() {
        return ingreso;
    }

    /**
     * @param ingreso the ingreso to set
     */
    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    /**
     * @return the salida
     */
    public Date getSalida() {
        return salida;
    }

    /**
     * @param salida the salida to set
     */
    public void setSalida(Date salida) {
        this.salida = salida;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the notaPasantia
     */
    public NotaPasantia getNotaPasantia() {
        return notaPasantia;
    }

    /**
     * @param notaPasantia the notaPasantia to set
     */
    public void setNotaPasantia(NotaPasantia notaPasantia) {
        this.notaPasantia = notaPasantia;
    }

}
