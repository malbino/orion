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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.negocio.RegistroDocenteFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("PruebaRecuperacionController")
@SessionScoped
public class PruebaRecuperacionController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    RegistroDocenteFacade registroDocenteFacade;
    @EJB
    ActividadFacade actividadFacade;

    private GestionAcademica seleccionGestionAcademica;
    private List<Nota> notas;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        notas = new ArrayList();
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        notas = new ArrayList();
    }

    public void actualizarNotas() {
        if (seleccionGestionAcademica != null) {
            notas = registroDocenteFacade.listaRecuperatorios(seleccionGestionAcademica, loginController.getUsr().getId_persona());
        }
    }

    public void editarNota(Nota nota) {
        if (nota.getRecuperatorio() != null) {
            if (nota.getRecuperatorio() >= nota.getGrupo().getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }
    }

    public void guardar() {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.REGISTRO_NOTAS_PRUEBA_RECUPERACION, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
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
