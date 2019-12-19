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
import org.malbino.orion.entities.Estudiante;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class EstudianteFacade extends AbstractFacade<Estudiante> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public EstudianteFacade() {
        super(Estudiante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Estudiante buscarPorDni(String dni) {
        Estudiante usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE e.dni=:dni");
            q.setParameter("dni", dni);

            usr = (Estudiante) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public Estudiante buscarPorDni(String dni, int id_persona) {
        Estudiante usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE e.dni=:dni AND e.id_persona!=:id_persona");
            q.setParameter("dni", dni);
            q.setParameter("id_persona", id_persona);

            usr = (Estudiante) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public List<Estudiante> listaEstudiantes() {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Estudiante> buscar(String keyword) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE "
                    + "LOWER(e.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.lugarNacimiento) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nacionalidad) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
