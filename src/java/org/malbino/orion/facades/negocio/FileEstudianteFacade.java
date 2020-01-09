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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.facades.RolFacade;
import org.malbino.orion.util.Constantes;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class FileEstudianteFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    RolFacade rolFacade;
    @EJB
    MateriaFacade materiaFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarParcial(Nota nota) {
        Integer sum = 0;
        if (nota.getPrimerParcial() != null) {
            sum += nota.getPrimerParcial();
        }
        if (nota.getSegundoParcial() != null) {
            sum += nota.getSegundoParcial();
        }
        if (nota.getTercerParcial() != null) {
            sum += nota.getTercerParcial();
        }
        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getCuartoParcial() != null) {
                sum += nota.getCuartoParcial();
            }
        }
        Double promedio = sum.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
        Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
        nota.setNotaFinal(promedioRedondeado);

        if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
            nota.setCondicion(Condicion.APROBADO);
        } else {
            nota.setCondicion(Condicion.REPROBADO);
        }

        em.merge(nota);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarRecuperatorio(Nota nota) {
        if (nota.getRecuperatorio() != null) {
            if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }

        em.merge(nota);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarPago(Pago pago) {
        if (pago.getMonto() > 0) {
            pago.setPagado(Boolean.FALSE);
        } else {
            pago.setPagado(Boolean.TRUE);
        }

        em.merge(pago);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarEstudiante(Estudiante estudiante) {
        Integer c1 = estudianteFacade.cantidadEstudiantes(estudiante.getFecha()).intValue() + 1;
        String codigo = Fecha.extrarAño(estudiante.getFecha()) + String.format("%04d", c1);
        estudiante.setCodigo(codigo);
        estudiante.setUsuario(codigo);
        List<Rol> roles = new ArrayList();
        roles.add(rolFacade.find(Constantes.ID_ROL_ESTUDIANTE));
        estudiante.setRoles(roles);
        em.persist(estudiante);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> oferta(Carrera carrera, Estudiante estudiante) {
        List<Materia> oferta = new ArrayList();

        List<Materia> listaMateriasCarrera = materiaFacade.listaMaterias(carrera.getId_carrera());
        List<Materia> listaMateriaAprobadas = materiaFacade.listaMateriaAprobadas(estudiante.getId_persona(), carrera.getId_carrera());
        listaMateriasCarrera.removeAll(listaMateriaAprobadas);

        for (Materia materia : listaMateriasCarrera) {
            List<Materia> prerequisitos = materia.getPrerequisitos();
            if (listaMateriaAprobadas.containsAll(prerequisitos)) {
                oferta.add(materia);
            }
        }

        return oferta;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean crearNota(Nota nota) {
        if (nota.getNotaFinal() != null) {
            if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }

        em.persist(nota);

        return true;
    }
}
