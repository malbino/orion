/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.malbino.orion.enums.Caracter;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "estudiante")

@PrimaryKeyJoinColumn(name = "id_persona")
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario implements Serializable {

    @Column(unique = true)
    private Integer matricula;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Boolean diplomaBachiller;
    
    private String nombreContacto;
    private Integer celularContacto;
    private String parentescoContacto;
    private String nombreColegio;
    private Caracter caracterColegio;
    private Integer egresoColegio;

    @OneToMany(mappedBy = "estudiante", orphanRemoval = true)
    private List<Nota> notas;

    @OneToMany(mappedBy = "estudiante", orphanRemoval = true)
    private List<Inscrito> inscritos;

    @Transient
    private Date fechaInscripcion;

    public Estudiante() {
    }

    /**
     * @return the matricula
     */
    public Integer getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the diplomaBachiller
     */
    public Boolean getDiplomaBachiller() {
        return diplomaBachiller;
    }

    /**
     * @param diplomaBachiller the diplomaBachiller to set
     */
    public void setDiplomaBachiller(Boolean diplomaBachiller) {
        this.diplomaBachiller = diplomaBachiller;
    }

    /**
     * @return the notas
     */
    public List<Nota> getNotas() {
        return notas;
    }

    /**
     * @param notas the notas to set
     */
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    /**
     * @return the inscritos
     */
    public List<Inscrito> getInscritos() {
        return inscritos;
    }

    /**
     * @param inscritos the inscritos to set
     */
    public void setInscritos(List<Inscrito> inscritos) {
        this.inscritos = inscritos;
    }

    /**
     * @return the fechaInscripcion
     */
    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    /**
     * @param fechaInscripcion the fechaInscripcion to set
     */
    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String fecha_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fecha);
    }

    public String diplomaBachiller_siNo() {
        String s = "";
        if (diplomaBachiller) {
            s = "Sí";
        } else {
            s = "No";
        }
        return s;
    }

    /**
     * @return the nombreContacto
     */
    public String getNombreContacto() {
        return nombreContacto;
    }

    /**
     * @param nombreContacto the nombreContacto to set
     */
    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto.trim().toUpperCase();
    }

    /**
     * @return the celularContacto
     */
    public Integer getCelularContacto() {
        return celularContacto;
    }

    /**
     * @param celularContacto the celularContacto to set
     */
    public void setCelularContacto(Integer celularContacto) {
        this.celularContacto = celularContacto;
    }

    /**
     * @return the parentescoContacto
     */
    public String getParentescoContacto() {
        return parentescoContacto;
    }

    /**
     * @param parentescoContacto the parentescoContacto to set
     */
    public void setParentescoContacto(String parentescoContacto) {
        this.parentescoContacto = parentescoContacto.trim().toUpperCase();
    }

    /**
     * @return the nombreColegio
     */
    public String getNombreColegio() {
        return nombreColegio;
    }

    /**
     * @param nombreColegio the nombreColegio to set
     */
    public void setNombreColegio(String nombreColegio) {
        this.nombreColegio = nombreColegio.trim().toUpperCase();
    }

    /**
     * @return the caracterColegio
     */
    public Caracter getCaracterColegio() {
        return caracterColegio;
    }

    /**
     * @param caracterColegio the caracterColegio to set
     */
    public void setCaracterColegio(Caracter caracterColegio) {
        this.caracterColegio = caracterColegio;
    }

    /**
     * @return the egresoColegio
     */
    public Integer getEgresoColegio() {
        return egresoColegio;
    }

    /**
     * @param egresoColegio the egresoColegio to set
     */
    public void setEgresoColegio(Integer egresoColegio) {
        this.egresoColegio = egresoColegio;
    }
}
