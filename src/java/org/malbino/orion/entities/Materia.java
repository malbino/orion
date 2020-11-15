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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "materia", catalog = "orion", schema = "orion", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "carrera"}))
public class Materia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_materia;

    private Integer numero;
    private String codigo;
    private String nombre;
    private Nivel nivel;
    private Integer horas;
    private Integer creditajeMateria;
    private Boolean curricular;

    @JoinTable(name = "prerequisito", catalog = "orion", schema = "orion", joinColumns = {
        @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")}, inverseJoinColumns = {
        @JoinColumn(name = "id_prerequisito", referencedColumnName = "id_materia")})
    @ManyToMany
    private List<Materia> prerequisitos;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    @Transient
    private Grupo grupo;

    public Materia() {
    }

    /**
     * @return the id_materia
     */
    public Integer getId_materia() {
        return id_materia;
    }

    /**
     * @param id_materia the id_materia to set
     */
    public void setId_materia(Integer id_materia) {
        this.id_materia = id_materia;
    }

    /**
     * @return the numero
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nivel
     */
    public Nivel getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the horas
     */
    public Integer getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    /**
     * @return the creditajeMateria
     */
    public Integer getCreditajeMateria() {
        return creditajeMateria;
    }

    /**
     * @param creditajeMateria the creditajeMateria to set
     */
    public void setCreditajeMateria(Integer creditajeMateria) {
        this.creditajeMateria = creditajeMateria;
    }

    /**
     * @return the curricular
     */
    public Boolean getCurricular() {
        return curricular;
    }

    /**
     * @param curricular the curricular to set
     */
    public void setCurricular(Boolean curricular) {
        this.curricular = curricular;
    }

    /**
     * @return the prerequisitos
     */
    public List<Materia> getPrerequisitos() {
        return prerequisitos;
    }

    /**
     * @param prerequisitos the prerequisitos to set
     */
    public void setPrerequisitos(List<Materia> prerequisitos) {
        this.prerequisitos = prerequisitos;
    }

    /**
     * @return the carrera
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
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

    public String curricularToString() {
        return curricular ? "Sí" : "No";
    }

    public String prerequisitosToString() {
        String s = " ";
        for (Materia m : prerequisitos) {
            if (s.compareTo(" ") == 0) {
                s = m.getCodigo();
            } else {
                s += ", " + m.getCodigo();
            }
        }
        return s;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id_materia);
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
        final Materia other = (Materia) obj;
        if (!Objects.equals(this.id_materia, other.id_materia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
    }
}
