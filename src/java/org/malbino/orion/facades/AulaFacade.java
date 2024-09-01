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
import org.malbino.orion.entities.Aula;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class AulaFacade extends AbstractFacade<Aula> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AulaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public AulaFacade() {
        super(Aula.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Aula buscarPorNombre(String nombre, int id_campus) {
        Aula c = null;

        try {
            Query q = em.createQuery("SELECT a FROM Aula a JOIN a.campus c WHERE a.nombre=:nombre AND c.id_campus=:id_campus");
            q.setParameter("nombre", nombre);
            q.setParameter("id_campus", id_campus);

            c = (Aula) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscarPorNombre\n" + e.getMessage());
        }

        return c;
    }

    public Aula buscarPorNombre(String nombre, int id_aula, int id_campus) {
        Aula c = null;

        try {
            Query q = em.createQuery("SELECT a FROM Aula a JOIN a.campus c WHERE a.nombre=:nombre AND a.id_aula!=:id_aula AND c.id_campus=:id_campus");
            q.setParameter("nombre", nombre);
            q.setParameter("id_aula", id_aula);
            q.setParameter("id_campus", id_campus);

            c = (Aula) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscarPorNombre\n" + e.getMessage());
        }

        return c;
    }

    public List<Aula> listaAulas() {
        List<Aula> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Aula a ORDER BY a.nombre");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaAulas\n" + e.getMessage());
        }

        return l;
    }

    public List<Aula> listaAulas(int id_campus) {
        List<Aula> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Aula a JOIN a.campus c WHERE c.id_campus=:id_campus ORDER BY a.nombre");
            q.setParameter("id_campus", id_campus);

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaAulas\n" + e.getMessage());
        }

        return l;
    }

    public List<Aula> buscar(String keyword) {
        List<Aula> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Aula a WHERE "
                    + "LOWER(a.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(a.descripcion) LIKE LOWER(:keyword) "
                    + "ORDER BY a.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }

    public List<Aula> buscar(String keyword, int id_campus) {
        List<Aula> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Aula a JOIN a.campus c WHERE c.id_campus=:id_campus AND "
                    + "(LOWER(a.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(a.descripcion) LIKE LOWER(:keyword)) "
                    + "ORDER BY a.nombre");
            q.setParameter("id_campus", id_campus);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }
}
