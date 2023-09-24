/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.Concepto;
import org.malbino.orion.facades.PostulanteFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class AdmisionesFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    PostulanteFacade postulanteFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarPostulante(Postulante postulante) {
        //postulante
        long cantidadPostulantes = postulanteFacade.cantidadPostulantes(postulante.getGestionAcademica().getId_gestionacademica(), postulante.getCarrera().getId_carrera());
        String codigo
                = "POS-"
                + postulante.getGestionAcademica().codigo() + "-"
                + postulante.getCarrera().getCodigo() + "-"
                + String.format("%04d", cantidadPostulantes + 1);
        postulante.setCodigo(codigo);
        postulante.setFecha(Fecha.getDate());

        em.persist(postulante);
        em.flush();

        //pago
        Integer monto = postulante.getCarrera().getCreditajeAdmision() * postulante.getCarrera().getCampus().getInstituto().getPrecioCredito();
        Pago pago = new Pago(Concepto.ADMISION, monto, false, postulante);
        em.persist(pago);

        return true;
    }
}
