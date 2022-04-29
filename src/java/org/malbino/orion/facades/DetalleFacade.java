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
            Query q = em.createQuery("SELECT d FROM Detalle d JOIN d.comprobante c WHERE c.id_comprobante=:id_comprobante ORDER By d.concepto");
            q.setParameter("id_comprobante", id_comprobante);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Detalle> kardexEconomico(int id_persona, int id_carrera) {
        List<Detalle> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT d FROM Detalle d JOIN d.comprobante c JOIN c.inscrito i JOIN i.estudiante e JOIN i.carrera a JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND a.id_carrera=:id_carrera AND c.valido=TRUE ORDER BY ga.gestion, ga.periodo, c.fecha, d.concepto");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
