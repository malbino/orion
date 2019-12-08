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
import org.malbino.orion.entities.Materia;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class MateriaFacade extends AbstractFacade<Materia> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public MateriaFacade() {
        super(Materia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Materia buscarPorCodigo(String codigo) {
        Materia c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo");
            q.setParameter("codigo", codigo);

            c = (Materia) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public Materia buscarPorCodigo(String codigo, int id_materia) {
        Materia c = null;

        try {
            Query q = em.createQuery("SELECT m FROM Materia m WHERE m.codigo=:codigo AND m.id_materia!=:id_materia");
            q.setParameter("codigo", codigo);
            q.setParameter("id_materia", id_materia);

            c = (Materia) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Materia> listaMaterias(int id_carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera ORDER BY m.nivel, m.numero");
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Materia> listaMaterias(int id_carrera, int id_materia) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND m.id_materia!=:id_materia ORDER BY m.nivel, m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Materia> buscar(String keyword, int id_carrera) {
        List<Materia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT m FROM Materia m JOIN m.carrera c WHERE c.id_carrera=:id_carrera AND "
                    + "(LOWER(m.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(m.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY m.numero");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
