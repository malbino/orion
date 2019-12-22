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
import org.malbino.orion.entities.Pago;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class PagoFacade extends AbstractFacade<Pago> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public PagoFacade() {
        super(Pago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Pago> listaPagosAdeudados(int id_inscrito) {
        List<Pago> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pago p JOIN p.inscrito i WHERE i.id_inscrito=:id_inscrito AND p.pagado=FALSE ORDER BY p.concepto");
            q.setParameter("id_inscrito", id_inscrito);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Pago> listaPagosPagados(int id_inscrito) {
        List<Pago> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Pago p JOIN p.inscrito i WHERE i.id_inscrito=:id_inscrito AND p.pagado=TRUE ORDER BY p.concepto");
            q.setParameter("id_inscrito", id_inscrito);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

}
