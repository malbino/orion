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

    public GestionAcademica buscarPorCodigoRegimen(String codigo, Regimen regimen) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.codigo=:codigo AND ga.regimen=:regimen");
            q.setParameter("codigo", codigo);
            q.setParameter("regimen", regimen);

            ga = (GestionAcademica) q.getSingleResult();
        } catch (Exception e) {

        }

        return ga;
    }
    
     public GestionAcademica buscarPorCodigoRegimen(String codigo, Regimen regimen, int id_gestionacademica) {
        GestionAcademica ga = null;

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.codigo=:codigo AND ga.regimen=:regimen AND ga.id_gestionacademica!=:id_gestionacademica");
            q.setParameter("codigo", codigo);
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
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.regimen=:regimen ORDER BY ga.numero");
            q.setParameter("regimen", regimen);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GestionAcademica> buscar(String keyword, Regimen regimen) {
        List<GestionAcademica> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT ga FROM GestionAcademica ga WHERE ga.regimen=:regimen AND "
                    + "LOWER(ga.codigo) LIKE LOWER(:keyword) "
                    + "ORDER BY ga.numero");
            q.setParameter("regimen", regimen);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
