/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.IndicadorPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.entities.Pasantia;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Evaluacion;
import org.malbino.orion.enums.Indicador;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.IndicadorPasantiaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.facades.PasantiaFacade;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class PasantiaEstudianteFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @EJB
    PasantiaFacade pasantiaFacade;
    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @EJB
    IndicadorPasantiaFacade indicadorPasantiaFacade;

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Pasantia> oferta(Carrera carrera, Estudiante estudiante) {
        CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
        carreraEstudianteId.setId_carrera(carrera.getId_carrera());
        carreraEstudianteId.setId_persona(estudiante.getId_persona());
        CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(carreraEstudianteId);

        List<Pasantia> listaPasantiasCarrera;
        if (carreraEstudiante != null) {
            listaPasantiasCarrera = pasantiaFacade.listaPasantias(carrera, carreraEstudiante.getMencion());
        } else {
            listaPasantiasCarrera = pasantiaFacade.listaPasantias(carrera, null);
        }

        List<Pasantia> listaPasantiaAprobadas = pasantiaFacade.listaPasantiasAprobadas(estudiante.getId_persona(), carrera.getId_carrera());
        listaPasantiasCarrera.removeAll(listaPasantiaAprobadas);

        return listaPasantiasCarrera;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public NotaPasantia registrarPasantia(Date fecha, Estudiante estudiante, GrupoPasantia grupoPasantia, CarreraEstudiante carreraEstudiante, Empresa empresa) {
        em.merge(estudiante);

        Long maximoCodigo = notaPasantiaFacade.maximoCodigo(grupoPasantia.getGestionAcademica().getId_gestionacademica(), carreraEstudiante.getCarrera().getId_carrera());
        Long codigo;
        if (maximoCodigo == null) {
            codigo = (Long.valueOf(grupoPasantia.getGestionAcademica().getGestion().toString() + grupoPasantia.getGestionAcademica().getPeriodo().getPeriodoEntero().toString() + carreraEstudiante.getCarrera().getId_carrera().toString()) * 10000) + 1;
        } else {
            codigo = maximoCodigo + 1;
        }

        NotaPasantia notaPasantia = new NotaPasantia(fecha, codigo, 0, 0, 0, Condicion.ABANDONO, "", estudiante, grupoPasantia, empresa);
        em.persist(notaPasantia);

        Indicador[] indicadores = Indicador.values();
        for (Indicador indicador : indicadores) {
            IndicadorPasantia indicadorPasantia = new IndicadorPasantia(indicador, Evaluacion.INSUFICIENTE, notaPasantia);
            indicadorPasantiaFacade.create(indicadorPasantia);
        }

        return notaPasantia;
    }
}
