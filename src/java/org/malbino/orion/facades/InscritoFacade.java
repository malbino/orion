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
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.enums.Tipo;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class InscritoFacade extends AbstractFacade<Inscrito> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public InscritoFacade() {
        super(Inscrito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public long cantidadInscritos(int id_gestioncademica, Tipo tipo) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND i.tipo=:tipo");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("tipo", tipo);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public long cantidadInscritos(int id_gestioncademica, int id_carrera) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

}