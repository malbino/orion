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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.InscritoFacade;

/**
 *
 * @author Tincho
 */
@Named("InscritosController")
@SessionScoped
public class InscritosController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    InscritoFacade inscritoFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private String seleccionParalelo;

    private List<Inscrito> inscritos;
    private Inscrito seleccionInscrito;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionTurno = null;
        seleccionParalelo = null;

        inscritos = new ArrayList<>();
        seleccionInscrito = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica());
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo == null) {
            inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo != null) {
            inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo);
        } else {
            inscritos = new ArrayList<>();
        }
        seleccionInscrito = null;

        filter = false;
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
        Nivel[] niveles = new Nivel[0];
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            niveles = Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
        }
        return niveles;
    }

    @Override
    public Turno[] listaTurnos() {
        Turno[] turnos = new Turno[0];
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null) {
            turnos = Turno.values();
        }
        return turnos;
    }

    public List<String> listaParalelos() {
        List<String> paralelos = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null) {
            paralelos = grupoFacade.listaParalelos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno);
        }
        return paralelos;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            if (seleccionGestionAcademica != null && seleccionCarrera == null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
                inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica());
            } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
                inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
            } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno == null && seleccionParalelo == null) {
                inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel);
            } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo == null) {
                inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno);
            } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo != null) {
                inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo);
            } else {
                inscritos = new ArrayList<>();
            }
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel == null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno == null && seleccionParalelo == null) {
            inscritos = inscritoFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo == null) {
            inscritos = inscritoFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo != null) {
            inscritos = inscritoFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo, keyword);
        }
    }

    public void eliminarInscrito() throws IOException {
        if (inscritoFacade.remove(seleccionInscrito)) {
            this.reinit();
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
     * @return the seleccionParalelo
     */
    public String getSeleccionParalelo() {
        return seleccionParalelo;
    }

    /**
     * @param seleccionParalelo the seleccionParalelo to set
     */
    public void setSeleccionParalelo(String seleccionParalelo) {
        this.seleccionParalelo = seleccionParalelo;
    }

    /**
     * @return the inscritos
     */
    public List<Inscrito> getInscritos() {
        return inscritos;
    }

    /**
     * @param inscritos the inscritos to set
     */
    public void setInscritos(List<Inscrito> inscritos) {
        this.inscritos = inscritos;
    }

    /**
     * @return the seleccionInscrito
     */
    public Inscrito getSeleccionInscrito() {
        return seleccionInscrito;
    }

    /**
     * @param seleccionInscrito the seleccionInscrito to set
     */
    public void setSeleccionInscrito(Inscrito seleccionInscrito) {
        this.seleccionInscrito = seleccionInscrito;
    }

    /**
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
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

}
