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
import org.malbino.orion.entities.Mencion;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class MencionFacade extends AbstractFacade<Mencion> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public MencionFacade() {
        super(Mencion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Mencion buscarPorCodigo(String codigo, int id_carrera) {
        Mencion c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Mencion m JOIN m.carrera c WHERE m.codigo=:codigo AND c.id_carrera=:id_carrera");
            q.setParameter("codigo", codigo);
            q.setParameter("id_carrera", id_carrera);

            c = (Mencion) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public Mencion buscarPorCodigo(String codigo, int id_mencion, int id_carrera) {
        Mencion c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Mencion m JOIN m.carrera c WHERE m.codigo=:codigo AND m.id_mencion!=:id_mencion AND c.id_carrera=:id_carrera");
            q.setParameter("codigo", codigo);
            q.setParameter("id_mencion", id_mencion);
            q.setParameter("id_carrera", id_carrera);

            c = (Mencion) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Mencion> listaMenciones(int id_carrera) {
        List<Mencion> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Mencion m JOIN m.carrera c WHERE c.id_carrera=:id_carrera ORDER BY m.nombre");
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Mencion> buscar(String keyword, int id_carrera) {
        List<Mencion> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Mencion m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND "
                    + "(LOWER(m.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(m.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY m.nombre");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

}
