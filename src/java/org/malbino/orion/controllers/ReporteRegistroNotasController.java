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
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.enums.TipoNota;

/**
 *
 * @author Tincho
 */
@Named("ReporteRegistroNotasController")
@SessionScoped
public class ReporteRegistroNotasController extends AbstractController implements Serializable {

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private TipoNota seleccionTipoNota;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionTipoNota = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionTipoNota = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public TipoNota[] listaTiposNota() {
        TipoNota[] a = new TipoNota[0];
        if (seleccionGestionAcademica != null) {
            a = Arrays.stream(TipoNota.values()).filter(tipoNota -> tipoNota.getRegimen().equals(seleccionGestionAcademica.getRegimen())).toArray(TipoNota[]::new);
        }
        return a;
    }

    public void generarReporte() throws IOException {
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionTipoNota != null) {
            this.insertarParametro("id_gestionacademica", seleccionGestionAcademica.getId_gestionacademica());
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());
            this.insertarParametro("tipoNota", seleccionTipoNota);

            toRegistroNotas();
        }
    }

    public void toReporteRegistroNotas() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/registroNotas/reporteRegistroNotas");
    }

    public void toRegistroNotas() throws IOException {
        this.redireccionarViewId("/reportes/registroNotas/registroNotas");
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
     * @return the seleccionTipoNota
     */
    public TipoNota getSeleccionTipoNota() {
        return seleccionTipoNota;
    }

    /**
     * @param seleccionTipoNota the seleccionTipoNota to set
     */
    public void setSeleccionTipoNota(TipoNota seleccionTipoNota) {
        this.seleccionTipoNota = seleccionTipoNota;
    }

}