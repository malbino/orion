/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.util.Constantes;

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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Grupo> programarGrupos(GestionAcademica gestionAcademica, Carrera carrera, Nivel nivel, Mencion mencion, Turno turno, Integer capacidad) {
        List<Grupo> grupos = new ArrayList<>();

        List<Materia> materias = materiaFacade.listaMaterias(carrera, mencion, nivel);
        for (Materia materia : materias) {
            Integer c1 = grupoFacade.cantidadGrupos(gestionAcademica.getId_gestionacademica(), materia.getId_materia(), turno).intValue();
            String codigo = Constantes.ABECEDARIO[c1];

            Grupo grupo = new Grupo(codigo, capacidad, turno, true, gestionAcademica, materia);
            em.persist(grupo);

            grupos.add(grupo);
        }

        return grupos;
    }

}
