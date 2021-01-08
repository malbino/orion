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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class ComprobanteFacade extends AbstractFacade<Comprobante> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public ComprobanteFacade() {
        super(Comprobante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long cantidadComprobantes(Date fecha) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(c) FROM Comprobante c WHERE c.fecha BETWEEN :inicio AND :fin");
            q.setParameter("inicio", Fecha.getInicioAño(fecha));
            q.setParameter("fin", Fecha.getFinAño(fecha));

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Comprobante> listaComprobantes() {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c ORDER BY c.fecha DESC");
            q.setMaxResults(100);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Comprobante> buscar(String keyword) {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c JOIN c.inscrito i JOIN i.estudiante e JOIN i.carrera a JOIN i.gestionAcademica ga WHERE "
                    + "LOWER(c.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(a.nombre) LIKE LOWER(:keyword) OR "
                    + "LOWER(CAST(ga.gestion AS CHAR)) LIKE LOWER(:keyword) "
                    + "ORDER BY c.fecha DESC");
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Comprobante buscarComprobanteValido(int id_pago) {
        Comprobante c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Detalle d JOIN d.combrobante c JOIN d.pago p WHERE p.id_pago=:id_pago AND c.valido=TRUE");
            q.setParameter("id_pago", id_pago);
            q.setMaxResults(1);

            c = (Comprobante) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }
}
