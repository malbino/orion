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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "persona", catalog = "orion", schema = "public")

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo")
@DiscriminatorValue("Persona")
public abstract class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_persona;

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    @Column(unique = true)
    private String dni;
    private String lugar;
    @Temporal(TemporalType.DATE)
    private Date fechaAacimiento;
    private String lugarAacimiento;
    private String nacionalidad;
    private String sexo;
    private String direccion;
    private Integer telefono;
    private Integer celular;
    private String email;
    private String foto;

    public Persona() {
    }

    /**
     * @return the id_persona
     */
    public Integer getId_persona() {
        return id_persona;
    }

    /**
     * @param id_persona the id_persona to set
     */
    public void setId_persona(Integer id_persona) {
        this.id_persona = id_persona;
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
     * @return the primerApellido
     */
    public String getPrimerApellido() {
        return primerApellido;
    }

    /**
     * @param primerApellido the primerApellido to set
     */
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    /**
     * @return the segundoApellido
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }

    /**
     * @param segundoApellido the segundoApellido to set
     */
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the lugar
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * @param lugar the lugar to set
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * @return the fechaAacimiento
     */
    public Date getFechaAacimiento() {
        return fechaAacimiento;
    }

    /**
     * @param fechaAacimiento the fechaAacimiento to set
     */
    public void setFechaAacimiento(Date fechaAacimiento) {
        this.fechaAacimiento = fechaAacimiento;
    }

    /**
     * @return the lugarAacimiento
     */
    public String getLugarAacimiento() {
        return lugarAacimiento;
    }

    /**
     * @param lugarAacimiento the lugarAacimiento to set
     */
    public void setLugarAacimiento(String lugarAacimiento) {
        this.lugarAacimiento = lugarAacimiento;
    }

    /**
     * @return the nacionalidad
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * @param nacionalidad the nacionalidad to set
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public Integer getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the celular
     */
    public Integer getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the foto
     */
    public String getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id_persona);
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
        final Persona other = (Persona) obj;
        if (!Objects.equals(this.id_persona, other.id_persona)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getPrimerApellido();
        if (this.getSegundoApellido() != null && !this.getSegundoApellido().isEmpty()) {
            s += " " + this.getSegundoApellido();
        }
        s += " " + this.nombre;
        return s;
    }

}