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
import org.malbino.orion.entities.Campus;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class CampusFacade extends AbstractFacade<Campus> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public CampusFacade() {
        super(Campus.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Campus buscarPorSucursal(String sucursal) {
        Campus c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Campus c WHERE c.sucursal=:sucursal");
            q.setParameter("sucursal", sucursal);

            c = (Campus) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public Campus buscarPorSucursal(String sucursal, int id_campus) {
        Campus c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Campus c WHERE c.sucursal=:sucursal AND c.id_campus!=:id_campus");
            q.setParameter("sucursal", sucursal);
            q.setParameter("id_campus", id_campus);

            c = (Campus) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Campus> listaCampus() {
        List<Campus> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Campus c ORDER BY c.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Campus> buscar(String keyword) {
        List<Campus> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Campus c WHERE "
                    + "LOWER(c.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.sucursal) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.direccion) LIKE LOWER(:keyword) "
                    + "ORDER BY c.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
