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
import org.malbino.orion.enums.Turno;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "grupo", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "turno", "id_gestionacademica", "id_materia"}))
public class Grupo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_grupo;

    private String codigo;
    private Integer capacidad;
    private Turno turno;
    private Boolean abierto;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    @JoinColumn(name = "id_materia")
    @ManyToOne
    private Materia materia;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Empleado empleado;

    public Grupo() {
    }

    public Grupo(String codigo, Integer capacidad, Turno turno, Boolean abierto, GestionAcademica gestionAcademica, Materia materia) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.turno = turno;
        this.abierto = abierto;
        this.gestionAcademica = gestionAcademica;
        this.materia = materia;
    }

    public String idnumberMoodle() {
        return "g" + id_grupo;
    }

    public String fullNameMoodle() {
        return this.getMateria().getNombre() + " - "
                + this.getTurno().getInicial() + this.codigo + " - "
                + this.getMateria().getNivel().getAbreviatura() + " - "
                + this.getMateria().getCarrera().getNombre() + " - "
                + this.getGestionAcademica().toString();
    }

    public String shortNameMoodle() {
        return this.getMateria().getCodigo() + " - "
                + this.getTurno().getInicial() + this.codigo + " - "
                + this.getMateria().getNivel().getAbreviatura() + " - "
                + this.getMateria().getCarrera().getCodigo() + " - "
                + this.getGestionAcademica().toString();
    }

    public long inicioMoodle() {
        return this.getGestionAcademica().getInicio().getTime() / 1000;
    }

    public long finMoodle() {
        return this.getGestionAcademica().getFin().getTime() / 1000;
    }

    public String summaryMoodle() {
        return "Curso de " + this.getMateria().getNombre() + " del " + this.getMateria().getNivel().getNombre() + " de la carrera de " + this.getMateria().getCarrera().getNombre() + ".";
    }

    /**
     * @return the id_grupo
     */
    public Integer getId_grupo() {
        return id_grupo;
    }

    /**
     * @param id_grupo the id_grupo to set
     */
    public void setId_grupo(Integer id_grupo) {
        this.id_grupo = id_grupo;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the capacidad
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * @param capacidad the capacidad to set
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * @return the turno
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * @param turno the turno to set
     */
    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    /**
     * @return the abierto
     */
    public Boolean getAbierto() {
        return abierto;
    }

    /**
     * @param abierto the abierto to set
     */
    public void setAbierto(Boolean abierto) {
        this.abierto = abierto;
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
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String abiertoToString() {
        return abierto ? "Sí" : "No";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id_grupo);
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
        final Grupo other = (Grupo) obj;
        if (!Objects.equals(this.id_grupo, other.id_grupo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo + " [" + turno.getNombre() + "]";
    }

    public String toString_InscripcionInternet() {
        String s = codigo + " [" + turno.getNombre() + ", Por designar]";
        if (empleado != null) {
            s = codigo + " [" + turno.getNombre() + ", " + empleado.toString() + "]";
        }
        return s;
    }

    public String toString_RegistroParcial() {
        return materia.getNombre() + " [" + codigo + ", " + turno.getNombre() + "]";
    }

    public String toString_Horario() {
        String s = materia.getNombre() + " [Por designar]";
        if (empleado != null) {
            s = materia.getNombre() + " [" + empleado.toString() + "]";
        }
        return s;
    }

    public String toHtml_Horario() {
        String s = "<br/>";

        s += materia.getCodigo() + " [" + codigo + ", " + turno.getNombre() + "]" + "<br/>";
        s += materia.getCarrera().getCodigo() + " [" + materia.getCarrera().getResolucionMinisterial1() + "]" + "<br/>";
        String e = "Por designar";
        if (empleado != null) {
            e = empleado.getPrimerApellido();
        }
        s += e + "<br/>";

        return s;
    }
}
