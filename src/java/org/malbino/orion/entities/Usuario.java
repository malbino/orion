/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "usuario", catalog = "orion", schema = "public")

@PrimaryKeyJoinColumn(name = "id_persona")
@DiscriminatorValue("Usuario")
public class Usuario extends Persona implements Serializable {

    @Column(unique = true)
    private String usuario;
    private String contrasena;

    @Transient
    private String contrasenaSinEncriptar;
    @Transient
    private Boolean resetearContraseña;
    @Transient
    private String pinGenerado;
    @Transient
    private String pinIntroducido;

    @JoinTable(name = "cuenta", catalog = "orion", schema = "public", joinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")}, inverseJoinColumns = {
        @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")})
    @ManyToMany
    private List<Rol> roles;

    public Usuario() {
    }

    public String usuarioMoodle() {
        return this.getUsuario().replaceAll("-", "").toLowerCase();
    }

    public String contraseñaMoodle() {
        return "*M00dle" + this.usuarioMoodle() + "*";
    }
    
    public String apellidos() {
        String apellidos = this.getPrimerApellido();
        if(this.getSegundoApellido() != null) {
            apellidos += " " + this.getSegundoApellido();            
        }
        return apellidos;
    }
    
    public String emailTemporal() {
        return this.usuarioMoodle() + "@cambiatuemail.com";
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the contrasena
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * @param contrasena the contrasena to set
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * @return the contrasenaSinEncriptar
     */
    public String getContrasenaSinEncriptar() {
        return contrasenaSinEncriptar;
    }

    /**
     * @param contrasenaSinEncriptar the contrasenaSinEncriptar to set
     */
    public void setContrasenaSinEncriptar(String contrasenaSinEncriptar) {
        this.contrasenaSinEncriptar = contrasenaSinEncriptar;
    }

    /**
     * @return the resetearContraseña
     */
    public Boolean getResetearContraseña() {
        return resetearContraseña;
    }

    /**
     * @param resetearContraseña the resetearContraseña to set
     */
    public void setResetearContraseña(Boolean resetearContraseña) {
        this.resetearContraseña = resetearContraseña;
    }

    /**
     * @return the pinGenerado
     */
    public String getPinGenerado() {
        return pinGenerado;
    }

    /**
     * @param pinGenerado the pinGenerado to set
     */
    public void setPinGenerado(String pinGenerado) {
        this.pinGenerado = pinGenerado;
    }

    /**
     * @return the pinIntroducido
     */
    public String getPinIntroducido() {
        return pinIntroducido;
    }

    /**
     * @param pinIntroducido the pinIntroducido to set
     */
    public void setPinIntroducido(String pinIntroducido) {
        this.pinIntroducido = pinIntroducido;
    }

    /**
     * @return the roles
     */
    public List<Rol> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
