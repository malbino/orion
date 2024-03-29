/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.enums.Sexo;
import org.malbino.orion.enums.Turno;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class InscritoFacade extends AbstractFacade<Inscrito> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public InscritoFacade() {
        super(Inscrito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Inscrito> listaInscritosPersona(int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona ORDER BY c.nombre, ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Inscrito buscarInscrito(int id_persona, int id_carrera, int id_gestionacademica) {
        Inscrito i = null;

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND ga.id_gestionacademica=:id_gestionacademica");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_gestionacademica", id_gestionacademica);

            i = (Inscrito) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public List<Inscrito> listaInscritosPruebaRecuperacion(GestionAcademica gestionAcademica, int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN n.grupo g JOIN g.empleado e JOIN i.estudiante s WHERE ga.id_gestionacademica=:id_gestionacademica AND e.id_persona=:id_persona AND i.id_inscrito IN (SELECT i.id_inscrito FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND n.notaFinal<:notaMinimaAprobacion GROUP BY i.id_inscrito HAVING COUNT(n) >= 1 AND COUNT(n) <=:cantidadMaximaReprobaciones) ORDER BY s.primerApellido, s.segundoApellido, s.nombre");
            q.setParameter("id_gestionacademica", gestionAcademica.getId_gestionacademica());
            q.setParameter("id_persona", id_persona);
            q.setParameter("notaMinimaAprobacion", gestionAcademica.getModalidadEvaluacion().getNotaMinimaAprobacion());
            q.setParameter("cantidadMaximaReprobaciones", gestionAcademica.getModalidadEvaluacion().getCantidadMaximaReprobaciones());

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritosPruebaRecuperacion(GestionAcademica gestionAcademica, Carrera carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.estudiante s WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND i.id_inscrito IN (SELECT i.id_inscrito FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND n.notaFinal<:notaMinimaAprobacion GROUP BY i.id_inscrito HAVING COUNT(n) >= 1 AND COUNT(n) <=:cantidadMaximaReprobaciones) ORDER BY s.primerApellido, s.segundoApellido, s.nombre");
            q.setParameter("id_gestionacademica", gestionAcademica.getId_gestionacademica());
            q.setParameter("id_carrera", carrera.getId_carrera());
            q.setParameter("notaMinimaAprobacion", gestionAcademica.getModalidadEvaluacion().getNotaMinimaAprobacion());
            q.setParameter("cantidadMaximaReprobaciones", gestionAcademica.getModalidadEvaluacion().getCantidadMaximaReprobaciones());

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica ORDER BY i.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY i.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera, Nivel nivel) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.grupo g JOIN g.materia m JOIN m.carrera c JOIN g.gestionAcademica ga JOIN n.inscrito i JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.grupo g JOIN g.materia m JOIN m.carrera c JOIN g.gestionAcademica ga JOIN n.inscrito i JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno, String paralelo) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.grupo g JOIN g.materia m JOIN m.carrera c JOIN g.gestionAcademica ga JOIN n.inscrito i JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritosCarrera(int id_gestionacademica, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritosPorEstudianteCarrera(int id_persona, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera ORDER BY ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Integer maximoNumero(int id_gestioncademica, int id_carrera) {
        Integer i = null;

        try {
            Query q = em.createQuery("SELECT MAX(i.numero) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            i = (Integer) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public Long maximoCodigo(int id_gestioncademica, int id_carrera) {
        Long l = null;

        try {
            Query q = em.createQuery("SELECT MAX(i.codigo) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadInscritos() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE");

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadInscritos(Sexo sexo) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.estudiante e WHERE ga.vigente=TRUE AND e.sexo=:sexo");
            q.setParameter("sexo", sexo);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadInscritosEfectivos() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(i)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE AND n.notaFinal!=0");

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadInscritosReprobados() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(i)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE AND n.condicion=:condicion");
            q.setParameter("condicion", Condicion.REPROBADO);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadInscritos(int id_gestionacademica, int id_carrera) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> buscar(int id_gestionacademica, String keyword) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND "
                    + "(CAST(i.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Inscrito> buscar(int id_gestionacademica, int id_carrera, String keyword) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND "
                    + "(CAST(i.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Inscrito> buscar(int id_gestionacademica, int id_carrera, Nivel nivel, String keyword) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Inscrito i JOIN i.estudiante e JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.notas n JOIN n.grupo g JOIN g.materia m  WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND "
                    + "(CAST(i.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Inscrito> buscar(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno, String keyword) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Inscrito i JOIN i.estudiante e JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.notas n JOIN n.grupo g JOIN g.materia m  WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND "
                    + "(CAST(i.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Inscrito> buscar(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno, String paralelo, String keyword) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Inscrito i JOIN i.estudiante e JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.notas n JOIN n.grupo g JOIN g.materia m  WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND "
                    + "(CAST(i.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Inscrito> listaInscritosMulticarrera(GestionAcademica gestionAcademica) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND e.id_persona IN (SELECT e.id_persona FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica GROUP BY e.id_persona HAVING COUNT(e) > 1) ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", gestionAcademica.getId_gestionacademica());

            l = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_carrera, Nivel nivel) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT(i) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN n.materia m JOIN i.estudiante e WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera, Nivel nivel, String paralelo) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.grupo g JOIN g.materia m JOIN m.carrera c JOIN g.gestionAcademica ga JOIN n.inscrito i JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.codigo=:paralelo ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("paralelo", paralelo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Inscrito> listaInscritos(Integer gestion, Periodo periodo) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.gestion=:gestion AND ga.periodo=:periodo ORDER BY i.id_inscrito");
            q.setParameter("gestion", gestion);
            q.setParameter("periodo", periodo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
