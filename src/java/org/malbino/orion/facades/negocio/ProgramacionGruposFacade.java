/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.MateriaFacade;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class ProgramacionGruposFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    MateriaFacade materiaFacade;
    @EJB
    GrupoFacade grupoFacade;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean programarGrupos(GestionAcademica gestionAcademica, Carrera carrera, Nivel nivel, Turno turno, Integer capacidad) {
        boolean b = true;

        try {
            List<Materia> materias = materiaFacade.listaMaterias(carrera.getId_carrera(), nivel);
            for (Materia materia : materias) {
                long cantidadGrupos = grupoFacade.cantidadGrupos(gestionAcademica.getId_gestionacademica(), materia.getId_materia());
                String codigo = "G" + (cantidadGrupos + 1);

                Grupo grupo = new Grupo(codigo, capacidad, turno, true, gestionAcademica, materia);
                em.persist(grupo);
            }
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

}
