/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.RegistroDocenteFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Named("SegundoParcialController")
@SessionScoped
public class SegundoParcialController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    RegistroDocenteFacade registroDocenteFacade;
    @EJB
    ActividadFacade actividadFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Grupo seleccionGrupo;
    private List<Nota> notas;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionGrupo = null;
        notas = new ArrayList();
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionGrupo = null;
        notas = new ArrayList();
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public List<Grupo> listaGrupos() {
        List<Grupo> l = new ArrayList();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && loginController.getUsr() != null) {
            l = grupoFacade.listaGruposEmpleado(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), loginController.getUsr().getId_persona());
        }
        return l;
    }

    public void actualizarNotas() {
        if (seleccionGrupo != null) {
            notas = notaFacade.listaNotasGrupo(seleccionGrupo.getId_grupo());
        }
    }

    public void editarNota(Nota nota) {
        if (nota.getSegundoParcial() != null) {
            Integer sum = 0;
            if (nota.getPrimerParcial() != null) {
                sum += nota.getPrimerParcial();
            }
            if (nota.getSegundoParcial() != null) {
                sum += nota.getSegundoParcial();
            }
            if (nota.getTercerParcial() != null) {
                sum += nota.getTercerParcial();
            }
            if (nota.getGrupo().getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
                if (nota.getCuartoParcial() != null) {
                    sum += nota.getCuartoParcial();
                }
            }
            Double promedio = sum.doubleValue() / nota.getGrupo().getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
            Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
            nota.setNotaFinal(promedioRedondeado);

            if (nota.getNotaFinal() >= nota.getGrupo().getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }
    }

    public void guardar() {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.REGISTRO_NOTAS_SEGUNDO_PARCIAL, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (registroDocenteFacade.editarNotas(notas)) {
                actualizarNotas();

                this.mensajeDeInformacion("Guardado.");
            }
        } else {
            actualizarNotas();

            this.mensajeDeError("Fuera de fecha.");
        }
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
     * @return the seleccionGrupo
     */
    public Grupo getSeleccionGrupo() {
        return seleccionGrupo;
    }

    /**
     * @param seleccionGrupo the seleccionGrupo to set
     */
    public void setSeleccionGrupo(Grupo seleccionGrupo) {
        this.seleccionGrupo = seleccionGrupo;
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
}
