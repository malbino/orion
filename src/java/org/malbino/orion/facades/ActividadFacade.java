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
import org.malbino.orion.entities.Actividad;
import org.malbino.orion.enums.Funcionalidad;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class ActividadFacade extends AbstractFacade<Actividad> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public ActividadFacade() {
        super(Actividad.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Actividad> listaActividad(int id_gestionacademica) {
        List<Actividad> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Actividad a JOIN a.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica ORDER BY a.inicio, a.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Actividad> buscar(String keyword, int id_gestionacademica) {
        List<Actividad> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Actividad a JOIN a.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND "
                    + "(LOWER(a.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(a.descripcion) LIKE LOWER(:keyword)) "
                    + "ORDER BY a.inicio, a.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Actividad> listaActividades(Date fecha, Funcionalidad funcionalidad, int id_gestionacademica) {
        List<Actividad> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Actividad a JOIN a.gestionAcademica ga WHERE :fecha BETWEEN a.inicio AND a.fin AND a.funcionalidad=:funcionalidad AND ga.id_gestionacademica=:id_gestionacademica");
            q.setParameter("fecha", fecha);
            q.setParameter("funcionalidad", funcionalidad);
            q.setParameter("id_gestionacademica", id_gestionacademica);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Actividad> listaActividadesProximas(Date fecha) {
        List<Actividad> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Actividad a WHERE a.inicio >= :fecha OR :fecha BETWEEN a.inicio AND a.fin ORDER BY a.inicio, a.nombre");
            q.setParameter("fecha", fecha);
            q.setMaxResults(5);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
