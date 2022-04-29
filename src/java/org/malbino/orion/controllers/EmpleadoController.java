/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Empleado;
import org.malbino.orion.facades.EmpleadoFacade;

/**
 *
 * @author Tincho
 */
@Named("EmpleadoController")
@SessionScoped
public class EmpleadoController extends AbstractController implements Serializable {

    @EJB
    EmpleadoFacade empleadoFacade;

    private List<Empleado> empleados;
    private Empleado nuevoEmpleado;
    private Empleado seleccionEmpleado;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        empleados = empleadoFacade.listaEmpleados();
        nuevoEmpleado = new Empleado();
        seleccionEmpleado = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        empleados = empleadoFacade.listaEmpleados();
        nuevoEmpleado = new Empleado();
        seleccionEmpleado = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            empleados = empleadoFacade.listaEmpleados();
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        empleados = empleadoFacade.buscar(keyword);
    }

    public void crearEmpleado() throws IOException {
        if (empleadoFacade.buscarPorDni(nuevoEmpleado.getDni()) == null) {
            if (empleadoFacade.create(nuevoEmpleado)) {
                this.toEmpleados();
            }
        } else {
            this.mensajeDeError("Empleado repetido.");
        }
    }

    public void editarEmpleado() throws IOException {
        if (empleadoFacade.buscarPorDni(seleccionEmpleado.getDni(), seleccionEmpleado.getId_persona()) == null) {
            if (empleadoFacade.edit(seleccionEmpleado)) {
                this.toEmpleados();
            }
        } else {
            this.mensajeDeError("Empleado repetido.");
        }
    }

    public void toNuevoEmpleado() throws IOException {
        this.redireccionarViewId("/administrador/empleado/nuevoEmpleado");
    }

    public void toEditarEmpleado() throws IOException {
        this.redireccionarViewId("/administrador/empleado/editarEmpleado");
    }

    public void toEmpleados() throws IOException {
        reinit();
        
        this.redireccionarViewId("/administrador/empleado/empleados");
    }

    /**
     * @return the empleados
     */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /**
     * @param empleados the empleados to set
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    /**
     * @return the crearEmpleado
     */
    public Empleado getNuevoEmpleado() {
        return nuevoEmpleado;
    }

    /**
     * @param nuevoEmpleado the crearEmpleado to set
     */
    public void setNuevoEmpleado(Empleado nuevoEmpleado) {
        this.nuevoEmpleado = nuevoEmpleado;
    }

    /**
     * @return the seleccionEmpleado
     */
    public Empleado getSeleccionEmpleado() {
        return seleccionEmpleado;
    }

    /**
     * @param seleccionEmpleado the seleccionEmpleado to set
     */
    public void setSeleccionEmpleado(Empleado seleccionEmpleado) {
        this.seleccionEmpleado = seleccionEmpleado;
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
