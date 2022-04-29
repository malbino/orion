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
@Table(name = "nota", uniqueConstraints = @UniqueConstraint(columnNames = {"id_gestionacademica", "id_materia", "id_estudiante"}))
public class Nota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_nota;

    private Integer teoria1;
    private Integer practica1;
    private Integer nota1;

    private Integer teoria2;
    private Integer practica2;
    private Integer nota2;

    private Integer teoria3;
    private Integer practica3;
    private Integer nota3;

    private Integer teoria4;
    private Integer practica4;
    private Integer nota4;

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

    public Nota(Integer notaFinal, Modalidad modalidad, Condicion condicion, GestionAcademica gestionAcademica, Materia materia, Estudiante estudiante, Inscrito inscrito, Grupo grupo) {
        this.notaFinal = notaFinal;
        this.modalidad = modalidad;
        this.condicion = condicion;
        this.gestionAcademica = gestionAcademica;
        this.materia = materia;
        this.estudiante = estudiante;
        this.inscrito = inscrito;
        this.grupo = grupo;
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
     * @return the teoria1
     */
    public Integer getTeoria1() {
        return teoria1;
    }

    /**
     * @param teoria1 the teoria1 to set
     */
    public void setTeoria1(Integer teoria1) {
        this.teoria1 = teoria1;
    }

    /**
     * @return the practica1
     */
    public Integer getPractica1() {
        return practica1;
    }

    /**
     * @param practica1 the practica1 to set
     */
    public void setPractica1(Integer practica1) {
        this.practica1 = practica1;
    }

    /**
     * @return the nota1
     */
    public Integer getNota1() {
        return nota1;
    }

    /**
     * @param nota1 the nota1 to set
     */
    public void setNota1(Integer nota1) {
        this.nota1 = nota1;
    }

    /**
     * @return the teoria2
     */
    public Integer getTeoria2() {
        return teoria2;
    }

    /**
     * @param teoria2 the teoria2 to set
     */
    public void setTeoria2(Integer teoria2) {
        this.teoria2 = teoria2;
    }

    /**
     * @return the practica2
     */
    public Integer getPractica2() {
        return practica2;
    }

    /**
     * @param practica2 the practica2 to set
     */
    public void setPractica2(Integer practica2) {
        this.practica2 = practica2;
    }

    /**
     * @return the nota2
     */
    public Integer getNota2() {
        return nota2;
    }

    /**
     * @param nota2 the nota2 to set
     */
    public void setNota2(Integer nota2) {
        this.nota2 = nota2;
    }

    /**
     * @return the teoria3
     */
    public Integer getTeoria3() {
        return teoria3;
    }

    /**
     * @param teoria3 the teoria3 to set
     */
    public void setTeoria3(Integer teoria3) {
        this.teoria3 = teoria3;
    }

    /**
     * @return the practica3
     */
    public Integer getPractica3() {
        return practica3;
    }

    /**
     * @param practica3 the practica3 to set
     */
    public void setPractica3(Integer practica3) {
        this.practica3 = practica3;
    }

    /**
     * @return the nota3
     */
    public Integer getNota3() {
        return nota3;
    }

    /**
     * @param nota3 the nota3 to set
     */
    public void setNota3(Integer nota3) {
        this.nota3 = nota3;
    }

    /**
     * @return the teoria4
     */
    public Integer getTeoria4() {
        return teoria4;
    }

    /**
     * @param teoria4 the teoria4 to set
     */
    public void setTeoria4(Integer teoria4) {
        this.teoria4 = teoria4;
    }

    /**
     * @return the practica4
     */
    public Integer getPractica4() {
        return practica4;
    }

    /**
     * @param practica4 the practica4 to set
     */
    public void setPractica4(Integer practica4) {
        this.practica4 = practica4;
    }

    /**
     * @return the nota4
     */
    public Integer getNota4() {
        return nota4;
    }

    /**
     * @param nota4 the nota4 to set
     */
    public void setNota4(Integer nota4) {
        this.nota4 = nota4;
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
        hash = 41 * hash + Objects.hashCode(this.getId_nota());
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
