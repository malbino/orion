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
import org.malbino.orion.entities.Rol;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class RolFacade extends AbstractFacade<Rol> {
    
    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public RolFacade() {
        super(Rol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Rol buscarRol(String nombre) {
        Rol rol = null;

        try {
            Query q = em.createQuery("SELECT r FROM Rol r WHERE r.nombre=:nombre");
            q.setParameter("nombre", nombre);

            rol = (Rol) q.getSingleResult();
        } catch (Exception e) {

        }

        return rol;
    }

    public Rol buscarRol(String nombre, int id_rol) {
        Rol rol = null;

        try {
            Query q = em.createQuery("SELECT r FROM Rol r WHERE r.nombre=:nombre AND r.id_rol!=:id_rol");
            q.setParameter("nombre", nombre);
            q.setParameter("id_rol", id_rol);

            rol = (Rol) q.getSingleResult();
        } catch (Exception e) {

        }

        return rol;
    }

    public List<Rol> listaRoles() {
        List<Rol> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT r FROM Rol r ORDER BY r.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Rol> listaRoles(int id_persona) {
        List<Rol> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT r FROM Rol r JOIN r.usuarios u WHERE u.id_persona=:id_persona ORDER BY r.nombre");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Rol> buscar(String keyword) {
        List<Rol> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT r FROM Rol r WHERE "
                    + "LOWER(r.nombre) LIKE LOWER(:keyword) "
                    + "ORDER BY r.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
