/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.malbino.orion.enums.Tipo;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "inscrito", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "numero", "id_persona", "id_carrera", "id_gestionacademica"}))
public class Inscrito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_inscrito;

    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Tipo tipo;

    private Integer codigo;
    private Integer numero;

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    @OneToMany(mappedBy = "inscrito", orphanRemoval = true)
    private List<Nota> notas;

    @OneToMany(mappedBy = "inscrito", orphanRemoval = true)
    private List<Pago> pagos;

    public Inscrito() {
    }

    public Inscrito(Date fecha, Tipo tipo, Integer codigo, Integer numero, Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.codigo = codigo;
        this.numero = numero;
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.gestionAcademica = gestionAcademica;
    }

    /**
     * @return the id_inscrito
     */
    public Integer getId_inscrito() {
        return id_inscrito;
    }

    /**
     * @param id_inscrito the id_inscrito to set
     */
    public void setId_inscrito(Integer id_inscrito) {
        this.id_inscrito = id_inscrito;
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
     * @return the tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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
     * @return the notas
     */
    public List<Nota> getNotas() {
        return notas;
    }

    /**
     * @param notas the notas to set
     */
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    /**
     * @return the pagos
     */
    public List<Pago> getPagos() {
        return pagos;
    }

    /**
     * @param pagos the pagos to set
     */
    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id_inscrito);
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
        final Inscrito other = (Inscrito) obj;
        if (!Objects.equals(this.id_inscrito, other.id_inscrito)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return carrera.toString() + " - " + gestionAcademica.toString();
    }

    public String fecha_ddMMyyyy() {
        return Fecha.formatearFecha_ddMMyyyy(fecha);
    }

    public CarreraEstudiante.CarreraEstudianteId carreraEstudianteId() {
        CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
        carreraEstudianteId.setId_carrera(carrera.getId_carrera());
        carreraEstudianteId.setId_persona(estudiante.getId_persona());

        return carreraEstudianteId;
    }
}
