/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;

/**
 *
 * @author Tincho
 */
@Named("EstudianteNuevoPostulacionController")
@SessionScoped
public class EstudianteNuevoPostulacionController extends AbstractController implements Serializable {

    @EJB
    InscripcionesFacade inscripcionesFacade;
    @EJB
    ActividadFacade actividadFacade;
    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;

    private Postulante seleccionPostulante;
    private Estudiante nuevoEstudiante;
    private GestionAcademica seleccionGestionAcademica;
    private Boolean traspasoConvalidacion;
    private CarreraEstudiante seleccionCarreraEstudiante;

    private Comprobante nuevoComprobante;

    @PostConstruct
    public void init() {
        seleccionPostulante = null;
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        traspasoConvalidacion = Boolean.FALSE;
        seleccionCarreraEstudiante = null;

        nuevoComprobante = new Comprobante();
    }

    public void reinit() {
        seleccionPostulante = null;
        nuevoEstudiante = new Estudiante();
        seleccionGestionAcademica = null;
        traspasoConvalidacion = Boolean.FALSE;
        seleccionCarreraEstudiante = null;

        nuevoComprobante = new Comprobante();
    }

    public void cargarPostulante() {
        if (seleccionPostulante != null) {
            nuevoEstudiante.setNombre(seleccionPostulante.getNombre());
            nuevoEstudiante.setPrimerApellido(seleccionPostulante.getPrimerApellido());
            nuevoEstudiante.setSegundoApellido(seleccionPostulante.getSegundoApellido());
            nuevoEstudiante.setDni(seleccionPostulante.getCi());
            nuevoEstudiante.setLugarExpedicion(seleccionPostulante.getLugarExpedicion());
            nuevoEstudiante.setFechaNacimiento(seleccionPostulante.getFechaNacimiento());
            nuevoEstudiante.setLugarNacimiento(seleccionPostulante.getLugarNacimiento());
            nuevoEstudiante.setNacionalidad(seleccionPostulante.getNacionalidad());
            nuevoEstudiante.setSexo(seleccionPostulante.getSexo());
            nuevoEstudiante.setDireccion(seleccionPostulante.getDireccion());
            nuevoEstudiante.setTelefono(seleccionPostulante.getTelefono());
            nuevoEstudiante.setCelular(seleccionPostulante.getCelular());
            nuevoEstudiante.setEmail(seleccionPostulante.getEmail());
            nuevoEstudiante.setNombreContacto(seleccionPostulante.getNombreContacto());
            nuevoEstudiante.setCelularContacto(seleccionPostulante.getCelularContacto());
            nuevoEstudiante.setParentescoContacto(seleccionPostulante.getParentescoContacto());
            nuevoEstudiante.setNombreColegio(seleccionPostulante.getNombreColegio());
            nuevoEstudiante.setCaracterColegio(seleccionPostulante.getCaracterColegio());
            nuevoEstudiante.setEgresoColegio(seleccionPostulante.getEgresoColegio());
            nuevoEstudiante.setFecha(Fecha.getDate());
            nuevoEstudiante.setDiplomaBachiller(seleccionPostulante.getDiplomaBachiller());
            nuevoEstudiante.setFoto(seleccionPostulante.getFoto());

            seleccionGestionAcademica = seleccionPostulante.getGestionAcademica();
            CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
            carreraEstudianteId.setId_carrera(seleccionPostulante.getCarrera().getId_carrera());
            carreraEstudianteId.setId_persona(0);
            CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
            carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
            carreraEstudiante.setCarrera(seleccionPostulante.getCarrera());
            seleccionCarreraEstudiante = carreraEstudiante;
        }
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList<>();
        if (seleccionGestionAcademica != null) {
            List<Carrera> carreras = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
            for (Carrera carrera : carreras) {
                List<Mencion> menciones = mencionFacade.listaMenciones(carrera.getId_carrera());
                if (menciones.isEmpty()) {
                    if (!traspasoConvalidacion) {
                        CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                        carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                        carreraEstudianteId.setId_persona(0);
                        CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                        carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                        carreraEstudiante.setCarrera(carrera);

                        l.add(carreraEstudiante);
                    } else {
                        Nivel[] niveles = Nivel.values(carrera.getRegimen());
                        for (int i = 1; i < niveles.length; i++) {
                            Nivel nivel = niveles[i];

                            CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                            carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                            carreraEstudianteId.setId_persona(0);
                            CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                            carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                            carreraEstudiante.setCarrera(carrera);
                            carreraEstudiante.setNivelInicio(nivel);

                            l.add(carreraEstudiante);
                        }
                    }
                } else {
                    for (Mencion mencion : menciones) {
                        if (!traspasoConvalidacion) {
                            CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                            carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                            carreraEstudianteId.setId_persona(0);
                            CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                            carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                            carreraEstudiante.setMencion(mencion);
                            carreraEstudiante.setCarrera(carrera);

                            l.add(carreraEstudiante);
                        } else {
                            Nivel[] niveles = Nivel.values(carrera.getRegimen());
                            for (int i = 1; i < niveles.length; i++) {
                                Nivel nivel = niveles[i];

                                CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                                carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                                carreraEstudianteId.setId_persona(0);
                                CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                                carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                                carreraEstudiante.setMencion(mencion);
                                carreraEstudiante.setCarrera(carrera);
                                carreraEstudiante.setNivelInicio(nivel);

                                l.add(carreraEstudiante);
                            }
                        }

                    }
                }
            }
        }
        return l;
    }

