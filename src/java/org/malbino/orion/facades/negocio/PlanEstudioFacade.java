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
import org.malbino.orion.entities.Materia;
import org.malbino.orion.facades.MateriaFacade;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class PlanEstudioFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    MateriaFacade materiaFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean eliminarMateria(Materia prerequisito) {
        List<Materia> materias = materiaFacade.listaMaterias(prerequisito.getId_materia());
        for (Materia materia : materias) {
            materia.getPrerequisitos().remove(prerequisito);

            materiaFacade.edit(materia);
            materiaFacade.getEntityManager().flush();
        }

        materiaFacade.remove(prerequisito);

        return true;
    }
}
