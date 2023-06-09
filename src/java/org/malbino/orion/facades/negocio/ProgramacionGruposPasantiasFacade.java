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
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.entities.Pasantia;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.PasantiaFacade;
import org.malbino.orion.util.Constantes;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class ProgramacionGruposPasantiasFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    PasantiaFacade pasantiaFacade;
    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<GrupoPasantia> programarGruposPasantias(GestionAcademica gestionAcademica, Carrera carrera, Nivel nivel, Mencion mencion, Turno turno, Integer capacidad) {
        List<GrupoPasantia> gruposPasantia = new ArrayList<>();

        List<Pasantia> pasantias = pasantiaFacade.listaPasantias(carrera, mencion, nivel);
        for (Pasantia pasantia : pasantias) {
            Integer c1 = grupoPasantiaFacade.cantidadGruposPasantias(gestionAcademica.getId_gestionacademica(), pasantia.getId_pasantia(), turno).intValue();
            String codigo = Constantes.ABECEDARIO[c1];

            GrupoPasantia grupoPasantia = new GrupoPasantia(codigo, capacidad, turno, true, gestionAcademica, pasantia);
            em.persist(grupoPasantia);

            gruposPasantia.add(grupoPasantia);
        }

        return gruposPasantia;
    }

}
