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
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.pojos.centralizador.Centralizador;
import org.malbino.orion.pojos.centralizador.EstudianteCentralizador;
import org.malbino.orion.pojos.centralizador.MateriaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaNotas;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class CentralizadorCalificacionesHomologacionFacade {

    private static final int CANTIDAD_MAXIMA_ESTUDIANTES = 20;
    private static final int CANTIDAD_MAXIMA_MATERIAS = 10;

    private static final String TITULO_CC_H = "CENTRALIZADOR DE CALIFICACIONES\nHOMOLOGACIÓN DE ASIGNATURAS - PLAN DE ESTUDIOS ";
    private static final String NOTA_CC_H = "Elaboración de Centralizador de Calificaciones - Homologación de Asignaturas, en cumplimiento a Resolución Ministerial N° 0189/2023";

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public List<Mencion> listaMenciones(Carrera carrera, Nivel nivel) {
        List<Mencion> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT m.mencion FROM Materia m WHERE m.carrera=:carrera AND m.nivel=:nivel ORDER BY m.mencion.nombre");
            q.setParameter("carrera", carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();

            //sin mencion
            if (l.isEmpty()) {
                l.add(null);
            }
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Estudiante> listaEstudiantesHomologacion(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT e FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.HOMOLOGACION);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> listaMaterias(Carrera carrera, Mencion mencion, Nivel nivel) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND m.curricular=:curricular ORDER BY m.numero");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            //condiciones centralizador
            q.setParameter("curricular", true);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Nota> listaNotasHomologacion(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.HOMOLOGACION);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Centralizador centralizadorCalificacionesHomologacion(GestionAcademica gestionAcademica, Carrera carrera, int numeroLibro, int numeroFolio) {
        Centralizador centralizador = new Centralizador( //centralizador calificaciones
                carrera.getCampus().getInstituto().getUbicacion(),
                carrera.getCampus().getInstituto().getNombreRegulador(),
                carrera.getCampus().getInstituto().getResolucionMinisterial(),
                carrera.getCampus().getInstituto().getCaracter().toString()
        );
        List<PaginaCentralizador> paginasCentralizador = new ArrayList(); //paginas centralizador

        //notas
        Nivel[] niveles = Nivel.values(carrera.getRegimen());
        for (Nivel nivel : niveles) { //niveles
            List<Mencion> menciones = listaMenciones(carrera, nivel);

            for (Mencion mencion : menciones) { //menciones
                int numeroEstudiante = 1;

                List<Estudiante> estudiantes = listaEstudiantesHomologacion(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel);
                Iterator<Estudiante> iteratorEstudiantes = estudiantes.iterator();
                if (!estudiantes.isEmpty()) {
                    //materias centralizador
                    List<Materia> materias = listaMaterias(carrera, mencion, nivel);
                    Iterator<Materia> iteratorMaterias = materias.iterator();
                    MateriaCentralizador[] materiasCentralizador = new MateriaCentralizador[CANTIDAD_MAXIMA_MATERIAS];
                    for (int i = 0; i < CANTIDAD_MAXIMA_MATERIAS; i++) {
                        MateriaCentralizador materiaCentralizador;
                        if (iteratorMaterias.hasNext()) {
                            Materia materia = iteratorMaterias.next();
                            materiaCentralizador = new MateriaCentralizador(materia.getCodigo(), materia.getNombre());
                        } else {
                            materiaCentralizador = new MateriaCentralizador(" ", " ");
                        }
                        materiasCentralizador[i] = materiaCentralizador;
                    }

                    //notas
                    List<Nota> notas = listaNotasHomologacion(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel);

                    int cantidadPaginas = Redondeo.redondear_UP(((double) estudiantes.size() / CANTIDAD_MAXIMA_ESTUDIANTES), 0).intValue();
                    for (int pagina = 1; pagina <= cantidadPaginas; pagina++) { //paginas
                        //paginas centralziador
                        String codigoRegistro;
                        if (mencion == null) {
                            codigoRegistro
                                    = "CCH-"
                                    + gestionAcademica.codigo() + "-"
                                    + carrera.getCodigo() + "-"
                                    + nivel.getAbreviatura() + "-"
                                    + pagina;
                        } else {
                            codigoRegistro
                                    = "CCH-"
                                    + gestionAcademica.codigo() + "-"
                                    + carrera.getCodigo() + "-"
                                    + nivel.getAbreviatura() + "-"
                                    + mencion.getCodigo() + "-"
                                    + pagina;
                        }
                        PaginaNotas paginaNotas = new PaginaNotas(
                                codigoRegistro,
                                TITULO_CC_H + carrera.getResolucionMinisterial1(),
                                numeroLibro,
                                numeroFolio,
                                "",
                                gestionAcademica.codigo(),
                                carrera.getNivelAcademico().getNombre(),
                                carrera.getNombre(),
                                carrera.getRegimen().getNombre(),
                                nivel.getOrdinal(),
                                NOTA_CC_H,
                                CANTIDAD_MAXIMA_MATERIAS,
                                CANTIDAD_MAXIMA_ESTUDIANTES
                        );
                        paginaNotas.setMateriasCentralizador(materiasCentralizador);

                        //estudiantes centralizador
                        EstudianteCentralizador[] estudiantesCentralizador = new EstudianteCentralizador[CANTIDAD_MAXIMA_ESTUDIANTES];
                        for (int i = 0; i < CANTIDAD_MAXIMA_ESTUDIANTES; i++) {
                            EstudianteCentralizador estudianteCentralizador;
                            if (iteratorEstudiantes.hasNext()) {
                                Estudiante estudiante = iteratorEstudiantes.next();
                                estudianteCentralizador = new EstudianteCentralizador(
                                        String.valueOf(numeroEstudiante),
                                        estudiante.toString(),
                                        estudiante.dniLugar(),
                                        CANTIDAD_MAXIMA_MATERIAS
                                );

                                iteratorMaterias = materias.iterator();
                                String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) { //notas
                                    if (iteratorMaterias.hasNext()) {
                                        Materia materia = iteratorMaterias.next();

                                        List<Nota> collect = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia)).collect(Collectors.toList());
                                        Iterator<Nota> iteratorCollect = collect.iterator();
                                        if (iteratorCollect.hasNext()) {
                                            Nota nota = iteratorCollect.next();

                                            if (nota.getNotaFinal() != null) {
                                                notasEstudianteCentralizador[j] = nota.getNotaFinal().toString();
                                            } else {
                                                notasEstudianteCentralizador[j] = "";
                                            }
                                        }
                                    } else {
                                        notasEstudianteCentralizador[j] = " ";
                                    }
                                }
                                estudianteCentralizador.setObservaciones(Condicion.APROBADO.toString()); //observacion

                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);

                                numeroEstudiante++;
                            } else {
                                estudianteCentralizador = new EstudianteCentralizador(" ", " ", " ", " ", CANTIDAD_MAXIMA_MATERIAS);

                                String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) {
                                    notasEstudianteCentralizador[j] = " ";
                                }
                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);
                            }

                            estudiantesCentralizador[i] = estudianteCentralizador;
                        }
                        paginaNotas.setEstudiantesCentralizador(estudiantesCentralizador);

                        paginasCentralizador.add(paginaNotas);

                        numeroFolio++;
                    }
                }
            }
        }

        centralizador.setPaginasCentralizador(paginasCentralizador);

        return centralizador;
    }

}
