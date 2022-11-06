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
import org.malbino.orion.entities.Empresa;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class EmpresaFacade extends AbstractFacade<Empresa> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EmpresaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public EmpresaFacade() {
        super(Empresa.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Empresa buscarPorDni(String dni) {
        Empresa usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Empresa e WHERE e.dni=:dni");
            q.setParameter("dni", dni);

            usr = (Empresa) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public Empresa buscarPorDni(String dni, int id_persona) {
        Empresa usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Empresa e WHERE e.dni=:dni AND e.id_persona!=:id_persona");
            q.setParameter("dni", dni);
            q.setParameter("id_persona", id_persona);

            usr = (Empresa) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public List<Empresa> listaEmpresas() {
        List<Empresa> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Empresa e ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Empresa> buscar(String keyword) {
        List<Empresa> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Empresa e WHERE "
                    + "LOWER(e.nombreEmpresa) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.actividad) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nit) LIKE LOWER(:keyword) "
                    + "ORDER BY e.nombreEmpresa");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }
}
