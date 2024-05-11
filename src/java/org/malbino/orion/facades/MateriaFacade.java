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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Mencion;
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

    public List<Materia> buscarPorCodigo(String codigo, Carrera carrera, Mencion mencion) {
        List<Materia> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo AND m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion)");
            q.setParameter("codigo", codigo);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> buscarPorCodigo(String codigo, int id_materia, Carrera carrera, Mencion mencion) {
        List<Materia> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo AND m.id_materia!=:id_materia AND m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion)");
            q.setParameter("codigo", codigo);
            q.setParameter("id_materia", id_materia);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(Carrera carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.carrera=:carrera ORDER BY m.nivel, m.mencion, m.numero");
            q.setParameter("carrera", carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(Carrera carrera, Mencion mencion) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) ORDER BY m.nivel, m.mencion, m.numero");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(Carrera carrera, Mencion mencion, int id_materia) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.id_materia!=:id_materia ORDER BY m.nivel, m.mencion, m.numero");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(Carrera carrera, Mencion mencion, Nivel nivel) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.nivel=:nivel ORDER BY m.numero");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
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
                    + "ORDER BY m.nivel, m.mencion, m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Nivel> nivelesPendientes(Estudiante estudiante, Carrera carrera, Mencion mencion) {
        List<Nivel> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT m.nivel FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.id_materia NOT IN (SELECT m.id_materia FROM Nota n JOIN n.materia m WHERE n.estudiante=:estudiante AND m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND n.condicion=:condicion) ORDER BY m.nivel, m.numero");
            q.setParameter("estudiante", estudiante);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
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

    public List<Materia> listaMateriaAprobadas(Estudiante estudiante, Carrera carrera, Mencion mencion) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Nota n JOIN n.materia m WHERE n.estudiante=:estudiante AND m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND n.condicion=:condicion ORDER BY m.nivel, m.mencion, m.numero");
            q.setParameter("estudiante", estudiante);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("condicion", Condicion.APROBADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadMaximaMateriasNivel(Carrera carrera, Mencion mencion) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(m) AS cantidad FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) GROUP BY m.nivel ORDER BY cantidad DESC");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setMaxResults(1);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadMateriasCurriculares(Carrera carrera, Mencion mencion) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(m) FROM Materia m WHERE m.carrera=:carrera AND (m.mencion IS NULL OR m.mencion=:mencion) AND m.curricular = TRUE");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> listaMaterias(int id_prerequisito) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.prerequisitos p WHERE p.id_materia=:id_prerequisito");
            q.setParameter("id_prerequisito", id_prerequisito);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
