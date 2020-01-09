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
import org.malbino.orion.entities.Inscrito;

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

    public Long cantidadInscritos(int id_gestioncademica, int id_carrera) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona ORDER BY c.nombre, ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Inscrito buscarInscrito(int id_persona, int id_carrera, int id_gestionacademica) {
        Inscrito i = null;

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND ga.id_gestionacademica=:id_gestionacademica");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_gestionacademica", id_gestionacademica);

            i = (Inscrito) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public List<Inscrito> listaInscritosPruebaRecuperacion(GestionAcademica gestionAcademica, int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN n.grupo g JOIN g.empleado e WHERE ga.id_gestionacademica=:id_gestionacademica AND e.id_persona=:id_persona AND n.notaFinal<:notaMinimaAprobacion GROUP BY i HAVING COUNT(n) >= 1 AND COUNT(n) <=:cantidadMaximaReprobaciones");
            q.setParameter("id_gestionacademica", gestionAcademica.getId_gestionacademica());
            q.setParameter("id_persona", id_persona);
            q.setParameter("notaMinimaAprobacion", gestionAcademica.getRegimen().getNotaMinimaAprobacion());
            q.setParameter("cantidadMaximaReprobaciones", gestionAcademica.getRegimen().getCantidadMaximaReprobaciones());

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY i.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
