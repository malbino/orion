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
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "notapasantia", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "id_persona", "id_grupopasantia", "id_empresa"}))
public class NotaPasantia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_notapasantia;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Long codigo;
    private Integer notaTutor;
    private Integer notaEmpresa;
    private Integer notaFinal;
    private Condicion condicion;
    private Boolean contratado;
    private String observaciones;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_grupopasantia")
    @ManyToOne
    private GrupoPasantia grupoPasantia;

    @JoinColumn(name = "id_empresa")
    @ManyToOne
    private Empresa empresa;

    public NotaPasantia() {
    }

    public NotaPasantia(Date fecha, Long codigo, Integer notaTutor, Integer notaEmpresa, Integer notaFinal, Condicion condicion, String observaciones, Estudiante estudiante, GrupoPasantia grupoPasantia, Empresa empresa) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.notaTutor = notaTutor;
        this.notaEmpresa = notaEmpresa;
        this.notaFinal = notaFinal;
        this.condicion = condicion;
        this.observaciones = observaciones;
        this.estudiante = estudiante;
        this.grupoPasantia = grupoPasantia;
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id_notapasantia);
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
        final NotaPasantia other = (NotaPasantia) obj;
        if (!Objects.equals(this.id_notapasantia, other.id_notapasantia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return grupoPasantia.getPasantia().getNombre() + " [" + grupoPasantia.getGestionAcademica().toString() + "]";
    }

    public String fecha_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fecha);
    }

    public String contratadoToString() {
        return contratado ? "Sí" : "No";
    }

    /**
     * @return the id_notapasantia
     */
    public Integer getId_notapasantia() {
        return id_notapasantia;
    }

    /**
     * @param id_notapasantia the id_notapasantia to set
     */
    public void setId_notapasantia(Integer id_notapasantia) {
        this.id_notapasantia = id_notapasantia;
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
     * @return the notaTutor
     */
    public Integer getNotaTutor() {
        return notaTutor;
    }

    /**
     * @param notaTutor the notaTutor to set
     */
    public void setNotaTutor(Integer notaTutor) {
        this.notaTutor = notaTutor;
    }

    /**
     * @return the notaEmpresa
     */
    public Integer getNotaEmpresa() {
        return notaEmpresa;
    }

    /**
     * @param notaEmpresa the notaEmpresa to set
     */
    public void setNotaEmpresa(Integer notaEmpresa) {
        this.notaEmpresa = notaEmpresa;
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
     * @return the grupoPasantia
     */
    public GrupoPasantia getGrupoPasantia() {
        return grupoPasantia;
    }

    /**
     * @param grupoPasantia the grupoPasantia to set
     */
    public void setGrupoPasantia(GrupoPasantia grupoPasantia) {
        this.grupoPasantia = grupoPasantia;
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
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @return the contratado
     */
    public Boolean getContratado() {
        return contratado;
    }

    /**
     * @param contratado the contratado to set
     */
    public void setContratado(Boolean contratado) {
        this.contratado = contratado;
    }

}
