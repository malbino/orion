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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.FileEstudianteFacade;

/**
 *
 * @author Tincho
 */
@Named("HistorialAcademicoController")
@SessionScoped
public class HistorialAcademicoController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    NotaFacade notaFacade;
    @EJB
    FileEstudianteFacade fileEstudianteFacade;

    private Estudiante seleccionEstudiante;
    private Carrera seleccionCarrera;
    private List<Nota> historialAcademico;

    private Nota nuevaNota;
    private Nota seleccionNota;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        historialAcademico = new ArrayList();

        nuevaNota = new Nota();
        seleccionNota = null;
    }

    public void reinit() {
        if (seleccionEstudiante != null && seleccionCarrera != null) {
            historialAcademico = notaFacade.historialAcademico(seleccionEstudiante.getId_persona(), seleccionCarrera.getId_carrera());
        }

        nuevaNota = new Nota();
        seleccionNota = null;
    }

    public void sumaNota1() {
        Integer nota1 = 0;
        if (seleccionNota.getTeoria1() != null) {
            nota1 += seleccionNota.getTeoria1();
        }
        if (seleccionNota.getPractica1() != null) {
            nota1 += seleccionNota.getPractica1();
        }
        seleccionNota.setNota1(nota1);
    }

    public void sumaNota2() {
        Integer nota2 = 0;
        if (seleccionNota.getTeoria2() != null) {
            nota2 += seleccionNota.getTeoria2();
        }
        if (seleccionNota.getPractica2() != null) {
            nota2 += seleccionNota.getPractica2();
        }
        seleccionNota.setNota2(nota2);
    }

    public void sumaNota3() {
        Integer nota3 = 0;
        if (seleccionNota.getTeoria3() != null) {
            nota3 += seleccionNota.getTeoria3();
        }
        if (seleccionNota.getPractica3() != null) {
            nota3 += seleccionNota.getPractica3();
        }
        seleccionNota.setNota3(nota3);
    }

    public void sumaNota4() {
        Integer nota4 = 0;
        if (seleccionNota.getTeoria4() != null) {
            nota4 += seleccionNota.getTeoria4();
        }
        if (seleccionNota.getPractica4() != null) {
            nota4 += seleccionNota.getPractica4();
        }
        seleccionNota.setNota4(nota4);
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarrera != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarrera.getRegimen());
        }
        return l;
    }

    public List<Materia> listaMaterias() {
        List<Materia> l = new ArrayList();
        if (seleccionCarrera != null && seleccionEstudiante != null) {
            l = fileEstudianteFacade.oferta(seleccionCarrera, seleccionEstudiante);
        }
        return l;
    }

    @Override
    public Modalidad[] listaModalidades() {
        return Modalidad.values(Boolean.FALSE);
    }

    public void editarParcial() throws IOException {
        if (fileEstudianteFacade.editarParcial(seleccionNota)) {
            toHistorialAcademico();
        }
    }

    public void editarRecuperatorio() throws IOException {
        List<Nota> listaNotasReprobadas = notaFacade.listaNotasReprobadas(seleccionNota.getGestionAcademica().getId_gestionacademica(), seleccionNota.getMateria().getCarrera().getId_carrera(), seleccionNota.getEstudiante().getId_persona());
        if (listaNotasReprobadas.size() <= seleccionNota.getMateria().getCarrera().getRegimen().getCantidadMaximaReprobaciones()) {
            if (seleccionNota.getNotaFinal() != null
                    && seleccionNota.getNotaFinal() >= seleccionNota.getMateria().getCarrera().getRegimen().getNotaMinimmaPruebaRecuperacion()
                    && seleccionNota.getNotaFinal() < seleccionNota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                if (fileEstudianteFacade.editarRecuperatorio(seleccionNota)) {
                    toHistorialAcademico();
                }
            } else {
                this.mensajeDeError("La nota final esta fuera del rango permitido.");
            }
        } else {
            this.mensajeDeError("Las materias reprobadas exceden el maximo permitido.");
        }
    }

    public void crearNota() throws IOException {
        List<Nota> listaNotasMateria = notaFacade.listaNotasMateria(nuevaNota.getGestionAcademica().getId_gestionacademica(), seleccionEstudiante.getId_persona(), nuevaNota.getMateria().getId_materia());
        if (listaNotasMateria.isEmpty()) {
            nuevaNota.setEstudiante(seleccionEstudiante);
            if (fileEstudianteFacade.crearNota(nuevaNota)) {
                toHistorialAcademico();
            }
        } else {
            this.mensajeDeError("Nota repetida.");
        }
    }

    public void editarNota() throws IOException {
        if (fileEstudianteFacade.editarNota(seleccionNota)) {
            toHistorialAcademico();
        }
    }

    public void eliminarNota() throws IOException {
        if (notaFacade.remove(seleccionNota)) {
            toHistorialAcademico();
        }
    }

    public void toHistorialAcademico() throws IOException {
        reinit();

        this.redireccionarViewId("/fileEstudiante/historialAcademico/historialAcademico");
    }

    public void toEditarParcial() throws IOException {
        this.redireccionarViewId("/fileEstudiante/historialAcademico/editarParcial");
    }

    public void toEditarRecuperatorio() throws IOException {
        this.redireccionarViewId("/fileEstudiante/historialAcademico/editarRecuperatorio");
    }

    public void toNuevaNota() throws IOException {
        this.redireccionarViewId("/fileEstudiante/historialAcademico/nuevaNota");
    }

    public void toEditarNota() throws IOException {
        this.redireccionarViewId("/fileEstudiante/historialAcademico/editarNota");
    }

    /**
     * @return the seleccionEstudiante
     */
    public Estudiante getSeleccionEstudiante() {
        return seleccionEstudiante;
    }

    /**
     * @param seleccionEstudiante the seleccionEstudiante to set
     */
    public void setSeleccionEstudiante(Estudiante seleccionEstudiante) {
        this.seleccionEstudiante = seleccionEstudiante;
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
     * @return the historialAcademico
     */
    public List<Nota> getHistorialAcademico() {
        return historialAcademico;
    }

    /**
     * @param historialAcademico the historialAcademico to set
     */
    public void setHistorialAcademico(List<Nota> historialAcademico) {
        this.historialAcademico = historialAcademico;
    }

    /**
     * @return the nuevaNota
     */
    public Nota getNuevaNota() {
        return nuevaNota;
    }

    /**
     * @param nuevaNota the nuevaNota to set
     */
    public void setNuevaNota(Nota nuevaNota) {
        this.nuevaNota = nuevaNota;
    }

    /**
     * @return the seleccionNota
     */
    public Nota getSeleccionNota() {
        return seleccionNota;
    }

    /**
     * @param seleccionNota the seleccionNota to set
     */
    public void setSeleccionNota(Nota seleccionNota) {
        this.seleccionNota = seleccionNota;
    }

}
