/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Periodo;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class PeriodoFacade extends AbstractFacade<Periodo> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PeriodoFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public PeriodoFacade() {
        super(Periodo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Periodo buscar(Date inicio, Date fin) {
        Periodo p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Periodo p WHERE p.inicio=:inicio AND p.fin=:fin");
            q.setParameter("inicio", inicio);
            q.setParameter("fin", fin);

            p = (Periodo) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return p;
    }

    public Periodo buscar(Date inicio, Date fin, int id_periodo) {
        Periodo p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Periodo p WHERE p.inicio=:inicio AND p.fin=:fin AND p.id_periodo!=:id_periodo");
            q.setParameter("inicio", inicio);
            q.setParameter("fin", fin);
            q.setParameter("id_periodo", id_periodo);

            p = (Periodo) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return p;
    }

    public List<Periodo> listaPeriodos() {
        List<Periodo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Periodo p ORDER BY p.inicio");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaPeriodos\n" + e.getMessage());
        }

        return l;
    }

    public List<Periodo> buscar(String keyword) {
        List<Periodo> periodos = listaPeriodos();
        List<Periodo> periodosFiltrados = new ArrayList();

        for (Periodo p : periodos) {
            if (p.inicio_HHmm().toUpperCase().contains(keyword.toUpperCase()) || p.fin_HHmm().toUpperCase().contains(keyword.toUpperCase())) {
                periodosFiltrados.add(p);
            }
        }
        return periodosFiltrados;
    }
    
    public Periodo buscar(Date fin) {
        Periodo p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Periodo p WHERE p.fin=:fin");
            q.setParameter("fin", fin);

            p = (Periodo) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return p;
    }
}
