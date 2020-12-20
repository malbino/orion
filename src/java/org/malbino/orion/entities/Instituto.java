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
import org.malbino.orion.enums.Caracter;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "instituto")
public class Instituto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_instituto;

    private String nombreRegulador;
    private String ubicacion;
    private Caracter caracter;
    private String nombreImpuestos;
    private String nit;
    private String actividadEconomica;
    private Integer precioCredito;
    private String resolucionMinisterial;
    private String logo;

    @JoinColumn(name = "id_rector")
    @ManyToOne
    private Empleado rector;

    @JoinColumn(name = "id_directoracademico")
    @ManyToOne
    private Empleado directorAcademico;

    public Instituto() {
    }

    /**
     * @return the id_instituto
     */
    public Integer getId_instituto() {
        return id_instituto;
    }

    /**
     * @param id_instituto the id_instituto to set
     */
    public void setId_instituto(Integer id_instituto) {
        this.id_instituto = id_instituto;
    }

    /**
     * @return the nombreRegulador
     */
    public String getNombreRegulador() {
        return nombreRegulador;
    }

    /**
     * @param nombreRegulador the nombreRegulador to set
     */
    public void setNombreRegulador(String nombreRegulador) {
        this.nombreRegulador = nombreRegulador;
    }

    /**
     * @return the ubicacion
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * @param ubicacion the ubicacion to set
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * @return the caracter
     */
    public Caracter getCaracter() {
        return caracter;
    }

    /**
     * @param caracter the caracter to set
     */
    public void setCaracter(Caracter caracter) {
        this.caracter = caracter;
    }

    /**
     * @return the nombreImpuestos
     */
    public String getNombreImpuestos() {
        return nombreImpuestos;
    }

    /**
     * @param nombreImpuestos the nombreImpuestos to set
     */
    public void setNombreImpuestos(String nombreImpuestos) {
        this.nombreImpuestos = nombreImpuestos;
    }

    /**
     * @return the nit
     */
    public String getNit() {
        return nit;
    }

    /**
     * @param nit the nit to set
     */
    public void setNit(String nit) {
        this.nit = nit;
    }

    /**
     * @return the actividadEconomica
     */
    public String getActividadEconomica() {
        return actividadEconomica;
    }

    /**
     * @param actividadEconomica the actividadEconomica to set
     */
    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    /**
     * @return the precioCredito
     */
    public Integer getPrecioCredito() {
        return precioCredito;
    }

    /**
     * @param precioCredito the precioCredito to set
     */
    public void setPrecioCredito(Integer precioCredito) {
        this.precioCredito = precioCredito;
    }

    /**
     * @return the resolucionMinisterial
     */
    public String getResolucionMinisterial() {
        return resolucionMinisterial;
    }

    /**
     * @param resolucionMinisterial the resolucionMinisterial to set
     */
    public void setResolucionMinisterial(String resolucionMinisterial) {
        this.resolucionMinisterial = resolucionMinisterial;
    }

    /**
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the rector
     */
    public Empleado getRector() {
        return rector;
    }

    /**
     * @param rector the rector to set
     */
    public void setRector(Empleado rector) {
        this.rector = rector;
    }

    /**
     * @return the directorAcademico
     */
    public Empleado getDirectorAcademico() {
        return directorAcademico;
    }

    /**
     * @param directorAcademico the directorAcademico to set
     */
    public void setDirectorAcademico(Empleado directorAcademico) {
        this.directorAcademico = directorAcademico;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id_instituto);
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
        final Instituto other = (Instituto) obj;
        if (!Objects.equals(this.id_instituto, other.id_instituto)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreRegulador;
    }

}
