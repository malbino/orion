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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.enums.ModalidadEvaluacion;
import org.malbino.orion.enums.Periodo;
import org.malbino.orion.enums.Regimen;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class GestionAcademicaFacade extends AbstractFacade<GestionAcademica> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public GestionAcademicaFacade() {
        super(GestionAcademica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public GestionAcademica buscarPorCodigoRegimen(Integer gestion, Periodo periodo, Regimen regimen) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.gestion=:gestion AND ga.periodo=:periodo AND ga.regimen=:regimen");
            q.setParameter("gestion", gestion);
            q.setParameter("periodo", periodo);
            q.setParameter("regimen", regimen);

            ga = (GestionAcademica) q.getSingleResult();
        } catch (Exception e) {

        }

        return ga;
    }

    public GestionAcademica buscarPorCodigoRegimen(Integer gestion, Periodo periodo, Regimen regimen, int id_gestionacademica) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.gestion=:gestion AND ga.periodo=:periodo AND ga.regimen=:regimen AND ga.id_gestionacademica!=:id_gestionacademica");
            q.setParameter("gestion", gestion);
            q.setParameter("periodo", periodo);
            q.setParameter("regimen", regimen);
            q.setParameter("id_gestionacademica", id_gestionacademica);

            ga = (GestionAcademica) q.getSingleResult();
        } catch (Exception e) {

        }

        return ga;
    }

    public List<GestionAcademica> listaGestionAcademica(Regimen regimen) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.regimen=:regimen ORDER BY ga.gestion, ga.periodo");
            q.setParameter("regimen", regimen);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademica(Regimen regimen, boolean vigente) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.regimen=:regimen AND ga.vigente=:vigente ORDER BY ga.gestion, ga.periodo");
            q.setParameter("regimen", regimen);
            q.setParameter("vigente", vigente);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademica() {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.vigente=TRUE ORDER BY ga.gestion, ga.periodo");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> buscar(String keyword, Regimen regimen) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.regimen=:regimen AND "
                    + "LOWER(CAST(ga.gestion AS CHAR)) LIKE LOWER(:keyword) "
                    + "ORDER BY ga.gestion, ga.periodo");
            q.setParameter("regimen", regimen);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademicaDashboard() {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga ORDER BY ga.gestion, ga.periodo");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Integer> listaGestiones() {
        List<Integer> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT ga.gestion FROM GestionAcademica ga ORDER BY ga.gestion, ga.periodo");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademica(ModalidadEvaluacion modalidadEvaluacion1, boolean vigente) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.modalidadEvaluacion=:modalidadEvaluacion1 AND ga.vigente=:vigente ORDER BY ga.gestion, ga.periodo");
            q.setParameter("modalidadEvaluacion1", modalidadEvaluacion1);
            q.setParameter("vigente", vigente);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademica(ModalidadEvaluacion modalidadEvaluacion1, ModalidadEvaluacion modalidadEvaluacion2, boolean vigente) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE (ga.modalidadEvaluacion=:modalidadEvaluacion1 OR ga.modalidadEvaluacion=:modalidadEvaluacion2) AND ga.vigente=:vigente ORDER BY ga.gestion, ga.periodo");
            q.setParameter("modalidadEvaluacion1", modalidadEvaluacion1);
            q.setParameter("modalidadEvaluacion2", modalidadEvaluacion2);
            q.setParameter("vigente", vigente);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> listaGestionAcademica(ModalidadEvaluacion modalidadEvaluacion1, ModalidadEvaluacion modalidadEvaluacion2, ModalidadEvaluacion modalidadEvaluacion3, boolean vigente) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE (ga.modalidadEvaluacion=:modalidadEvaluacion1 OR ga.modalidadEvaluacion=:modalidadEvaluacion2 OR ga.modalidadEvaluacion=:modalidadEvaluacion3) AND ga.vigente=:vigente ORDER BY ga.gestion, ga.periodo");
            q.setParameter("modalidadEvaluacion1", modalidadEvaluacion1);
            q.setParameter("modalidadEvaluacion2", modalidadEvaluacion2);
            q.setParameter("modalidadEvaluacion3", modalidadEvaluacion3);
            q.setParameter("vigente", vigente);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
