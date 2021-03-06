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
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.enums.Regimen;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class CarreraFacade extends AbstractFacade<Carrera> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public CarreraFacade() {
        super(Carrera.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Carrera buscarPorCodigo(String codigo) {
        Carrera c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c WHERE c.codigo=:codigo");
            q.setParameter("codigo", codigo);

            c = (Carrera) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public Carrera buscarPorCodigo(String codigo, int id_carrera) {
        Carrera c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c WHERE c.codigo=:codigo AND c.id_carrera!=:id_carrera");
            q.setParameter("codigo", codigo);
            q.setParameter("id_carrera", id_carrera);

            c = (Carrera) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c ORDER BY c.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Carrera> listaCarreras(int id_campus) {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c JOIN c.campus a WHERE a.id_campus=:id_campus ORDER BY c.nombre");
            q.setParameter("id_campus", id_campus);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Carrera> listaCarreras(Regimen regimen) {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c WHERE c.regimen=:regimen ORDER BY c.nombre");
            q.setParameter("regimen", regimen);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Carrera> listaCarrerasEstudiante(int id_persona) {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Estudiante e JOIN e.carreras c WHERE e.id_persona=:id_persona ORDER BY c.nombre");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Carrera> buscar(String keyword) {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c WHERE "
                    + "LOWER(c.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.resolucionMinisterial) LIKE LOWER(:keyword) "
                    + "ORDER BY c.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Carrera> buscar(String keyword, int id_campus) {
        List<Carrera> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Carrera c JOIN c.campus a WHERE a.id_campus=:id_campus AND "
                    + "(LOWER(c.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.resolucionMinisterial) LIKE LOWER(:keyword)) "
                    + "ORDER BY c.nombre");
            q.setParameter("id_campus", id_campus);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
