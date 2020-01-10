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
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class CentralizadorCalificacionesFacade {

    private static final int CANTIDAD_MAXIMA_ESTUDIANTES = 17;
    private static final int CANTIDAD_MAXIMA_MATERIAS = 10;

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Estudiante> listaEstudiantes(int id_gestioncademica, int id_carrera, Nivel nivel, Turno turno, Condicion condicion) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT e FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND n.condicion=:condicion AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("condicion", condicion);
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
    public List<Nota> listaNotas(int id_gestioncademica, int id_carrera, Nivel nivel, Turno turno, Condicion condicion) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.grupo g JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND n.condicion=:condicion AND m.curricular=:curricular AND n.modalidad=:modalidad ORDER BY e.primerApellido, e.segundoApellido, e.nombre, m.numero");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("condicion", condicion);
            //condiciones centralizador
            q.setParameter("curricular", true);
            q.setParameter("modalidad", Modalidad.REGULAR);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Centralizador centralizadorCalificaciones(GestionAcademica gestionAcademica, Carrera carrera, int numeroLibro) {
        Centralizador centralizador = new Centralizador( //centralizador calificaciones
                carrera.getCampus().getInstituto().getUbicacion(),
                carrera.getCampus().getInstituto().getNombreRegulador(),
                carrera.getCampus().getInstituto().getResolucionMinisterial(),
                carrera.getCampus().getInstituto().getCaracter().toString()
        );
        List<PaginaCentralizador> paginasCentralizador = new ArrayList(); //paginas centralizador

        int numeroFolio = 1; //numero folio
        
        //aprobados
        Nivel[] niveles = Nivel.values(carrera.getRegimen());
        for (Nivel nivel : niveles) { //niveles
            Turno[] turnos = Turno.values();
            for (Turno turno : turnos) { //turnos
                int numeroEstudiante = 1;

                List<Estudiante> estudiantes = listaEstudiantes(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel, turno, Condicion.APROBADO);
                Iterator<Estudiante> iteratorEstudiantes = estudiantes.iterator();
                if (!estudiantes.isEmpty()) {
                    //materias centralizador
                    List<Materia> materias = listaMaterias(carrera.getId_carrera(), nivel);
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
                    List<Nota> notas = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel, turno, Condicion.APROBADO);

                    int cantidadPaginas = Redondeo.redondear_UP(((double) estudiantes.size() / CANTIDAD_MAXIMA_ESTUDIANTES), 0).intValue();
                    for (int pagina = 1; pagina <= cantidadPaginas; pagina++) { //paginas
                        //paginas centralziador
                        String codigoRegistro
                                = "AP-"
                                + gestionAcademica.toString() + "-"
                                + carrera.getNivelAcademico().getAbreviatura() + "-"
                                + carrera.getCodigo() + "-"
                                + carrera.getRegimen().getInicial() + "-"
                                + nivel.getAbreviatura() + "-"
                                + pagina;
                        PaginaCentralizador paginaCentralizador = new PaginaCentralizador(
                                codigoRegistro,
                                numeroLibro,
                                numeroFolio,
                                turno.getNombre(),
                                gestionAcademica.toString(),
                                carrera.getNivelAcademico().getNombre(),
                                carrera.getNombre(),
                                carrera.getRegimen().getNombre(),
                                nivel.getNombre()
                        );
                        paginaCentralizador.setMateriasCentralizador(materiasCentralizador);

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
                                        Condicion.APROBADO.toString()
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
                                            if (nota.getRecuperatorio() != null) {
                                                notasEstudianteCentralizador[j] = nota.getRecuperatorio().toString();
                                            } else {
                                                notasEstudianteCentralizador[j] = nota.getNotaFinal().toString();
                                            }
                                        }
                                    } else {
                                        notasEstudianteCentralizador[j] = " ";
                                    }
                                }
                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);

                                numeroEstudiante++;
                            } else {
                                estudianteCentralizador = new EstudianteCentralizador(" ", " ", " ", " ");

                                String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) {
                                    notasEstudianteCentralizador[j] = " ";
                                }
                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);
                            }

                            estudiantesCentralizador[i] = estudianteCentralizador;
                        }
                        paginaCentralizador.setEstudiantesCentralizador(estudiantesCentralizador);

                        paginasCentralizador.add(paginaCentralizador);

                        numeroFolio++;
                    }
                }
            }
        }

        //reprobados
        for (Nivel nivel : niveles) { //niveles
            Turno[] turnos = Turno.values();
            for (Turno turno : turnos) { //turnos
                int numeroEstudiante = 1;

                List<Estudiante> estudiantes = listaEstudiantes(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel, turno, Condicion.REPROBADO);
                Iterator<Estudiante> iteratorEstudiantes = estudiantes.iterator();
                if (!estudiantes.isEmpty()) {
                    //materias centralizador
                    List<Materia> materias = listaMaterias(carrera.getId_carrera(), nivel);
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
                    List<Nota> notas = listaNotas(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel, turno, Condicion.REPROBADO);

                    int cantidadPaginas = Redondeo.redondear_UP(((double) estudiantes.size() / CANTIDAD_MAXIMA_ESTUDIANTES), 0).intValue();
                    for (int pagina = 1; pagina <= cantidadPaginas; pagina++) { //paginas
                        //paginas centralziador
                        String codigoRegistro
                                = "RE-"
                                + gestionAcademica.toString() + "-"
                                + carrera.getNivelAcademico().getAbreviatura() + "-"
                                + carrera.getCodigo() + "-"
                                + carrera.getRegimen().getInicial() + "-"
                                + nivel.getAbreviatura() + "-"
                                + pagina;
                        PaginaCentralizador paginaCentralizador = new PaginaCentralizador(
                                codigoRegistro,
                                numeroLibro,
                                numeroFolio,
                                turno.getNombre(),
                                gestionAcademica.toString(),
                                carrera.getNivelAcademico().getNombre(),
                                carrera.getNombre(),
                                carrera.getRegimen().getNombre(),
                                nivel.getNombre()
                        );
                        paginaCentralizador.setMateriasCentralizador(materiasCentralizador);

                        //estudiantes centralizador
                        EstudianteCentralizador[] estudiantesCentralizador = new EstudianteCentralizador[CANTIDAD_MAXIMA_ESTUDIANTES];
                        for (int i = 0; i < CANTIDAD_MAXIMA_ESTUDIANTES; i++) {
                            EstudianteCentralizador estudianteCentralizador;
                            if (iteratorEstudiantes.hasNext()) {
                                Estudiante estudiante = iteratorEstudiantes.next();
                                estudianteCentralizador = new EstudianteCentralizador(
                                        String.valueOf(numeroEstudiante),
                                        estudiante.toString(),
                                        estudiante.dniLugar()
                                );

                                iteratorMaterias = materias.iterator();
                                String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                int sumatoriaNotaFinal = 0;
                                for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) { //notas
                                    if (iteratorMaterias.hasNext()) {
                                        Materia materia = iteratorMaterias.next();

                                        List<Nota> collect = notas.stream().filter(n -> n.getEstudiante().equals(estudiante) && n.getMateria().equals(materia)).collect(Collectors.toList());
                                        Iterator<Nota> iteratorCollect = collect.iterator();
                                        if (iteratorCollect.hasNext()) {
                                            Nota nota = iteratorCollect.next();
                                            if (nota.getRecuperatorio() != null) {
                                                notasEstudianteCentralizador[j] = nota.getRecuperatorio().toString();
                                                sumatoriaNotaFinal += nota.getRecuperatorio();
                                            } else {
                                                notasEstudianteCentralizador[j] = nota.getNotaFinal().toString();
                                                sumatoriaNotaFinal += nota.getNotaFinal();
                                            }
                                        }
                                    } else {
                                        notasEstudianteCentralizador[j] = " ";
                                    }
                                }
                                if (sumatoriaNotaFinal == 0) { //observacion
                                    estudianteCentralizador.setObservaciones(Condicion.ABANDONO.toString());
                                } else {
                                    estudianteCentralizador.setObservaciones(Condicion.REPROBADO.toString());
                                }

                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);

                                numeroEstudiante++;
                            } else {
                                estudianteCentralizador = new EstudianteCentralizador(" ", " ", " ", " ");

                                String[] notasEstudianteCentralizador = new String[CANTIDAD_MAXIMA_MATERIAS];
                                for (int j = 0; j < CANTIDAD_MAXIMA_MATERIAS; j++) {
                                    notasEstudianteCentralizador[j] = " ";
                                }
                                estudianteCentralizador.setNotas(notasEstudianteCentralizador);
                            }

                            estudiantesCentralizador[i] = estudianteCentralizador;
                        }
                        paginaCentralizador.setEstudiantesCentralizador(estudiantesCentralizador);

                        paginasCentralizador.add(paginaCentralizador);

                        numeroFolio++;
                    }
                }
            }
        }

        centralizador.setPaginasCentralizador(paginasCentralizador);

        return centralizador;
    }

    public class Centralizador {

        private String ubicacion;
        private String institucion;
        private String resolucionMinisterial;
        private String caracter;

        private List<PaginaCentralizador> paginasCentralizador;

        public Centralizador(String ubicacion, String institucion, String resolucionMinisterial, String caracter) {
            this.ubicacion = ubicacion;
            this.institucion = institucion;
            this.resolucionMinisterial = resolucionMinisterial;
            this.caracter = caracter;

            this.paginasCentralizador = new ArrayList();
        }

        /**
         * @return the ubicacion
         */
        public String getUbicacion() {
            return ubicacion;
        }

        /**
         * @param ubicacion the ubicacion to set
         */
        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }

        /**
         * @return the institucion
         */
        public String getInstitucion() {
            return institucion;
        }

        /**
         * @param institucion the institucion to set
         */
        public void setInstitucion(String institucion) {
            this.institucion = institucion;
        }

        /**
         * @return the resolucionMinisterial
         */
        public String getResolucionMinisterial() {
            return resolucionMinisterial;
        }

        /**
         * @param resolucionMinisterial the resolucionMinisterial to set
         */
        public void setResolucionMinisterial(String resolucionMinisterial) {
            this.resolucionMinisterial = resolucionMinisterial;
        }

        /**
         * @return the caracter
         */
        public String getCaracter() {
            return caracter;
        }

        /**
         * @param caracter the caracter to set
         */
        public void setCaracter(String caracter) {
            this.caracter = caracter;
        }

        /**
         * @return the paginasCentralizador
         */
        public List<PaginaCentralizador> getPaginasCentralizador() {
            return paginasCentralizador;
        }

        /**
         * @param paginasCentralizador the paginasCentralizador to set
         */
        public void setPaginasCentralizador(List<PaginaCentralizador> paginasCentralizador) {
            this.paginasCentralizador = paginasCentralizador;
        }
    }

    public class PaginaCentralizador {

        private String codigoRegistro;
        private Integer numeroLibro;
        private Integer numeroFolio;
        private String turno;
        private String gestion;
        private String nivel;
        private String carrera;
        private String regimen;
        private String curso;

        private MateriaCentralizador[] materiasCentralizador;
        private EstudianteCentralizador[] estudiantesCentralizador;

        public PaginaCentralizador(String codigoRegistro, Integer numeroLibro, Integer numeroFolio, String turno, String gestion, String nivel, String carrera, String regimen, String curso) {
            this.codigoRegistro = codigoRegistro;
            this.numeroLibro = numeroLibro;
            this.numeroFolio = numeroFolio;
            this.turno = turno;
            this.gestion = gestion;
            this.nivel = nivel;
            this.carrera = carrera;
            this.regimen = regimen;
            this.curso = curso;

            this.materiasCentralizador = new MateriaCentralizador[CANTIDAD_MAXIMA_MATERIAS];
            this.estudiantesCentralizador = new EstudianteCentralizador[CANTIDAD_MAXIMA_ESTUDIANTES];
        }

        /**
         * @return the codigoRegistro
         */
        public String getCodigoRegistro() {
            return codigoRegistro;
        }

        /**
         * @param codigoRegistro the codigoRegistro to set
         */
        public void setCodigoRegistro(String codigoRegistro) {
            this.codigoRegistro = codigoRegistro;
        }

        /**
         * @return the numeroLibro
         */
        public Integer getNumeroLibro() {
            return numeroLibro;
        }

        /**
         * @param numeroLibro the numeroLibro to set
         */
        public void setNumeroLibro(Integer numeroLibro) {
            this.numeroLibro = numeroLibro;
        }

        /**
         * @return the numeroFolio
         */
        public Integer getNumeroFolio() {
            return numeroFolio;
        }

        /**
         * @param numeroFolio the numeroFolio to set
         */
        public void setNumeroFolio(Integer numeroFolio) {
            this.numeroFolio = numeroFolio;
        }

        /**
         * @return the turno
         */
        public String getTurno() {
            return turno;
        }

        /**
         * @param turno the turno to set
         */
        public void setTurno(String turno) {
            this.turno = turno;
        }

        /**
         * @return the gestion
         */
        public String getGestion() {
            return gestion;
        }

        /**
         * @param gestion the gestion to set
         */
        public void setGestion(String gestion) {
            this.gestion = gestion;
        }

        /**
         * @return the nivel
         */
        public String getNivel() {
            return nivel;
        }

        /**
         * @param nivel the nivel to set
         */
        public void setNivel(String nivel) {
            this.nivel = nivel;
        }

        /**
         * @return the carrera
         */
        public String getCarrera() {
            return carrera;
        }

        /**
         * @param carrera the carrera to set
         */
        public void setCarrera(String carrera) {
            this.carrera = carrera;
        }

        /**
         * @return the regimen
         */
        public String getRegimen() {
            return regimen;
        }

        /**
         * @param regimen the regimen to set
         */
        public void setRegimen(String regimen) {
            this.regimen = regimen;
        }

        /**
         * @return the curso
         */
        public String getCurso() {
            return curso;
        }

        /**
         * @param curso the curso to set
         */
        public void setCurso(String curso) {
            this.curso = curso;
        }

        /**
         * @return the materiasCentralizador
         */
        public MateriaCentralizador[] getMateriasCentralizador() {
            return materiasCentralizador;
        }

        /**
         * @param materiasCentralizador the materiasCentralizador to set
         */
        public void setMateriasCentralizador(MateriaCentralizador[] materiasCentralizador) {
            this.materiasCentralizador = materiasCentralizador;
        }

        /**
         * @return the estudiantesCentralizador
         */
        public EstudianteCentralizador[] getEstudiantesCentralizador() {
            return estudiantesCentralizador;
        }

        /**
         * @param estudiantesCentralizador the estudiantesCentralizador to set
         */
        public void setEstudiantesCentralizador(EstudianteCentralizador[] estudiantesCentralizador) {
            this.estudiantesCentralizador = estudiantesCentralizador;
        }
    }

    public class MateriaCentralizador {

        private String codigo;
        private String nombre;

        public MateriaCentralizador(String codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }

        /**
         * @return the codigo
         */
        public String getCodigo() {
            return codigo;
        }

        /**
         * @param codigo the codigo to set
         */
        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        /**
         * @return the nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public class EstudianteCentralizador {

        private String numero;
        private String nombre;
        private String ci;
        private String[] notas;
        private String observaciones;

        public EstudianteCentralizador(String numero, String nombre, String ci, String observaciones) {
            this.numero = numero;
            this.nombre = nombre;
            this.ci = ci;
            this.notas = notas = new String[CANTIDAD_MAXIMA_MATERIAS];
            this.observaciones = observaciones;
        }

        public EstudianteCentralizador(String numero, String nombre, String ci) {
            this.numero = numero;
            this.nombre = nombre;
            this.ci = ci;
            this.notas = notas = new String[CANTIDAD_MAXIMA_MATERIAS];
        }

        /**
         * @return the numero
         */
        public String getNumero() {
            return numero;
        }

        /**
         * @param numero the numero to set
         */
        public void setNumero(String numero) {
            this.numero = numero;
        }

        /**
         * @return the nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the ci
         */
        public String getCi() {
            return ci;
        }

        /**
         * @param ci the ci to set
         */
        public void setCi(String ci) {
            this.ci = ci;
        }

        /**
         * @return the notas
         */
        public String[] getNotas() {
            return notas;
        }

        /**
         * @param notas the notas to set
         */
        public void setNotas(String[] notas) {
            this.notas = notas;
        }

        /**
         * @return the observaciones
         */
        public String getObservaciones() {
            return observaciones;
        }

        /**
         * @param observaciones the observaciones to set
         */
        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }
    }
}
