/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Detalle;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.DetalleFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class PagosFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    DetalleFacade detalleFacade;
    
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean nuevoPago(Comprobante comprobante, List<Pago> pagos) {
        long cantidadComprobantes = comprobanteFacade.cantidadComprobantes(comprobante.getFecha());
        comprobante.setCodigo(String.format("%05d", (cantidadComprobantes + 1)) + "/" + Fecha.extrarAño(comprobante.getFecha()));
        em.persist(comprobante);
        
        for (Pago pago : pagos) {
            Detalle detalle = new Detalle(pago.getConcepto(), pago.getMonto(), comprobante, pago);
            em.persist(detalle);
            
            pago.setPagado(true);
            em.merge(pago);
        }
        
        return true;
    }
    
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean anularPago(Comprobante comprobante) {
        comprobante.setValido(false);
        em.merge(comprobante);
        
        List<Detalle> detalles = detalleFacade.listaDetalles(comprobante.getId_comprobante());
        for (Detalle detalle : detalles) {
            Pago pago = detalle.getPago();
            pago.setPagado(false);
            em.merge(pago);
        }
        
        return true;
    }

}