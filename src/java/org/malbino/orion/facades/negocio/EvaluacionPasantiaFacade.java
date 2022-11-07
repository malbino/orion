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
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.entities.IndicadorPasantia;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.facades.IndicadorPasantiaFacade;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class EvaluacionPasantiaFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    IndicadorPasantiaFacade indicadorPasantiaFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean evaluacionEmpresa(NotaPasantia notaPasantia, List<IndicadorPasantia> indicadoresPasantias) {
        double sumatoria = 0.0;
        for (IndicadorPasantia indicadorPasantia : indicadoresPasantias) {
            em.merge(indicadorPasantia);

            sumatoria += indicadorPasantia.getEvaluacion().getNotaCuantitativa();
        }
        double promedioNotaEmpresa = sumatoria / indicadoresPasantias.size();
        Integer notaEmpresa = Redondeo.redondear_HALFUP(promedioNotaEmpresa, 0).intValue();
        notaPasantia.setNotaEmpresa(notaEmpresa);

        double promedioNotaFinal = (notaPasantia.getNotaEmpresa().doubleValue() + notaPasantia.getNotaTutor().doubleValue()) / 2.0;
        Integer notaFinal = Redondeo.redondear_HALFUP(promedioNotaFinal, 0).intValue();
        notaPasantia.setNotaFinal(notaFinal);
        if (notaFinal >= notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
            notaPasantia.setCondicion(Condicion.APROBADO);
        } else {
            notaPasantia.setCondicion(Condicion.REPROBADO);
        }

        em.merge(notaPasantia);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean evaluacionTutor(NotaPasantia notaPasantia) {
        double promedioNotaFinal = (notaPasantia.getNotaEmpresa().doubleValue() + notaPasantia.getNotaTutor().doubleValue()) / 2.0;
        Integer notaFinal = Redondeo.redondear_HALFUP(promedioNotaFinal, 0).intValue();
        notaPasantia.setNotaFinal(notaFinal);
        if (notaFinal >= notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
            notaPasantia.setCondicion(Condicion.APROBADO);
        } else {
            notaPasantia.setCondicion(Condicion.REPROBADO);
        }

        em.merge(notaPasantia);

        return true;
    }
}
