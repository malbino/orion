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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.malbino.orion.enums.CategoriaAnuncio;
import org.malbino.orion.enums.Contrato;
import org.malbino.orion.enums.Departamento;
import org.malbino.orion.enums.Estado;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "anuncio")
public class Anuncio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_anuncio;

    @Column(unique = true)
    private Long codigo;
    private String titulo;
    private Departamento departamento;
    private CategoriaAnuncio categoriaAnuncio;
    private Integer sueldo;
    private Contrato contrato;
    @Temporal(TemporalType.DATE)
    private Date creacion;
    @Temporal(TemporalType.DATE)
    private Date publicacion;
    @Temporal(TemporalType.DATE)
    private Date vencimiento;
    private Estado estado;
    @Column(length = 20479)
    private String contenido;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_empresa")
    @ManyToOne
    private Empresa empresa;

    public Anuncio() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id_anuncio);
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
        final Anuncio other = (Anuncio) obj;
        if (!Objects.equals(this.id_anuncio, other.id_anuncio)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return titulo + " [" + codigo + "]";
    }

    public String creacion_ddMMyyyy() {
        String s = "";
        if (creacion != null) {
            s = Fecha.formatearFecha_ddMMyyyy(creacion);
        }
        return s;
    }

    public String publicacion_ddMMyyyy() {
        String s = "";
        if (publicacion != null) {
            s = Fecha.formatearFecha_ddMMyyyy(publicacion);
        }
        return s;
    }

    public String vencimiento_ddMMyyyy() {
        String s = "";
        if (vencimiento != null) {
            s = Fecha.formatearFecha_ddMMyyyy(vencimiento);
        }
        return s;
    }

    public String toHTML() {
        String html = null;

        String cabecera
                = "<p style=\"text-align:center\"><span style=\"font-size:18px\"><strong>" + this.getTitulo() + "</strong></span></p>"
                + "<p style=\"text-align:center\"><span style=\"font-size:16px\"><strong>" + this.getCodigo() + "</strong></span></p>"
                + "<p>&nbsp;</p>"
                + "<p>&nbsp;</p>";

        String contenido
                = "<style type=\"text/css\">"
                + "* {"
                + "  font-size: 14px;"
                + "}"
                + "</style>"
                + this.getContenido();
        
        html = cabecera + contenido;

        return html;
    }

    /**
     * @return the id_anuncio
     */
    public Integer getId_anuncio() {
        return id_anuncio;
    }

    /**
     * @param id_anuncio the id_anuncio to set
     */
    public void setId_anuncio(Integer id_anuncio) {
        this.id_anuncio = id_anuncio;
    }

    /**
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
     * @return the sueldo
     */
    public Integer getSueldo() {
        return sueldo;
    }

    /**
     * @param sueldo the sueldo to set
     */
    public void setSueldo(Integer sueldo) {
        this.sueldo = sueldo;
    }

    /**
     * @return the contrato
     */
    public Contrato getContrato() {
        return contrato;
    }

    /**
     * @param contrato the contrato to set
     */
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    /**
     * @return the publicacion
     */
    public Date getPublicacion() {
        return publicacion;
    }

    /**
     * @param publicacion the publicacion to set
     */
    public void setPublicacion(Date publicacion) {
        this.publicacion = publicacion;
    }

    /**
     * @return the vencimiento
     */
    public Date getVencimiento() {
        return vencimiento;
    }

    /**
     * @param vencimiento the vencimiento to set
     */
    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
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
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the categoriaAnuncio
     */
    public CategoriaAnuncio getCategoriaAnuncio() {
        return categoriaAnuncio;
    }

    /**
     * @param categoriaAnuncio the categoriaAnuncio to set
     */
    public void setCategoriaAnuncio(CategoriaAnuncio categoriaAnuncio) {
        this.categoriaAnuncio = categoriaAnuncio;
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the creacion
     */
    public Date getCreacion() {
        return creacion;
    }

    /**
     * @param creacion the creacion to set
     */
    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

}
