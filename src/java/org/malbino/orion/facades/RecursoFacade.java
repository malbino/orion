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
import org.malbino.orion.entities.Recurso;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class RecursoFacade extends AbstractFacade<Recurso> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public RecursoFacade() {
        super(Recurso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Recurso> buscarPorPersonaNombre(int id_persona) {
        List<Recurso> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT r FROM Recurso r JOIN r.roles o JOIN o.usuarios u WHERE u.id_persona=:id_persona");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Recurso> buscarPorPersonaUri(int id_persona, String uri) {
        List<Recurso> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT r.id_recurso FROM Recurso r JOIN r.roles o JOIN o.usuarios u WHERE u.id_persona=:id_persona AND CAST(:uri AS TEXT) LIKE CONCAT(r.urlPattern, '%')");
            q.setParameter("id_persona", id_persona);
            q.setParameter("uri", uri);

            l = q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }

        return l;
    }

}