    public void registrarEstudiante() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (estudianteFacade.buscarPorDni(nuevoEstudiante.getDni()) == null) {
                nuevoComprobante.setFecha(Fecha.getDate());
                nuevoComprobante.setValido(true);
                nuevoComprobante.setUsuario(loginController.getUsr());

                String contrasena = Generador.generarContrasena();
                nuevoEstudiante.setContrasena(Encriptador.encriptar(contrasena));
                nuevoEstudiante.setContrasenaSinEncriptar(contrasena);

                if (inscripcionesFacade.registrarEstudianteNuevo(nuevoEstudiante, seleccionCarreraEstudiante, seleccionGestionAcademica, nuevoComprobante)) {
                    this.insertarParametro("id_comprobante", nuevoComprobante.getId_comprobante());
                    this.insertarParametro("est", nuevoEstudiante);

                    reinit();

                    this.toComprobantePago();
                } else {
                    this.mensajeDeError("No se pudo registrar al estudiante.");
                }
            } else {
                this.mensajeDeError("Estudiante repetido.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void toEstudianteNuevoPostulacion() throws IOException {
        this.redireccionarViewId("/inscripciones/estudianteNuevoPostulacion/estudianteNuevoPostulacion");
    }

    public void toComprobantePago() throws IOException {
        this.redireccionarViewId("/inscripciones/estudianteNuevoPostulacion/comprobantePago");
    }

    /**
     * @return the nuevoEstudiante
     */
    public Estudiante getNuevoEstudiante() {
        return nuevoEstudiante;
    }

    /**
     * @param nuevoEstudiante the nuevoEstudiante to set
     */
    public void setNuevoEstudiante(Estudiante nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
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
     * @return the seleccionCarreraEstudiante
     */
    public CarreraEstudiante getSeleccionCarreraEstudiante() {
        return seleccionCarreraEstudiante;
    }

    /**
     * @param seleccionCarreraEstudiante the seleccionCarreraEstudiante to set
     */
    public void setSeleccionCarreraEstudiante(CarreraEstudiante seleccionCarreraEstudiante) {
        this.seleccionCarreraEstudiante = seleccionCarreraEstudiante;
    }

    /**
     * @return the nuevoComprobante
     */
    public Comprobante getNuevoComprobante() {
        return nuevoComprobante;
    }

    /**
     * @param nuevoComprobante the nuevoComprobante to set
     */
    public void setNuevoComprobante(Comprobante nuevoComprobante) {
        this.nuevoComprobante = nuevoComprobante;
    }

    /**
     * @return the seleccionPostulante
     */
    public Postulante getSeleccionPostulante() {
        return seleccionPostulante;
    }

    /**
     * @param seleccionPostulante the seleccionPostulante to set
     */
    public void setSeleccionPostulante(Postulante seleccionPostulante) {
        this.seleccionPostulante = seleccionPostulante;
    }

    /**
     * @return the traspasoConvalidacion
     */
    public Boolean getTraspasoConvalidacion() {
        return traspasoConvalidacion;
    }

    /**
     * @param traspasoConvalidacion the traspasoConvalidacion to set
     */
    public void setTraspasoConvalidacion(Boolean traspasoConvalidacion) {
        this.traspasoConvalidacion = traspasoConvalidacion;
    }
}
