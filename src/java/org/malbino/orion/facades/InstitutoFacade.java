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
import org.malbino.orion.entities.Instituto;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class InstitutoFacade extends AbstractFacade<Instituto> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public InstitutoFacade() {
        super(Instituto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Instituto buscarPorId(int id_instituto) {
        Instituto i = null;

        try {
            Query q = em.createQuery("SELECT i FROM Instituto i WHERE i.id_instituto=:id_instituto");
            q.setParameter("id_instituto", id_instituto);

            i = (Instituto) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }
}
