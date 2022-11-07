
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
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.enums.Caracter;
import org.malbino.orion.enums.Concepto;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Departamento;
import org.malbino.orion.enums.Evaluacion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.LugarExpedicion;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.enums.NivelAcademico;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.enums.Sexo;
import org.malbino.orion.enums.TipoEmpresa;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.CampusFacade;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.EmpleadoFacade;
import org.malbino.orion.facades.EmpresaFacade;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.GestionAcademicaFacade;
import org.malbino.orion.facades.PostulanteFacade;
import org.malbino.orion.facades.UsuarioFacade;
import org.malbino.orion.util.Redondeo;
import org.primefaces.PrimeFaces;

/**
 *
 * @author malbino
 */
public abstract class AbstractController implements Serializable {

    @EJB
    EmpleadoFacade empleadoFacade;
    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    CampusFacade campusFacade;
    @EJB
    CarreraFacade carreraFacade;
    @EJB
    GestionAcademicaFacade gestionAcademicaFacade;
    @EJB
    PostulanteFacade postulanteFacade;
    @EJB
    UsuarioFacade usuarioFacade;
    @EJB
    EmpresaFacade empresaFacade;

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
        List<Empleado> empleados = empleadoFacade.findAll();
        List<Empleado> empleadosFiltrados = new ArrayList();

        for (Empleado e : empleados) {
            if (e.toString().toLowerCase().contains(consulta.toLowerCase())) {
                empleadosFiltrados.add(e);
            }
        }

        return empleadosFiltrados;
    }

    public List<Estudiante> completarEstudiante(String consulta) {
        List<Estudiante> estudiantes = estudianteFacade.findAll();
        List<Estudiante> estudiantesFiltrados = new ArrayList();

        for (Estudiante e : estudiantes) {
            if (e.toString().toLowerCase().contains(consulta.toLowerCase())) {
                estudiantesFiltrados.add(e);
            }
        }

        return estudiantesFiltrados;
    }

    public List<Postulante> completarPostulante(String consulta) {
        List<Postulante> postulantes = postulanteFacade.findAll();
        List<Postulante> postulantesFiltrados = new ArrayList();

        for (Postulante p : postulantes) {
            if (p.toString().toLowerCase().contains(consulta.toLowerCase())) {
                postulantesFiltrados.add(p);
            }
        }

        return postulantesFiltrados;
    }

    public List<Usuario> completarUsuario(String consulta) {
        List<Usuario> usuarios = usuarioFacade.findAll();
        List<Usuario> usuariosFiltrados = new ArrayList();

        for (Usuario u : usuarios) {
            if (u.toString().toLowerCase().contains(consulta.toLowerCase())) {
                usuariosFiltrados.add(u);
            }
        }

        return usuariosFiltrados;
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

    public List<GestionAcademica> listaGestionesAcademicas() {
        return gestionAcademicaFacade.listaGestionAcademica();
    }

    public Turno[] listaTurnos() {
        return Turno.values();
    }

    public Periodo[] listaPeriodos() {
        return Periodo.values();
    }

    public LugarExpedicion[] listaLugaresExpedicion() {
        return LugarExpedicion.values();
    }

    public Sexo[] listaSexos() {
        return Sexo.values();
    }

    public Caracter[] listaCaracteres() {
        return Caracter.values();
    }

    public Funcionalidad[] listaFuncionalidades() {
        return Funcionalidad.values();
    }

    public Modalidad[] listaModalidades() {
        return Modalidad.values();
    }

    public Concepto[] listaConceptos() {
        return Concepto.values();
    }

    public TipoEmpresa[] listaTiposEmpresa() {
        return TipoEmpresa.values();
    }

    public Departamento[] listaDepartamentos() {
        return Departamento.values();
    }

    public List<Empresa> listaEmpresas() {
        return empresaFacade.listaEmpresas();
    }
    
    public Evaluacion[] listaEvaluaciones() {
        return Evaluacion.values();
    }

    public void editarNota(Nota nota) {
        Integer nota1 = 0;
        if (nota.getTeoria1() != null) {
            nota1 += nota.getTeoria1();
        }
        if (nota.getPractica1() != null) {
            nota1 += nota.getPractica1();
        }
        if (nota.getTeoria1() == null && nota.getPractica1() == null) {
            nota.setNota1(null);
        } else {
            nota.setNota1(nota1);
        }

        Integer nota2 = 0;
        if (nota.getTeoria2() != null) {
            nota2 += nota.getTeoria2();
        }
        if (nota.getPractica2() != null) {
            nota2 += nota.getPractica2();
        }
        if (nota.getTeoria2() == null && nota.getPractica2() == null) {
            nota.setNota2(null);
        } else {
            nota.setNota2(nota2);
        }

        Integer nota3 = 0;
        if (nota.getTeoria3() != null) {
            nota3 += nota.getTeoria3();
        }
        if (nota.getPractica3() != null) {
            nota3 += nota.getPractica3();
        }
        if (nota.getTeoria3() == null && nota.getPractica3() == null) {
            nota.setNota3(null);
        } else {
            nota.setNota3(nota3);
        }

        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            Integer nota4 = 0;
            if (nota.getTeoria4() != null) {
                nota4 += nota.getTeoria4();
            }
            if (nota.getPractica4() != null) {
                nota4 += nota.getPractica4();
            }
            if (nota.getTeoria4() == null && nota.getPractica4() == null) {
                nota.setNota4(null);
            } else {
                nota.setNota4(nota4);
            }
        }

        Integer notaFinal = 0;
        if (nota.getNota1() != null) {
            notaFinal += nota.getNota1();
        }
        if (nota.getNota2() != null) {
            notaFinal += nota.getNota2();
        }
        if (nota.getNota3() != null) {
            notaFinal += nota.getNota3();
        }
        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getNota4() != null) {
                notaFinal += nota.getNota4();
            }
        }

        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 3) {
            if (nota.getNota1() == null && nota.getNota2() == null && nota.getNota3() == null) {
                nota.setNotaFinal(null);
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getRecuperatorio() != null) {
                    if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                } else if (nota.getNotaFinal() != null) {
                    if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else if (nota.getNotaFinal() == 0) {
                        nota.setCondicion(Condicion.ABANDONO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                }
            }
        } else if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getNota1() == null && nota.getNota2() == null && nota.getNota3() == null && nota.getNota4() == null) {
                nota.setNotaFinal(null);
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getRecuperatorio() != null) {
                    if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                } else if (nota.getNotaFinal() != null) {
                    if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else if (nota.getNotaFinal() == 0) {
                        nota.setCondicion(Condicion.ABANDONO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                }
            }
        }
    }
}
