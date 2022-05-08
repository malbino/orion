/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.pojos.seguimiento.EstudianteSeguimiento;
import org.malbino.orion.pojos.seguimiento.NotaSeguimiento;
import org.malbino.orion.pojos.seguimiento.Seguimiento;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class SeguimientoAcademicoFacade {

    private static final int CAN_MAX_MAT = 8;

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Estudiante> listaEstudiantes(int id_gestioncademica, int id_carrera, Nivel nivel, Turno turno, String paralelo) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT e FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> listaMaterias(int id_carrera, Nivel nivel) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.nivel=:nivel AND m.curricular=:curricular ORDER BY m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            //condiciones centralizador
            q.setParameter("curricular", true);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Nota> listaNotas(int id_gestioncademica, int id_carrera, Nivel nivel, Turno turno, String paralelo) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Seguimiento seguimientoAcademico(GestionAcademica seleccionGestionAcademica, Carrera seleccionCarrera, Nivel seleccionNivel, Turno seleccionTurno, String seleccionParalelo) {
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setInstituto(seleccionCarrera.getCampus().getInstituto().getNombreRegulador());
        seguimiento.setGestion(seleccionGestionAcademica.toString());
        seguimiento.setCarrera(seleccionCarrera.toString());
        seguimiento.setNivel(seleccionNivel.toString());
        seguimiento.setTurno(seleccionTurno.toString());
        seguimiento.setParalelo(seleccionParalelo);

        List<Materia> listaMaterias = listaMaterias(seleccionCarrera.getId_carrera(), seleccionNivel);
        String[] materiasSeguimiento = new String[CAN_MAX_MAT];
        for (int i = 0; i < materiasSeguimiento.length; i++) {
            if (i < listaMaterias.size()) {
                Materia materia = listaMaterias.get(i);
                materiasSeguimiento[i] = materia.getCodigo();
            } else {
                materiasSeguimiento[i] = "";
            }
        }
        seguimiento.setMateriasSeguimiento(materiasSeguimiento);

        List<Estudiante> listaEstudiantes = listaEstudiantes(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo);
        List<Nota> listaNotas = listaNotas(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo);
        EstudianteSeguimiento[] estudiantesSeguimiento = new EstudianteSeguimiento[listaEstudiantes.size()];
        for (int i = 0; i < listaEstudiantes.size(); i++) {
            Estudiante estudiante = listaEstudiantes.get(i);

            EstudianteSeguimiento estudianteSeguimiento = new EstudianteSeguimiento();
            estudianteSeguimiento.setNumero(i + 1);
            estudianteSeguimiento.setEstudiante(estudiante.toString());
            estudianteSeguimiento.setObservaciones("");

            NotaSeguimiento[] notasSeguimiento = new NotaSeguimiento[CAN_MAX_MAT];
            for (int j = 0; j < notasSeguimiento.length; j++) {
                if (j < listaMaterias.size()) {
                    Materia materia = listaMaterias.get(j);
                    
                    List<Nota> collect = listaNotas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia)).collect(Collectors.toList());
                    Iterator<Nota> iteratorCollect = collect.iterator();
                    if (iteratorCollect.hasNext()) {
                        Nota nota = iteratorCollect.next();

                        NotaSeguimiento notaSeguimiento = new NotaSeguimiento();
                        if (nota.getNota1() != null) {
                            notaSeguimiento.setNota1(nota.getNota1());
                        }
                        if (nota.getNota2() != null) {
                            notaSeguimiento.setNota2(nota.getNota2());
                        }
                        if (nota.getNota3() != null) {
                            notaSeguimiento.setNota3(nota.getNota3());
                        }
                        if (nota.getNota4() != null) {
                            notaSeguimiento.setNota4(nota.getNota4());
                        }
                        if (nota.getNotaFinal() != null) {
                            notaSeguimiento.setNotaFinal(nota.getNotaFinal());
                        }

                        notasSeguimiento[j] = notaSeguimiento;
                    } else {
                        NotaSeguimiento notaSeguimiento = new NotaSeguimiento();

                        notasSeguimiento[j] = notaSeguimiento;
                    }
                } else {
                    NotaSeguimiento notaSeguimiento = new NotaSeguimiento();

                    notasSeguimiento[j] = notaSeguimiento;
                }
            }
            estudianteSeguimiento.setNotasSeguimiento(notasSeguimiento);
            
            estudiantesSeguimiento[i] = estudianteSeguimiento;
        }
        seguimiento.setEstudiantesSeguimiento(estudiantesSeguimiento);

        return seguimiento;
    }

}
