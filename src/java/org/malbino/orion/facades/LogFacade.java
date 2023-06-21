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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class LogFacade extends AbstractFacade<Log> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public LogFacade() {
        super(Log.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Log> buscarLogs(Date desde, Date hasta, EventoLog eventoLog, EntidadLog entidadLog, String descripcion, String usuario) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Log> cq = cb.createQuery(Log.class);

        Root<Log> log = cq.from(Log.class);

        List<Predicate> predicates = new ArrayList();

        if (desde != null && hasta == null) {
            predicates.add(cb.and(cb.greaterThanOrEqualTo(log.get("fecha"), Fecha.getInicioDia(desde))));
        } else if (desde == null && hasta != null) {
            predicates.add(cb.and(cb.lessThanOrEqualTo(log.get("fecha"), Fecha.getFinDia(hasta))));
        } else if (desde != null && hasta != null) {
            Path<Date> fecha = log.get("fecha");
            predicates.add(cb.and(cb.between(fecha, Fecha.getInicioDia(desde), Fecha.getFinDia(hasta))));
        }

        if (eventoLog != null) {
            predicates.add(cb.and(cb.equal(log.get("eventoLog"), eventoLog)));
        }

        if (entidadLog != null) {
            predicates.add(cb.and(cb.equal(log.get("entidadLog"), entidadLog)));
        }

        if (descripcion != null && !descripcion.isEmpty()) {
            predicates.add(cb.and(cb.like(log.get("descripcion"), "%" + descripcion + "%")));
        }

        if (usuario != null && !usuario.isEmpty()) {
            predicates.add(cb.and(cb.like(log.get("usuario"), "%" + usuario + "%")));
        }

        cq.select(log)
                .where(predicates.toArray(new Predicate[predicates.size()]))
                .orderBy(cb.desc(log.get("fecha")));

        TypedQuery<Log> tq = em.createQuery(cq);

        return tq.getResultList();
    }

    public List<Log> listaLogNota(int id_nota) {
        List<Log> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT l FROM Log l WHERE l.entidadLog=:entidadLog AND l.idEntidad=:id_nota ORDER BY l.fecha DESC");
            q.setParameter("entidadLog", EntidadLog.NOTA);
            q.setParameter("id_nota", id_nota);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

}
