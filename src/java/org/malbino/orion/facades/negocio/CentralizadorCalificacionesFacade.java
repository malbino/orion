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
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.pojos.centralizador.Centralizador;
import org.malbino.orion.pojos.centralizador.EstudianteCentralizador;
import org.malbino.orion.pojos.centralizador.GrupoCentralizador;
import org.malbino.orion.pojos.centralizador.MateriaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaEstadisticas;
import org.malbino.orion.pojos.centralizador.PaginaNotas;
import org.malbino.orion.util.Redondeo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class CentralizadorCalificacionesFacade {

    private static final Logger log = LoggerFactory.getLogger(CentralizadorCalificacionesFacade.class);

    private static final int CANTIDAD_MAXIMA_ESTUDIANTES = 20;
    private static final int CANTIDAD_MAXIMA_MATERIAS = 10;

    private static final String TITULO_CC = "CENTRALIZADOR DE CALIFICACIONES";
    private static final String TITULO_CC_PR = "CENTRALIZADOR DE CALIFICACIONES\nPRUEBA DE RECUPERACIÓN";
    private static final String NOTA_CC = "NP = No se Presento\nAP = Aprobado\nPRE = Prerequisito";

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

    public List<String> listaParalelos(int id_gestionacademica, int id_carrera, Mencion mencion, Nivel nivel, Turno turno) {
        List<String> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT g.codigo FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND g.turno=:turno ORDER BY g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Estudiante> listaEstudiantes(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel, Turno turno, String paralelo) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT e FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
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
    public List<Estudiante> listaEstudiantesRecuperatorio(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel, Turno turno, String paralelo) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT e FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular AND n.modalidad=:modalidad AND n.recuperatorio IS NOT NULL ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
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
    public List<Grupo> listaGrupos(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel, Turno turno, String paralelo) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular ORDER BY m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);
            //condiciones centralizador
            q.setParameter("curricular", true);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Nota> listaNotas(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel, Turno turno, String paralelo) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
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
    public List<Nota> listaNotas(int id_gestioncademica, int id_carrera, Mencion mencion) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> listaNotas(int id_gestioncademica, int id_carrera, Mencion mencion, Nivel nivel) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Nota> listaNotas(GestionAcademica gestionAcademica, int id_carrera, Mencion mencion, Nivel nivel) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.inicio<:inicio AND c.id_carrera=:id_carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("inicio", gestionAcademica.getInicio());
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("mencion", mencion);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {
            log.error(e.toString());
        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Centralizador centralizadorCalificaciones(GestionAcademica gestionAcademica, Carrera carrera, int numeroLibro, int numeroFolio) {
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
                Turno[] turnos = Turno.values();

                for (Turno turno : turnos) { //turnos
                    List<String> paralelos = listaParalelos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno);

                    for (String paralelo : paralelos) { //paralelos
                        int numeroEstudiante = 1;

                        Integer cantidadInscritos = 0;
                        Integer porcentajeInscritos = 0;
                        Integer cantidadAprobados = 0;
                        Integer porcentajeAprobados = 0;
                        Integer cantidadReprobados = 0;
                        Integer porcentajeReprobados = 0;
                        Integer cantidadAbandonos = 0;
                        Integer porcentajeAbandonos = 0;

                        List<Estudiante> estudiantes = listaEstudiantes(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno, paralelo);
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

                            //grupos
                            List<Grupo> grupos = listaGrupos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno, paralelo);
                            Iterator<Grupo> iteratorGrupos = grupos.iterator();
                            GrupoCentralizador[] gruposCentralizador = new GrupoCentralizador[CANTIDAD_MAXIMA_MATERIAS];
                            for (int i = 0; i < CANTIDAD_MAXIMA_MATERIAS; i++) {
                                GrupoCentralizador grupoCentralizador;
                                if (iteratorGrupos.hasNext()) {
                                    Grupo grupo = iteratorGrupos.next();
                                    if (grupo.getEmpleado() != null) {
                                        grupoCentralizador = new GrupoCentralizador(grupo.getEmpleado().nombreFirma(), grupo.getMateria().getNombre());
                                    } else {
                                        grupoCentralizador = new GrupoCentralizador(" ", grupo.getMateria().getNombre());
                                    }
                                } else {
                                    grupoCentralizador = new GrupoCentralizador(" ", " ");
                                }
                                gruposCentralizador[i] = grupoCentralizador;
                            }

                            //notas
                            List<Nota> notas = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno, paralelo);
                            List<Nota> notasNivel = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel);
                            List<Nota> notasAnteriores = listaNotas(gestionAcademica, carrera.getId_carrera(), mencion, nivel);
                            List<Nota> notasGestionAcademica = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion);

                            int cantidadPaginas = Redondeo.redondear_UP(((double) estudiantes.size() / CANTIDAD_MAXIMA_ESTUDIANTES), 0).intValue();
                            for (int pagina = 1; pagina <= cantidadPaginas; pagina++) { //paginas
                                //paginas centralizador
                                PaginaNotas paginaNotas = new PaginaNotas(
                                        carrera.getCampus().getCodigoRITT(),
                                        TITULO_CC,
                                        numeroLibro,
                                        numeroFolio,
                                        turno.getNombre(),
                                        gestionAcademica.codigo(),
                                        carrera.getNivelAcademico().getNombre(),
                                        carrera.getNombre(),
                                        carrera.getRegimen().getNombre(),
                                        nivel.getOrdinal() + " - PARALELO " + paralelo,
                                        NOTA_CC,
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

                                        List<Nota> notasEstudiante = notas.stream().filter(n -> n.getEstudiante().equals(estudiante)).collect(Collectors.toList());
                                        List<Nota> notasCero = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getNotaFinal() != null && n.getNotaFinal() == 0).collect(Collectors.toList());
                                        List<Nota> notasAprobadas = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getCondicion().equals(Condicion.APROBADO)).collect(Collectors.toList());
                                        List<Nota> notasReprobadas = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getCondicion().equals(Condicion.REPROBADO) && n.getNotaFinal() > 0).collect(Collectors.toList());

                                        List<Nota> notasReprobadasAbandonadasGestionAcademica = notasGestionAcademica.stream().filter(n -> n.getEstudiante().equals(estudiante) && (n.getCondicion() != null && (n.getCondicion().equals(Condicion.REPROBADO) || n.getCondicion().equals(Condicion.ABANDONO)))).collect(Collectors.toList());

                                        //estado
                                        String estado = "";
                                        if (notasCero.size() == notasEstudiante.size()) {
                                            estado = Condicion.ABANDONO.toString();

                                            cantidadAbandonos++;
                                        } else if (notasAprobadas.size() > 0 && notasReprobadas.size() <= gestionAcademica.getModalidadEvaluacion().getCantidadMaximaReprobaciones()) {
                                            estado = Condicion.APROBADO.toString();

                                            cantidadAprobados++;
                                        } else {
                                            estado = Condicion.REPROBADO.toString();

                                            cantidadReprobados++;
                                        }

                                        //observacion
                                        String observacion = "";

                                        String observacionAsignaturasPendientes = "";
                                        for (Nota notaAprobada : notasAprobadas) {
                                            List<Nota> notasAnterioresReprobadasAbandonadas = notasAnteriores.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(notaAprobada.getMateria()) && (n.getCondicion() != null && (n.getCondicion().equals(Condicion.REPROBADO) || n.getCondicion().equals(Condicion.ABANDONO)))).collect(Collectors.toList());

                                            if (!notasAnterioresReprobadasAbandonadas.isEmpty()) {
                                                observacionAsignaturasPendientes = "ASIG. PEND. - REPROBADO " + notasAnterioresReprobadasAbandonadas.get(notasAnterioresReprobadasAbandonadas.size() - 1).getGestionAcademica().codigo();
                                            }
                                        }
                                        if (!observacionAsignaturasPendientes.isEmpty()) {
                                            observacion = observacionAsignaturasPendientes;
                                        }

                                        String observacionAsignaturasReprobadas = "";
                                        for (Nota notaReprobadaAbandonadaGestionAcademica : notasReprobadasAbandonadasGestionAcademica) {
                                            List<Nota> notasAnterioresReprobadasAbandonadas = notasAnteriores.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(notaReprobadaAbandonadaGestionAcademica.getMateria()) && (n.getCondicion() != null && (n.getCondicion().equals(Condicion.REPROBADO) || n.getCondicion().equals(Condicion.ABANDONO)))).collect(Collectors.toList());

                                            if ((notasAnterioresReprobadasAbandonadas.size() + 1) >= 2) {
                                                observacionAsignaturasReprobadas = "REPROBADO " + (notasAnterioresReprobadasAbandonadas.size() + 1) + "ª VEZ - ARTÍCULO 18";
                                            }
                                        }
                                        if (!observacionAsignaturasReprobadas.isEmpty()) {
                                            observacion = observacionAsignaturasReprobadas;
                                        }

                                        estudianteCentralizador = new EstudianteCentralizador(
                                                String.valueOf(numeroEstudiante),
                                                estudiante.toString(),
                                                estudiante.dniLugar(),
                                                estado,
                                                observacion,
                                                CANTIDAD_MAXIMA_MATERIAS
                                        );

                                        iteratorMaterias = materias.iterator();
                                        String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                        for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) { //notas
                                            if (iteratorMaterias.hasNext()) {
                                                Materia materia = iteratorMaterias.next();

                                                List<Nota> notasMateriaParalelo = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia)).collect(Collectors.toList());
                                                Iterator<Nota> iteratorNotasMateriaParalelo = notasMateriaParalelo.iterator();
                                                if (iteratorNotasMateriaParalelo.hasNext()) {
                                                    Nota nota = iteratorNotasMateriaParalelo.next();
                                                    if (nota.getRecuperatorio() != null) {
                                                        notasEstudianteCentralizador[j] = nota.getRecuperatorio().toString();
                                                    } else if (nota.getNotaFinal() != null) {
                                                        if (nota.getNotaFinal() == 0) {
                                                            notasEstudianteCentralizador[j] = "NP";
                                                        } else {
                                                            notasEstudianteCentralizador[j] = nota.getNotaFinal().toString();
                                                        }
                                                    } else {
                                                        notasEstudianteCentralizador[j] = "";
                                                    }

                                                    // libro y folio
                                                    nota.setNumeroLibro(numeroLibro);
                                                    nota.setNumeroFolio(numeroFolio);
                                                    em.merge(nota);
                                                } else {
                                                    List<Nota> notasMateriaNivel = notasNivel.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia)).collect(Collectors.toList());
                                                    Iterator<Nota> iteratorNotasMateriaNivel = notasMateriaNivel.iterator();
                                                    if (iteratorNotasMateriaNivel.hasNext()) {
                                                        notasEstudianteCentralizador[j] = "";
                                                    } else {
                                                        List<Nota> notasAnterioresAprobadas = notasAnteriores.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia) && (n.getCondicion() != null && n.getCondicion().equals(Condicion.APROBADO))).collect(Collectors.toList());
                                                        Iterator<Nota> iteradorNotasAnterioresAprobadas = notasAnterioresAprobadas.iterator();
                                                        if (iteradorNotasAnterioresAprobadas.hasNext()) {
                                                            notasEstudianteCentralizador[j] = "AP";
                                                        } else {
                                                            List<Materia> materiasAprobadas = new ArrayList<>();
                                                            for (Nota notaAnteriorAprobada : notasAnterioresAprobadas) {
                                                                materiasAprobadas.add(notaAnteriorAprobada.getMateria());
                                                            }
                                                            List<Materia> prerequisitos = materia.getPrerequisitos();
                                                            if (!materiasAprobadas.containsAll(prerequisitos)) {
                                                                notasEstudianteCentralizador[j] = "PRE";
                                                            } else {
                                                                notasEstudianteCentralizador[j] = "";
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                notasEstudianteCentralizador[j] = "";
                                            }
                                        }
                                        estudianteCentralizador.setNotas(notasEstudianteCentralizador);

                                        numeroEstudiante++;
                                    } else {
                                        estudianteCentralizador = new EstudianteCentralizador(" ", " ", " ", " ", " ", CANTIDAD_MAXIMA_MATERIAS);

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

                            //estadisticas
                            cantidadInscritos = estudiantes.size();
                            porcentajeInscritos = 100;
                            porcentajeAprobados = Redondeo.redondear_HALFUP(((cantidadAprobados.doubleValue() / cantidadInscritos.doubleValue()) * 100.0), 0).intValue();
                            porcentajeReprobados = Redondeo.redondear_HALFUP(((cantidadReprobados.doubleValue() / cantidadInscritos.doubleValue()) * 100.0), 0).intValue();
                            porcentajeAbandonos = Redondeo.redondear_HALFUP(((cantidadAbandonos.doubleValue() / cantidadInscritos.doubleValue()) * 100.0), 0).intValue();

                            PaginaEstadisticas paginaEstadisticas = new PaginaEstadisticas(
                                    CANTIDAD_MAXIMA_MATERIAS,
                                    cantidadInscritos,
                                    porcentajeInscritos,
                                    cantidadAprobados,
                                    porcentajeAprobados,
                                    cantidadReprobados,
                                    porcentajeReprobados,
                                    cantidadAbandonos,
                                    porcentajeAbandonos
                            );
                            paginaEstadisticas.setGruposCentralizador(gruposCentralizador);

                            paginasCentralizador.add(paginaEstadisticas);
                        }

                        //recuperatorio
                        numeroEstudiante = 1;

                        estudiantes = listaEstudiantesRecuperatorio(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno, paralelo);
                        iteratorEstudiantes = estudiantes.iterator();
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
                            List<Nota> notas = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), mencion, nivel, turno, paralelo);

                            int cantidadPaginas = Redondeo.redondear_UP(((double) estudiantes.size() / CANTIDAD_MAXIMA_ESTUDIANTES), 0).intValue();
                            for (int pagina = 1; pagina <= cantidadPaginas; pagina++) { //paginas
                                //paginas centralziador
                                PaginaNotas paginaNotas = new PaginaNotas(
                                        carrera.getCampus().getCodigoRITT(),
                                        TITULO_CC_PR,
                                        numeroLibro,
                                        numeroFolio,
                                        turno.getNombre(),
                                        gestionAcademica.codigo(),
                                        carrera.getNivelAcademico().getNombre(),
                                        carrera.getNombre(),
                                        carrera.getRegimen().getNombre(),
                                        nivel.getOrdinal() + " - PARALELO " + paralelo,
                                        "",
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

                                                List<Nota> collect = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia) && n.getRecuperatorio() != null).collect(Collectors.toList());
                                                Iterator<Nota> iteratorCollect = collect.iterator();
                                                if (iteratorCollect.hasNext()) {
                                                    Nota nota = iteratorCollect.next();

                                                    if (nota.getNotaFinal() != null) {
                                                        notasEstudianteCentralizador[j] = nota.getNotaFinal().toString();
                                                    } else {
                                                        notasEstudianteCentralizador[j] = "";
                                                    }

                                                    // libro y folio
                                                    nota.setNumeroLibro(numeroLibro);
                                                    nota.setNumeroFolio(numeroFolio);
                                                    em.merge(nota);
                                                }
                                            } else {
                                                notasEstudianteCentralizador[j] = " ";
                                            }
                                        }
                                        estudianteCentralizador.setEstado(Condicion.RECUPERACION.toString()); //estado

                                        estudianteCentralizador.setNotas(notasEstudianteCentralizador);

                                        numeroEstudiante++;
                                    } else {
                                        estudianteCentralizador = new EstudianteCentralizador(" ", " ", " ", " ", " ", CANTIDAD_MAXIMA_MATERIAS);

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
            }
        }

        centralizador.setPaginasCentralizador(paginasCentralizador);

        return centralizador;
    }

}
