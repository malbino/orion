
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
import org.malbino.orion.entities.Pasantia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class PasantiaFacade extends AbstractFacade<Pasantia> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public PasantiaFacade() {
        super(Pasantia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Pasantia> buscarPorCodigo(String codigo, Carrera carrera, Mencion mencion) {
        List<Pasantia> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.codigo=:codigo AND p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion)");
            q.setParameter("codigo", codigo);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> buscarPorCodigo(String codigo, int id_pasantia, Carrera carrera, Mencion mencion) {
        List<Pasantia> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.codigo=:codigo AND p.id_pasantia!=:id_pasantia AND p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion)");
            q.setParameter("codigo", codigo);
            q.setParameter("id_pasantia", id_pasantia);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> listaPasantias(Carrera carrera) {
        List<Pasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.carrera=:carrera ORDER BY p.nivel, p.mencion, p.nombre");
            q.setParameter("carrera", carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> listaPasantias(Carrera carrera, Mencion mencion) {
        List<Pasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion) ORDER BY p.nivel, p.mencion, p.nombre");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> listaPasantias(Carrera carrera, Mencion mencion, int id_pasantia) {
        List<Pasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion) AND p.id_pasantia!=:id_pasantia ORDER BY p.nivel, p.mencion, p.nombre");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("id_pasantia", id_pasantia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> listaPasantias(Carrera carrera, Mencion mencion, Nivel nivel) {
        List<Pasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p WHERE p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion) AND p.nivel=:nivel ORDER BY p.nombre");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Pasantia> buscar(String keyword, int id_carrera) {
        List<Pasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pasantia p JOIN p.carrera c WHERE c.id_carrera=:id_carrera AND "
                    + "(LOWER(p.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY p.nombre");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public Long cantidadMaximaPasantiasNivel(Carrera carrera, Mencion mencion) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(p) AS cantidad FROM Pasantia p WHERE p.carrera=:carrera AND (p.mencion IS NULL OR p.mencion=:mencion) GROUP BY p.nivel ORDER BY cantidad DESC");
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setMaxResults(1);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
}
