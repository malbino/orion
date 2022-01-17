/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Caracter;
import org.malbino.orion.enums.LugarExpedicion;
import org.malbino.orion.enums.Sexo;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "postulante", uniqueConstraints = @UniqueConstraint(columnNames = {"dni", "codigo", "id_gestionacademica", "id_carrera"}))
public class Postulante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_postulante;

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String ci;
    private LugarExpedicion lugarExpedicion;
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    private String lugarNacimiento;
    private String nacionalidad;
    private Sexo sexo;
    private String direccion;
    private Integer telefono;
    private Integer celular;
    private String email;
    private String nombreContacto;
    private Integer celularContacto;
    private String parentescoContacto;
    private String nombreColegio;
    private Caracter caracterColegio;
    private Integer egresoColegio;
    private String foto;

    private String codigo;
    private Date fecha;
    private Boolean diplomaBachiller;
    private String deposito;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    public Postulante() {
    }

    /**
     * @return the id_postulante
     */
    public Integer getId_postulante() {
        return id_postulante;
    }

    /**
     * @param id_postulante the id_postulante to set
     */
    public void setId_postulante(Integer id_postulante) {
        this.id_postulante = id_postulante;
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
        this.nombre = nombre.toUpperCase();
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
        this.primerApellido = primerApellido.toUpperCase();
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
        this.segundoApellido = segundoApellido.toUpperCase();
    }

    /**
     * @return the ci
     */
    public String getCi() {
        return ci;
    }

    /**
     * @param ci the ci to set
     */
    public void setCi(String ci) {
        this.ci = ci.toUpperCase();
    }

    /**
     * @return the lugarExpedicion
     */
    public LugarExpedicion getLugarExpedicion() {
        return lugarExpedicion;
    }

    /**
     * @param lugarExpedicion the lugarExpedicion to set
     */
    public void setLugarExpedicion(LugarExpedicion lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    /**
     * @return the fechaNacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento the fechaNacimiento to set
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * @return the lugarNacimiento
     */
    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    /**
     * @param lugarNacimiento the lugarNacimiento to set
     */
    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento.toUpperCase();
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
        this.nacionalidad = nacionalidad.toUpperCase();
    }

    /**
     * @return the sexo
     */
    public Sexo getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(Sexo sexo) {
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
     * @return the deposito
     */
    public String getDeposito() {
        return deposito;
    }

    /**
     * @param deposito the deposito to set
     */
    public void setDeposito(String deposito) {
        this.deposito = deposito;
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
        this.nombreContacto = nombreContacto.toUpperCase();
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
        this.parentescoContacto = parentescoContacto.toUpperCase();
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
        this.nombreColegio = nombreColegio.toUpperCase();
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

    public String ciLugar() {
        String s = "";
        if (ci != null && lugarExpedicion != null) {
            s = ci + " " + lugarExpedicion;
        } else if (ci != null && lugarExpedicion == null) {
            s = ci;
        }
        return s;
    }

    public String fechaNacimiento_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fechaNacimiento);
    }
    
     public String fecha_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fecha);
    }

    public String diplomaBachillerToString() {
        String s = "";
        if (diplomaBachiller != null) {
            s = diplomaBachiller ? "Sí" : "No";
        }
        return s;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id_postulante);
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
        final Postulante other = (Postulante) obj;
        if (!Objects.equals(this.id_postulante, other.id_postulante)) {
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
