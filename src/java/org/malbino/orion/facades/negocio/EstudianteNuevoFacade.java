/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.enums.Caracter;
import org.malbino.orion.enums.Concepto;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.enums.Tipo;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Stateless
@LocalBean
public class EstudianteNuevoFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    MateriaFacade materiaFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarEstudianteNuevo(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        long c1 = inscritoFacade.cantidadInscritos(gestionAcademica.getId_gestionacademica(), Tipo.NUEVO);
        String codigo = gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + String.format("%04d", (c1 + 1));
        estudiante.setCodigo(codigo);
        estudiante.setUsuario(codigo);
        em.persist(estudiante);

        Date fecha = Fecha.getDate();
        long c2 = inscritoFacade.cantidadInscritos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        String matricula = gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera() + String.format("%04d", (c2 + 1));
        Inscrito inscrito = new Inscrito(fecha, Tipo.NUEVO, matricula, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, inscrito);
            em.persist(pago);
        } else {
            Long creditajeMaterias = materiaFacade.creditajeMaterias(carrera.getId_carrera(), carrera.getRegimen().equals(Regimen.SEMESTRAL) ? Nivel.PRIMER_SEMESTRE : Nivel.PRIMER_AÑO);
            Integer monto = creditajeMaterias.intValue() * carrera.getCampus().getInstituto().getPrecioCredito();
            Integer cuotas = carrera.getRegimen().getCuotas();
            for (Concepto concepto : Concepto.values(carrera.getRegimen())) {
                Double montoCuotaSinRedondear = monto.doubleValue() / cuotas.doubleValue();
                Integer montoCuotaRedondeado = Redondeo.redondear_UP(montoCuotaSinRedondear, 0).intValue();

                Pago pago = new Pago(concepto, montoCuotaRedondeado, inscrito);
                em.persist(pago);

                monto -= montoCuotaRedondeado;
                cuotas--;
            }
        }
        return true;
    }

}
