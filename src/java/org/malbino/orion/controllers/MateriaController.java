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
import org.malbino.orion.entities.Materia;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.MateriaFacade;

/**
 *
 * @author Tincho
 */
@Named("MateriaController")
@SessionScoped
public class MateriaController extends AbstractController implements Serializable {

    @EJB
    MateriaFacade materiaFacade;

    private List<Materia> materias;
    private Materia nuevaMateria;
    private Materia seleccionMateria;
    private Carrera seleccionCarrera;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        materias = new ArrayList();
        nuevaMateria = new Materia();
        seleccionMateria = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            materias = materiaFacade.listaMaterias(seleccionCarrera.getId_carrera());
        }
        nuevaMateria = new Materia();
        seleccionMateria = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            if (seleccionCarrera != null) {
                materias = materiaFacade.listaMaterias(seleccionCarrera.getId_carrera());
            }
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionCarrera != null) {
            materias = materiaFacade.buscar(keyword, seleccionCarrera.getId_carrera());
        }
    }

    public Nivel[] listaNiveles() {
        return Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
    }

    public List<Materia> listaMateriasCrear() {
        return materiaFacade.listaMaterias(seleccionCarrera.getId_carrera());
    }

    public List<Materia> listaMateriasEditar() {
        return materiaFacade.listaMaterias(seleccionCarrera.getId_carrera(), seleccionMateria.getId_materia());
    }

    public void crearMateria() throws IOException {
        nuevaMateria.setCarrera(seleccionCarrera);
        if (materiaFacade.buscarPorCodigo(nuevaMateria.getCodigo()) == null) {
            if (materiaFacade.create(nuevaMateria)) {
                this.toMaterias();
            } else {
                this.mensajeDeError("No se pudo crear la materia.");
            }
        } else {
            this.mensajeDeError("Materia repetida.");
        }
    }

    public void editarMateria() throws IOException {
        if (materiaFacade.buscarPorCodigo(seleccionMateria.getCodigo(), seleccionMateria.getId_materia()) == null) {
            if (materiaFacade.edit(seleccionMateria)) {
                this.toMaterias();
            } else {
                this.mensajeDeError("No se pudo editar la materia.");
            }
        } else {
            this.mensajeDeError("Materia repetida.");
        }
    }

    public void toNuevaMateria() throws IOException {
        this.redireccionarViewId("/planesEstudio/materia/nuevaMateria");
    }

    public void toEditarMateria() throws IOException {
        this.redireccionarViewId("/planesEstudio/materia/editarMateria");
    }

    public void toMaterias() throws IOException {
        reinit();

        this.redireccionarViewId("/planesEstudio/materia/materias");
    }

    /**
     * @return the materias
     */
    public List<Materia> getMaterias() {
        return materias;
    }

    /**
     * @param materias the materias to set
     */
    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    /**
     * @return the nuevaMateria
     */
    public Materia getNuevaMateria() {
        return nuevaMateria;
    }

    /**
     * @param nuevaMateria the nuevaMateria to set
     */
    public void setNuevaMateria(Materia nuevaMateria) {
        this.nuevaMateria = nuevaMateria;
    }

    /**
     * @return the seleccionMateria
     */
    public Materia getSeleccionMateria() {
        return seleccionMateria;
    }

    /**
     * @param seleccionMateria the seleccionMateria to set
     */
    public void setSeleccionMateria(Materia seleccionMateria) {
        this.seleccionMateria = seleccionMateria;
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
