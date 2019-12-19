/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.Date;
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
    
    public long cantidadComprobantes(Date fecha) {
        long l = 0;
        
        try {
            Query q = em.createQuery("SELECT COUNT(c) FROM Comprobante c WHERE c.fecha BETWEEN :inicio AND :fin");
            q.setParameter("inicio", Fecha.getInicioAño(fecha));
            q.setParameter("fin", Fecha.getFinAño(fecha));
            
            l = (long) q.getSingleResult();            
        } catch (Exception e) {
            
        }
        
        return l;
    }
}
