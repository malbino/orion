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
import org.malbino.orion.entities.Materia;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class MateriaFacade extends AbstractFacade<Materia> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public MateriaFacade() {
        super(Materia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Materia buscarPorCodigo(String codigo) {
        Materia c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo");
            q.setParameter("codigo", codigo);

            c = (Materia) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public Materia buscarPorCodigo(String codigo, int id_materia) {
        Materia c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo AND m.id_materia!=:id_materia");
            q.setParameter("codigo", codigo);
            q.setParameter("id_materia", id_materia);

            c = (Materia) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Materia> listaMaterias(int id_carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera ORDER BY m.nivel, m.numero");
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(int id_carrera, int id_materia) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.id_materia!=:id_materia ORDER BY m.nivel, m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(int id_carrera, Nivel nivel) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.nivel=:nivel ORDER BY m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public long creditajeMaterias(int id_carrera, Nivel nivel) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT SUM(m.creditajeMateria) FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.nivel=:nivel");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> buscar(String keyword, int id_carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND "
                    + "(LOWER(m.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(m.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Nivel> nivelesPendientes(int id_persona, int id_carrera) {
        List<Nivel> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT m.nivel FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.id_materia NOT IN (SELECT m.id_materia FROM Nota n JOIN n.estudiante e JOIN n.materia m JOIN m.carrera c WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND n.condicion=:condicion) ORDER BY m.nivel, m.numero");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("condicion", Condicion.APROBADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMateriaAprobadas(int id_persona, int id_carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Nota n JOIN n.estudiante e JOIN n.materia m JOIN m.carrera c WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND n.condicion=:condicion ORDER BY m.numero");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("condicion", Condicion.APROBADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadMaximaMateriasNivel(int id_carrera) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(m) AS cantidad FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera GROUP BY m.nivel ORDER BY cantidad DESC");
            q.setParameter("id_carrera", id_carrera);
            q.setMaxResults(1);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
    
    public Long cantidadMaterias(int id_carrera) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(m) FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera");
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
}
