
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Empleado;
import org.malbino.orion.enums.NivelAcademico;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.CampusFacade;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.EmpleadoFacade;
import org.primefaces.PrimeFaces;

/**
 *
 * @author malbino
 */
public abstract class AbstractController implements Serializable {

    @EJB
    EmpleadoFacade empleadoFacade;
    @EJB
    CampusFacade campusFacade;
    @EJB
    CarreraFacade carreraFacade;

    protected void mensajeDeError(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, ""));
    }

    protected void mensajeDeInformacion(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, ""));
    }

    protected void insertarParametro(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        extContext.getSessionMap().put(key, value);
    }

    protected Object recuperarParametro(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        return extContext.getSessionMap().get(key);
    }

    protected void eliminarParametro(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        extContext.getSessionMap().remove(key);
    }

    protected void redireccionarViewId(String viewId) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        String url = extContext.encodeActionURL(
                context.getApplication().getViewHandler().getActionURL(context, viewId));
        extContext.redirect(url);
    }

    protected void redireccionarURL(String url) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        extContext.redirect(url);
    }

    protected void invalidateSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();

        extContext.invalidateSession();
    }

    protected String recuperarIP() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
        return request.getRemoteAddr();
    }

    protected String realPath() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return servletContext.getRealPath("");
    }

    protected void ejecutar(String js) {
        PrimeFaces.current().executeScript(js);
    }

    public List<Empleado> completarEmpleado(String consulta) {
        List<Empleado> empleados = empleadoFacade.listaEmpleados();
        List<Empleado> empleadosFiltrados = new ArrayList();

        for (Empleado e : empleados) {
            if (e.toString().toLowerCase().contains(consulta.toLowerCase())) {
                empleadosFiltrados.add(e);
            }
        }

        return empleadosFiltrados;
    }

    public List<Campus> listaCampus() {
        return campusFacade.listaCampus();
    }

    public List<Carrera> listaCarreras() {
        return carreraFacade.listaCarreras();
    }

    public Regimen[] listaRegimenes() {
        return Regimen.values();
    }

    public NivelAcademico[] listaNivelesAcademicos() {
        return NivelAcademico.values();
    }
}
