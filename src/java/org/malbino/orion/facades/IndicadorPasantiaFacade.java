/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.malbino.orion.entities.IndicadorPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class IndicadorPasantiaFacade extends AbstractFacade<IndicadorPasantia> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(IndicadorPasantiaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public IndicadorPasantiaFacade() {
        super(IndicadorPasantia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<IndicadorPasantia> listaIndicadoresPasantias(NotaPasantia notaPasantia) {
        List<IndicadorPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ip FROM IndicadorPasantia ip WHERE ip.notaPasantia=:notaPasantia ORDER BY ip.indicador");
            q.setParameter("notaPasantia", notaPasantia);

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaIndicadorPasantias\n" + e.getMessage());
        }

        return l;
    }

}
