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
import javax.persistence.Transient;
import org.malbino.orion.enums.Tipo;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "inscrito", catalog = "orion", schema = "public")
public class Inscrito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_inscrito;

    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Tipo tipo;

    private String matricula;
    private String codigo1;
    private String codigo2;
    private String codigo3;
    
    @Transient
    private String codigo1SinEncriptar;
    @Transient
    private String codigo2SinEncriptar;
    @Transient
    private String codigo3SinEncriptar;
    
    

    @JoinColumn(name = "id_persona")
    @ManyToOne
    private Estudiante estudiante;

    @JoinColumn(name = "id_carrera")
    @ManyToOne
    private Carrera carrera;

    @JoinColumn(name = "id_gestionacademica")
    @ManyToOne
    private GestionAcademica gestionAcademica;

    public Inscrito() {
    }

    public Inscrito(Date fecha, Tipo tipo, String matricula, Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.matricula = matricula;
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
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the codigo1
     */
    public String getCodigo1() {
        return codigo1;
    }

    /**
     * @param codigo1 the codigo1 to set
     */
    public void setCodigo1(String codigo1) {
        this.codigo1 = codigo1;
    }

    /**
     * @return the codigo2
     */
    public String getCodigo2() {
        return codigo2;
    }

    /**
     * @param codigo2 the codigo2 to set
     */
    public void setCodigo2(String codigo2) {
        this.codigo2 = codigo2;
    }

    /**
     * @return the codigo3
     */
    public String getCodigo3() {
        return codigo3;
    }

    /**
     * @param codigo3 the codigo3 to set
     */
    public void setCodigo3(String codigo3) {
        this.codigo3 = codigo3;
    }
    
    /**
     * @return the codigo1SinEncriptar
     */
    public String getCodigo1SinEncriptar() {
        return codigo1SinEncriptar;
    }

    /**
     * @param codigo1SinEncriptar the codigo1SinEncriptar to set
     */
    public void setCodigo1SinEncriptar(String codigo1SinEncriptar) {
        this.codigo1SinEncriptar = codigo1SinEncriptar;
    }

    /**
     * @return the codigo2SinEncriptar
     */
    public String getCodigo2SinEncriptar() {
        return codigo2SinEncriptar;
    }

    /**
     * @param codigo2SinEncriptar the codigo2SinEncriptar to set
     */
    public void setCodigo2SinEncriptar(String codigo2SinEncriptar) {
        this.codigo2SinEncriptar = codigo2SinEncriptar;
    }

    /**
     * @return the codigo3SinEncriptar
     */
    public String getCodigo3SinEncriptar() {
        return codigo3SinEncriptar;
    }

    /**
     * @param codigo3SinEncriptar the codigo3SinEncriptar to set
     */
    public void setCodigo3SinEncriptar(String codigo3SinEncriptar) {
        this.codigo3SinEncriptar = codigo3SinEncriptar;
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
}
