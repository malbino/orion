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

    public Integer maximoCodigo(Date fecha) {
        Integer i = null;

        try {
            Query q = em.createQuery("SELECT MAX(c.codigo) FROM Comprobante c WHERE c.fecha BETWEEN :inicio AND :fin");
            q.setParameter("inicio", Fecha.getInicioAño(fecha));
            q.setParameter("fin", Fecha.getFinAño(fecha));

            i = (Integer) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public List<Comprobante> listaComprobantes() {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c ORDER BY c.fecha DESC");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Comprobante> buscar(Date desde, Date hasta, String keyword) {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c WHERE c.id_comprobante "
                    + "IN ("
                    + "SELECT c.id_comprobante FROM Comprobante c JOIN c.postulante p WHERE c.fecha BETWEEN :desde AND :hasta AND "
                    + "(LOWER(c.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.deposito) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.nombre) LIKE LOWER(:keyword))"
                    + ") OR c.id_comprobante "
                    + "IN ("
                    + "SELECT c.id_comprobante FROM Comprobante c JOIN c.inscrito i JOIN i.estudiante e WHERE c.fecha BETWEEN :desde AND :hasta AND "
                    + "(LOWER(c.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(c.deposito) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nombre) LIKE LOWER(:keyword))"
                    + ") "
                    + "ORDER BY c.fecha DESC");
            q.setParameter("desde", Fecha.getInicioDia(desde));
            q.setParameter("hasta", Fecha.getFinDia(hasta));
            q.setParameter("keyword", "%" + keyword + "%");
            
            l = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return l;
    }

    public Comprobante buscarComprobanteValido(int id_pago) {
        Comprobante c = null;

        try {
            Query q = em.createQuery("SELECT c FROM Detalle d JOIN d.comprobante c JOIN d.pago p WHERE p.id_pago=:id_pago AND c.valido=TRUE");
            q.setParameter("id_pago", id_pago);
            q.setMaxResults(1);

            c = (Comprobante) q.getSingleResult();
        } catch (Exception e) {

        }

        return c;
    }

    public List<Comprobante> listaComprobantes(Date desde, Date hasta) {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c WHERE c.fecha BETWEEN :desde AND :hasta ORDER BY c.fecha DESC");
            q.setParameter("desde", Fecha.getInicioDia(desde));
            q.setParameter("hasta", Fecha.getFinDia(hasta));

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Comprobante> listaComprobantes(Date desde, Date hasta, int id_persona) {
        List<Comprobante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT c FROM Comprobante c JOIN c.usuario u WHERE c.fecha BETWEEN :desde AND :hasta AND u.id_persona=:id_persona ORDER BY c.fecha DESC");
            q.setParameter("desde", Fecha.getInicioDia(desde));
            q.setParameter("hasta", Fecha.getFinDia(hasta));
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
