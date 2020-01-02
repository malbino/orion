/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Nota;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class RegistroDocenteFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarNotas(List<Nota> notas) {
        for (Nota nota : notas) {
            em.merge(nota);
        }

        return true;
    }

}
