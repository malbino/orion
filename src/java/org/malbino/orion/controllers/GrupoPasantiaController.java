/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.facades.negocio.ProgramacionGruposPasantiasFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("GrupoPasantiaController")
@SessionScoped
public class GrupoPasantiaController extends AbstractController implements Serializable {

    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;
    @EJB
    MencionFacade mencionFacade;
    @EJB
    ProgramacionGruposPasantiasFacade programacionGruposPasantiasFacade;
    @Inject
    LoginController loginController;

    private List<GrupoPasantia> gruposPasantias;
    private GrupoPasantia seleccionGrupoPasantia;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Mencion seleccionMencion;
    private Turno seleccionTurno;
    private Integer capacidad;

    private String keyword;

    @PostConstruct
    public void init() {
        gruposPasantias = new ArrayList();
        seleccionGrupoPasantia = null;

        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionMencion = null;
        seleccionTurno = null;
        capacidad = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            gruposPasantias = grupoPasantiaFacade.listaGrupoPasantias(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionGrupoPasantia = null;

        seleccionNivel = null;
        seleccionMencion = null;
        seleccionTurno = null;
        capacidad = null;

        keyword = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public Nivel[] listaNiveles() {
        return Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
    }

    public List<Mencion> listaMenciones() {
        return mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            gruposPasantias = grupoPasantiaFacade.buscar(keyword, seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
    }

    public long cantidadNotasGrupoPasantia(GrupoPasantia grupoPasantia) {
        return grupoPasantiaFacade.cantidadNotasPasantiaGrupoPasantia(grupoPasantia.getId_grupopasantia());
    }

    public void programarGrupoPasantias() throws IOException {
        List<GrupoPasantia> gruposPasantia = programacionGruposPasantiasFacade.programarGruposPasantias(seleccionGestionAcademica, seleccionCarrera, seleccionNivel, seleccionMencion, seleccionTurno, capacidad);
        if (!gruposPasantia.isEmpty()) {
            //log
            for (GrupoPasantia grupoPasantia : gruposPasantia) {
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.GRUPO_PASANTIA, grupoPasantia.getId_grupopasantia(), "Creación grupo pasantía por programación de grupos pasantía", loginController.getUsr().toString()));
            }
            
            toGruposPasantias();
        }
    }

    public void editarGrupoPasantia() throws IOException {
        if (grupoPasantiaFacade.edit(seleccionGrupoPasantia)) {
            this.toGruposPasantias();
        }
    }

    public void eliminarGrupoPasantia() throws IOException {
        long cantidadNotasGrupoPasantia = grupoPasantiaFacade.cantidadNotasPasantiaGrupoPasantia(seleccionGrupoPasantia.getId_grupopasantia());
        if (cantidadNotasGrupoPasantia == 0) {
            if (grupoPasantiaFacade.remove(seleccionGrupoPasantia)) {
                this.toGruposPasantias();
            }
        } else {
            this.mensajeDeError("No se puede eliminar grupoPasantias con estudiantes inscritos.");
        }
    }

    public void toProgramarGruposPasantias() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/grupoPasantia/programarGruposPasantias");
    }

    public void toEditarGrupoPasantia() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/grupoPasantia/editarGrupoPasantia");
    }

    public void toGruposPasantias() throws IOException {
        reinit();

        this.redireccionarViewId("/gestionesAcademicas/grupoPasantia/gruposPasantias");
    }

    /**
     * @return the gruposPasantias
     */
    public List<GrupoPasantia> getGruposPasantias() {
        return gruposPasantias;
    }

    /**
     * @param gruposPasantias the gruposPasantias to set
     */
    public void setGruposPasantias(List<GrupoPasantia> gruposPasantias) {
        this.gruposPasantias = gruposPasantias;
    }

    /**
     * @return the seleccionGrupoPasantia
     */
    public GrupoPasantia getSeleccionGrupoPasantia() {
        return seleccionGrupoPasantia;
    }

    /**
     * @param seleccionGrupoPasantia the seleccionGrupoPasantia to set
     */
    public void setSeleccionGrupoPasantia(GrupoPasantia seleccionGrupoPasantia) {
        this.seleccionGrupoPasantia = seleccionGrupoPasantia;
    }

    /**
     * @return the seleccionGestionAcademica
     */
    public GestionAcademica getSeleccionGestionAcademica() {
        return seleccionGestionAcademica;
    }

    /**
     * @param seleccionGestionAcademica the seleccionGestionAcademica to set
     */
    public void setSeleccionGestionAcademica(GestionAcademica seleccionGestionAcademica) {
        this.seleccionGestionAcademica = seleccionGestionAcademica;
    }

    /**
     * @return the seleccionCarrera
     */
    public Carrera getSeleccionCarrera() {
        return seleccionCarrera;
    }

    /**
     * @param seleccionCarrera the seleccionCarrera to set
     */
    public void setSeleccionCarrera(Carrera seleccionCarrera) {
        this.seleccionCarrera = seleccionCarrera;
    }

    /**
     * @return the seleccionNivel
     */
    public Nivel getSeleccionNivel() {
        return seleccionNivel;
    }

    /**
     * @param seleccionNivel the seleccionNivel to set
     */
    public void setSeleccionNivel(Nivel seleccionNivel) {
        this.seleccionNivel = seleccionNivel;
    }

    /**
     * @return the seleccionTurno
     */
    public Turno getSeleccionTurno() {
        return seleccionTurno;
    }

    /**
     * @param seleccionTurno the seleccionTurno to set
     */
    public void setSeleccionTurno(Turno seleccionTurno) {
        this.seleccionTurno = seleccionTurno;
    }

    /**
     * @return the capacidad
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * @param capacidad the capacidad to set
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the seleccionMencion
     */
    public Mencion getSeleccionMencion() {
        return seleccionMencion;
    }

    /**
     * @param seleccionMencion the seleccionMencion to set
     */
    public void setSeleccionMencion(Mencion seleccionMencion) {
        this.seleccionMencion = seleccionMencion;
    }
}
