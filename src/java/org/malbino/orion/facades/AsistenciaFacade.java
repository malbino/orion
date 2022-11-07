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
import java.util.Date;
import org.malbino.orion.entities.Asistencia;
import org.malbino.orion.entities.NotaPasantia;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class AsistenciaFacade extends AbstractFacade<Asistencia> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AsistenciaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public AsistenciaFacade() {
        super(Asistencia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Asistencia buscarAsistencia(Date ingreso, Date salida, NotaPasantia notaPasantia) {
        Asistencia usr = null;

        try {
            Query q = em.createQuery("SELECT a FROM Asistencia a WHERE a.ingreso=:ingreso AND a.salida=:salida AND a.notaPasantia=:notaPasantia");
            q.setParameter("ingreso", ingreso);
            q.setParameter("salida", salida);
            q.setParameter("notaPasantia", notaPasantia);

            usr = (Asistencia) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscarAsistencia\n" + e.getMessage());
        }

        return usr;
    }

    public Asistencia buscarAsistencia(Date ingreso, Date salida, NotaPasantia notaPasantia, int id_asistencia) {
        Asistencia usr = null;

        try {
            Query q = em.createQuery("SELECT a FROM Asistencia a WHERE a.ingreso=:ingreso AND a.salida=:salida AND a.notaPasantia=:notaPasantia AND a.id_asistencia!=:id_asistencia");
            q.setParameter("ingreso", ingreso);
            q.setParameter("salida", salida);
            q.setParameter("notaPasantia", notaPasantia);
            q.setParameter("id_asistencia", id_asistencia);

            usr = (Asistencia) q.getSingleResult();
        } catch (Exception e) {
            log.error("buscarAsistencia\n" + e.getMessage());
        }

        return usr;
    }

    public List<Asistencia> listaAsistencias(NotaPasantia notaPasantia) {
        List<Asistencia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Asistencia a WHERE a.notaPasantia=:notaPasantia ORDER BY a.ingreso");
            q.setParameter("notaPasantia", notaPasantia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Asistencia> buscar(NotaPasantia notaPasantia, String keyword) {
        List<Asistencia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Asistencia a WHERE a.notaPasantia=:notaPasantia AND "
                    + "LOWER(a.descripcion) LIKE LOWER(:keyword) "
                    + "ORDER BY a.ingreso");

            q.setParameter("notaPasantia", notaPasantia);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }
}
