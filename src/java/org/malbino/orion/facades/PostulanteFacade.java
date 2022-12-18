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
import org.malbino.orion.entities.Postulante;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class PostulanteFacade extends AbstractFacade<Postulante> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public PostulanteFacade() {
        super(Postulante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Postulante buscarPostulante(String ci, int id_gestionacademica, int id_carrera) {
        Postulante p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE p.ci=:ci AND ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("ci", ci);
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            p = (Postulante) q.getSingleResult();
        } catch (Exception e) {

        }

        return p;
    }

    public Postulante buscarPostulante(String ci, int id_gestionacademica, int id_carrera, int id_postulante) {
        Postulante p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE p.ci=:ci AND ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.id_postulante!=:id_postulante");
            q.setParameter("ci", ci);
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_postulante", id_postulante);

            p = (Postulante) q.getSingleResult();
        } catch (Exception e) {

        }

        return p;
    }

    public Postulante buscarPostulante(String ci, Date fechaNacimiento, int id_gestionacademica, int id_carrera) {
        Postulante p = null;

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE p.ci=:ci AND p.fechaNacimiento=:fechaNacimiento AND ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("ci", ci);
            q.setParameter("fechaNacimiento", fechaNacimiento);
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            p = (Postulante) q.getSingleResult();
        } catch (Exception e) {

        }

        return p;
    }

    public long cantidadPostulantes(int id_gestionacademica, int id_carrera) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(p) FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Postulante> listaPostulantes(int id_gestionacademica) {
        List<Postulante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica ORDER BY p.fecha");
            q.setParameter("id_gestionacademica", id_gestionacademica);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Postulante> listaPostulantes(int id_gestionacademica, int id_carrera) {
        List<Postulante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY p.fecha");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Postulante> listaPostulantesGACarrera(int id_gestionacademica, int id_carrera) {
        List<Postulante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY p.primerApellido, p.segundoApellido, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Postulante> buscar(int id_gestionacademica, String keyword) {
        List<Postulante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga WHERE ga.id_gestionacademica=:id_gestionacademica AND "
                    + "(CAST(p.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(p.primerApellido, p.segundoApellido, p.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY p.primerApellido, p.segundoApellido, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Postulante> buscar(int id_gestionacademica, int id_carrera, String keyword) {
        List<Postulante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT p FROM Postulante p JOIN p.gestionAcademica ga JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND "
                    + "(CAST(p.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(p.primerApellido, p.segundoApellido, p.nombre), ' ', '')) LIKE :keyword) "
                    + "ORDER BY p.primerApellido, p.segundoApellido, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");
           
            l = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }
}
