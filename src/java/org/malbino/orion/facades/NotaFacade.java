/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class NotaFacade extends AbstractFacade<Nota> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public NotaFacade() {
        super(Nota.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Nota> listaNotas(int id_inscrito) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.inscrito i JOIN n.materia m WHERE i.id_inscrito=:id_inscrito ORDER BY m.nivel, m.numero");
            q.setParameter("id_inscrito", id_inscrito);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> historialAcademico(int id_persona, int id_carrera) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.estudiante e JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera ORDER BY ga.gestion, ga.periodo, m.nivel, m.numero");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> listaNotasGrupo(int id_grupo) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.grupo g JOIN n.estudiante e WHERE g.id_grupo=:id_grupo ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_grupo", id_grupo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> buscar(String keyword, int id_grupo) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.grupo g JOIN n.estudiante e WHERE g.id_grupo=:id_grupo AND "
                    + "(LOWER(e.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_grupo", id_grupo);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> listaNotasPruebaRecuperacion(Inscrito inscrito, int id_persona) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.inscrito i JOIN n.grupo g JOIN g.empleado e JOIN n.materia m WHERE i.id_inscrito=:id_inscrito AND e.id_persona=:id_persona AND n.notaFinal>=:notaMinimaPruebaRecuperacion AND n.notaFinal<:notaMinimaAprobacion ORDER BY m.nivel, m.numero");
            q.setParameter("id_inscrito", inscrito.getId_inscrito());
            q.setParameter("id_persona", id_persona);
            q.setParameter("notaMinimaPruebaRecuperacion", inscrito.getCarrera().getRegimen().getNotaMinimmaPruebaRecuperacion());
            q.setParameter("notaMinimaAprobacion", inscrito.getCarrera().getRegimen().getNotaMinimaAprobacion());

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> listaNotasReprobadas(int id_gestionacademica, int id_carrera, int id_persona) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND e.id_persona=:id_persona AND n.condicion=:condicion ORDER BY m.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);
            q.setParameter("condicion", Condicion.REPROBADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> listaNotasMateria(int id_gestionacademica, int id_persona, int id_materia) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN n.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND e.id_persona=:id_persona AND m.id_materia=:id_materia ORDER BY m.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Nota> reporteHistorialAcademico(int id_persona, int id_carrera) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.estudiante e JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND m.curricular=TRUE AND n.condicion=:condicion ORDER BY ga.gestion, ga.periodo, m.nivel, m.numero");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("condicion", Condicion.APROBADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public GestionAcademica inicioFormacion(int id_carrera, int id_persona) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT DISTINCT ga FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE c.id_carrera=:id_carrera AND e.id_persona=:id_persona AND m.curricular=TRUE AND n.condicion=:condicion ORDER BY ga.gestion, ga.periodo");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);
            q.setParameter("condicion", Condicion.APROBADO);
            q.setMaxResults(1);

            ga = (GestionAcademica) q.getSingleResult();
        } catch (Exception e) {

        }

        return ga;
    }

    public GestionAcademica finFormacion(int id_carrera, int id_persona) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT DISTINCT ga FROM Nota n JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE c.id_carrera=:id_carrera AND e.id_persona=:id_persona AND m.curricular=TRUE AND n.condicion=:condicion ORDER BY ga.gestion DESC, ga.periodo DESC");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);
            q.setParameter("condicion", Condicion.APROBADO);
            q.setMaxResults(1);

            ga = (GestionAcademica) q.getSingleResult();
        } catch (Exception e) {

        }

        return ga;
    }

    public Long cantidadNotasAprobadas(int id_carrera, int id_persona) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(n) FROM Nota n JOIN n.materia m JOIN m.carrera c JOIN n.estudiante e WHERE c.id_carrera=:id_carrera AND e.id_persona=:id_persona AND m.curricular=TRUE AND n.condicion=:condicion");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);
            q.setParameter("condicion", Condicion.APROBADO);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Double promedioReporteHistorialAcademico(int id_persona, int id_carrera) {
        Double d = 0.0;

        try {
            Query q = em.createQuery("SELECT AVG(COALESCE(n.recuperatorio, n.notaFinal)) FROM Nota n JOIN n.estudiante e JOIN n.gestionAcademica ga JOIN n.materia m JOIN m.carrera c WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND m.curricular=TRUE AND n.condicion=:condicion");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("condicion", Condicion.APROBADO);

            d = (Double) q.getSingleResult();
        } catch (Exception e) {

        }

        return d;
    }

    public Date fechaInicio(int id_persona) {
        Date d = null;

        try {
            Query q = em.createQuery("SELECT MIN(ga.inicio) FROM Nota n JOIN n.gestionAcademica ga JOIN n.estudiante e WHERE e.id_persona=:id_persona");
            q.setParameter("id_persona", id_persona);

            d = (Date) q.getSingleResult();
        } catch (Exception e) {

        }

        return d;
    }
}
