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
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Modalidad;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "nota", catalog = "orion", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"gestionAcademica", "materia", "estudiante"}))
public class Nota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_nota;

    private Integer primerParcial;
    private Integer segundoParcial;
    private Integer tercerParcial;
    private Integer cuartoParcial;
    private Integer notaFinal;
    private Integer recuperatorio;
    private Modalidad modalidad;
    private Condicion condicion;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    @JoinColumn(name = "id_materia")
    @ManyToOne
    private Materia materia;

    @JoinColumn(name = "id_estudiante")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_inscrito")
    @ManyToOne
    private Inscrito inscrito;

    @JoinColumn(name = "id_grupo")
    @ManyToOne
    private Grupo grupo;

    public Nota() {
    }

    public Nota(Modalidad modalidad, Condicion condicion, GestionAcademica gestionAcademica, Materia materia, Estudiante estudiante, Inscrito inscrito, Grupo grupo) {
        this.modalidad = modalidad;
        this.condicion = condicion;
        this.gestionAcademica = gestionAcademica;
        this.materia = materia;
        this.estudiante = estudiante;
        this.inscrito = inscrito;
        this.grupo = grupo;
    }

    public Nota(Modalidad modalidad, Condicion condicion, GestionAcademica gestionAcademica, Materia materia, Estudiante estudiante) {
        this.modalidad = modalidad;
        this.condicion = condicion;
        this.gestionAcademica = gestionAcademica;
        this.materia = materia;
        this.estudiante = estudiante;
    }

    

    /**
     * @return the id_nota
     */
    public Integer getId_nota() {
        return id_nota;
    }

    /**
     * @param id_nota the id_nota to set
     */
    public void setId_nota(Integer id_nota) {
        this.id_nota = id_nota;
    }

    /**
     * @return the primerParcial
     */
    public Integer getPrimerParcial() {
        return primerParcial;
    }

    /**
     * @param primerParcial the primerParcial to set
     */
    public void setPrimerParcial(Integer primerParcial) {
        this.primerParcial = primerParcial;
    }

    /**
     * @return the segundoParcial
     */
    public Integer getSegundoParcial() {
        return segundoParcial;
    }

    /**
     * @param segundoParcial the segundoParcial to set
     */
    public void setSegundoParcial(Integer segundoParcial) {
        this.segundoParcial = segundoParcial;
    }

    /**
     * @return the tercerParcial
     */
    public Integer getTercerParcial() {
        return tercerParcial;
    }

    /**
     * @param tercerParcial the tercerParcial to set
     */
    public void setTercerParcial(Integer tercerParcial) {
        this.tercerParcial = tercerParcial;
    }

    /**
     * @return the cuartoParcial
     */
    public Integer getCuartoParcial() {
        return cuartoParcial;
    }

    /**
     * @param cuartoParcial the cuartoParcial to set
     */
    public void setCuartoParcial(Integer cuartoParcial) {
        this.cuartoParcial = cuartoParcial;
    }

    /**
     * @return the notaFinal
     */
    public Integer getNotaFinal() {
        return notaFinal;
    }

    /**
     * @param notaFinal the notaFinal to set
     */
    public void setNotaFinal(Integer notaFinal) {
        this.notaFinal = notaFinal;
    }

    /**
     * @return the recuperatorio
     */
    public Integer getRecuperatorio() {
        return recuperatorio;
    }

    /**
     * @param recuperatorio the recuperatorio to set
     */
    public void setRecuperatorio(Integer recuperatorio) {
        this.recuperatorio = recuperatorio;
    }

    /**
     * @return the modalidad
     */
    public Modalidad getModalidad() {
        return modalidad;
    }

    /**
     * @param modalidad the modalidad to set
     */
    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    /**
     * @return the condicion
     */
    public Condicion getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    /**
     * @return the gestionAcademica
     */
    public GestionAcademica getGestionAcademica() {
        return gestionAcademica;
    }

    /**
     * @param gestionAcademica the gestionAcademica to set
     */
    public void setGestionAcademica(GestionAcademica gestionAcademica) {
        this.gestionAcademica = gestionAcademica;
    }

    /**
     * @return the materia
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * @param materia the materia to set
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     * @return the estudiante
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
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
     * @return the grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id_nota);
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
        final Nota other = (Nota) obj;
        if (!Objects.equals(this.id_nota, other.id_nota)) {
            return false;
        }
        return true;
    }

}
