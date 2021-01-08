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
import org.malbino.orion.entities.Usuario;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Usuario buscarPorUsuario(String usuario) {
        Usuario usr = null;

        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.usuario=:usuario");
            q.setParameter("usuario", usuario);

            usr = (Usuario) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public Usuario buscarPorUsuario(String usuario, int id_persona) {
        Usuario usr = null;

        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.usuario=:usuario AND u.id_persona!=:id_persona");
            q.setParameter("usuario", usuario);
            q.setParameter("id_persona", id_persona);

            usr = (Usuario) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public List<Usuario> listaUsuarios() {
        List<Usuario> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT u FROM Usuario u ORDER BY u.primerApellido, u.segundoApellido, u.nombre");
            q.setMaxResults(100);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Usuario> buscar(String keyword) {
        List<Usuario> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE "
                    + "LOWER(u.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(u.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(u.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(u.email) LIKE LOWER(:keyword) OR "
                    + "LOWER(u.usuario) LIKE LOWER(:keyword) "
                    + "ORDER BY u.primerApellido, u.segundoApellido, u.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
