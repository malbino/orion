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
import org.malbino.orion.entities.Detalle;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class DetalleFacade extends AbstractFacade<Detalle> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public DetalleFacade() {
        super(Detalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Detalle> listaDetalles(int id_comprobante) {
        List<Detalle> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT d FROM Detalle d JOIN d.combrobante c WHERE c.id_comprobante=:id_comprobante ORDER By d.concepto");
            q.setParameter("id_comprobante", id_comprobante);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
