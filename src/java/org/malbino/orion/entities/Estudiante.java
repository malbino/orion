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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    private Boolean tituloBachiller;

    @JoinTable(name = "cursa", joinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")}, inverseJoinColumns = {
        @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")})
    @ManyToMany
    private List<Carrera> carreras;

    @OneToMany(mappedBy = "estudiante", orphanRemoval = true)
    private List<Nota> notas;

    @OneToMany(mappedBy = "estudiante", orphanRemoval = true)
    private List<Inscrito> inscritos;

    public Estudiante() {
    }

    public String carrerasToString() {
        String s = " ";
        for (Carrera m : carreras) {
            if (s.compareTo(" ") == 0) {
                s = m.getCodigo();
            } else {
                s += ", " + m.getCodigo();
            }
        }
        return s;
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
     * @return the tituloBachiller
     */
    public Boolean getTituloBachiller() {
        return tituloBachiller;
    }

    /**
     * @param tituloBachiller the tituloBachiller to set
     */
    public void setTituloBachiller(Boolean tituloBachiller) {
        this.tituloBachiller = tituloBachiller;
    }

    /**
     * @return the carreras
     */
    public List<Carrera> getCarreras() {
        return carreras;
    }

    /**
     * @param carreras the carreras to set
     */
    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
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
}
