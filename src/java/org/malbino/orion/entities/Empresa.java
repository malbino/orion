/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.malbino.orion.enums.Departamento;
import org.malbino.orion.enums.TipoEmpresa;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "empresa")

@PrimaryKeyJoinColumn(name = "id_persona")
@DiscriminatorValue("Empresa")
public class Empresa extends Usuario implements Serializable {

    private String nombreEmpresa;
    private String actividad;
    private TipoEmpresa tipoEmpresa;
    private String nit;
    private Departamento departamento;
    private String provincia;
    private String direccionEmpresa;
    private Integer telefonoEmpresa;
    private Integer celularEmpresa;
    private String emailEmpresa;
    private String paginaEmpresa;
    private String logo;

    public Empresa() {
    }

    @Override
    public String toString() {
        return nombreEmpresa;
    }

    public String nombreContacto() {
        String s = this.getPrimerApellido();
        if (this.getSegundoApellido() != null && !this.getSegundoApellido().isEmpty()) {
            s += " " + this.getSegundoApellido();
        }
        s += " " + this.getNombre();
        return s;
    }

    /**
     * @return the nombreEmpresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * @param nombreEmpresa the nombreEmpresa to set
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * @return the actividad
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * @param actividad the actividad to set
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * @return the tipoEmpresa
     */
    public TipoEmpresa getTipoEmpresa() {
        return tipoEmpresa;
    }

    /**
     * @param tipoEmpresa the tipoEmpresa to set
     */
    public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
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
     * @return the departamento
     */
    public Departamento getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @param provincia the provincia to set
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @return the direccionEmpresa
     */
    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    /**
     * @param direccionEmpresa the direccionEmpresa to set
     */
    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    /**
     * @return the telefonoEmpresa
     */
    public Integer getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    /**
     * @param telefonoEmpresa the telefonoEmpresa to set
     */
    public void setTelefonoEmpresa(Integer telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    /**
     * @return the celularEmpresa
     */
    public Integer getCelularEmpresa() {
        return celularEmpresa;
    }

    /**
     * @param celularEmpresa the celularEmpresa to set
     */
    public void setCelularEmpresa(Integer celularEmpresa) {
        this.celularEmpresa = celularEmpresa;
    }

    /**
     * @return the emailEmpresa
     */
    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    /**
     * @param emailEmpresa the emailEmpresa to set
     */
    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    /**
     * @return the paginaEmpresa
     */
    public String getPaginaEmpresa() {
        return paginaEmpresa;
    }

    /**
     * @param paginaEmpresa the paginaEmpresa to set
     */
    public void setPaginaEmpresa(String paginaEmpresa) {
        this.paginaEmpresa = paginaEmpresa;
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
}
