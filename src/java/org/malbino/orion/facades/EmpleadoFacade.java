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
import org.malbino.orion.entities.Empleado;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class EmpleadoFacade extends AbstractFacade<Empleado> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public EmpleadoFacade() {
        super(Empleado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Empleado buscarPorDni(String dni) {
        Empleado usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Empleado e WHERE e.dni=:dni");
            q.setParameter("dni", dni);

            usr = (Empleado) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public Empleado buscarPorDni(String dni, int id_persona) {
        Empleado usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Empleado e WHERE e.dni=:dni AND e.id_persona!=:id_persona");
            q.setParameter("dni", dni);
            q.setParameter("id_persona", id_persona);

            usr = (Empleado) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public List<Empleado> listaEmpleados() {
        List<Empleado> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Empleado e ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Empleado> buscar(String keyword) {
        List<Empleado> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Empleado e WHERE "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "LOWER(CAST(e.telefono AS CHAR)) LIKE LOWER(:keyword) OR "
                    + "LOWER(CAST(e.celular AS CHAR)) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }
}
