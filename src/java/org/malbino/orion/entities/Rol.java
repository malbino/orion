/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_rol;

    @Column(unique = true)
    private String nombre;

    @JoinTable(name = "privilegio", joinColumns = {
        @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")}, inverseJoinColumns = {
        @JoinColumn(name = "id_recurso", referencedColumnName = "id_recurso")})
    @ManyToMany
    @OrderBy(value = "nombre")
    private List<Recurso> recursos;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;

    public Rol() {
    }

    public String recursosToString() {
        String s = " ";
        for (Recurso r : recursos) {
            if (s.compareTo(" ") == 0) {
                s = r.getNombre();
            } else {
                s += ", " + r.getNombre();
            }
        }
        return s;
    }

    /**
     * @return the id_rol
     */
    public Integer getId_rol() {
        return id_rol;
    }

    /**
     * @param id_rol the id_rol to set
     */
    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
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
     * @return the recursos
     */
    public List<Recurso> getRecursos() {
        return recursos;
    }

    /**
     * @param recursos the recursos to set
     */
    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id_rol);
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
        final Rol other = (Rol) obj;
        if (!Objects.equals(this.id_rol, other.id_rol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
