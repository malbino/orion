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
import org.malbino.orion.entities.Egresado;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Mencion;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class EgresadoFacade extends AbstractFacade<Egresado> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public EgresadoFacade() {
        super(Egresado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Egresado> buscar(String keyword) {
        List<Egresado> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Egresado g JOIN g.estudiante e WHERE "
                    + "(CAST(e.matricula AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "CAST(e.telefono AS CHAR) LIKE :keyword OR "
                    + "CAST(e.celular AS CHAR) LIKE :keyword OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword)) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Egresado> buscar(int id_carrera, String keyword) {
        List<Egresado> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Egresado g JOIN g.estudiante e JOIN g.carrera c WHERE c.id_carrera=:id_carrera AND "
                    + "(CAST(e.matricula AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "CAST(e.telefono AS CHAR) LIKE :keyword OR "
                    + "CAST(e.celular AS CHAR) LIKE :keyword OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword)) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Egresado buscar(Estudiante estudiante, Carrera carrera, Mencion mencion, GestionAcademica gestionAcademica) {
        Egresado g = null;

        try {
            Query q = em.createQuery("SELECT g FROM Egresado g WHERE g.estudiante=:estudiante AND g.carrera=:carrera AND (g.mencion IS NULL OR g.mencion=:mencion) AND g.gestionAcademica=:gestionAcademica");
            q.setParameter("estudiante", estudiante);
            q.setParameter("carrera", carrera);
            q.setParameter("mencion", mencion);
            q.setParameter("gestionAcademica", gestionAcademica);

            g = (Egresado) q.getSingleResult();
        } catch (Exception e) {

        }

        return g;
    }
}
