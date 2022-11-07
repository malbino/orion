/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class NotaPasantiaFacade extends AbstractFacade<NotaPasantia> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NotaPasantiaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public NotaPasantiaFacade() {
        super(NotaPasantia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public NotaPasantia buscarNotaPasantia(Estudiante estudiante, GrupoPasantia grupoPasantia, Empresa empresa) {
        NotaPasantia i = null;

        try {
            Query q = em.createQuery("SELECT np FROM NotaPasantia np WHERE np.estudiante=:estudiante AND np.grupoPasantia=:grupoPasantia AND np.empresa=:empresa");
            q.setParameter("estudiante", estudiante);
            q.setParameter("grupoPasantia", grupoPasantia);
            q.setParameter("empresa", empresa);

            i = (NotaPasantia) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public Long maximoCodigo(int id_gestioncademica, int id_carrera) {
        Long l = null;

        try {
            Query q = em.createQuery("SELECT MAX(np.codigo) FROM NotaPasantia np JOIN np.grupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            log.error("maximoCodigo\n" + e.getMessage());
        }

        return l;
    }
}
