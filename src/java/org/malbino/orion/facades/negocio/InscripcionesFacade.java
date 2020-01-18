/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.enums.Caracter;
import org.malbino.orion.enums.Concepto;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Tipo;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.facades.NotaFacade;
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
public class InscripcionesFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    MateriaFacade materiaFacade;
    @EJB
    RolFacade rolFacade;
    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    EstudianteFacade estudianteFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarEstudianteNuevo(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        Integer c1 = estudianteFacade.cantidadEstudiantes(estudiante.getFecha()).intValue() + 1;
        String matricula = Fecha.extrarAño(estudiante.getFecha()) + String.format("%04d", c1);
        estudiante.setMatricula(matricula);
        estudiante.setUsuario(matricula);
        List<Rol> roles = new ArrayList();
        roles.add(rolFacade.find(Constantes.ID_ROL_ESTUDIANTE));
        estudiante.setRoles(roles);
        List<Carrera> carreras = new ArrayList();
        carreras.add(carrera);
        estudiante.setCarreras(carreras);
        em.persist(estudiante);

        Date fecha = Fecha.getDate();
        Integer c2 = inscritoFacade.cantidadInscritos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera()).intValue() + 1;
        String codigo = gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera() + String.format("%04d", c2);
        Inscrito inscrito = new Inscrito(fecha, Tipo.NUEVO, codigo, c2, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, false, inscrito);
            em.persist(pago);
        } else {
            Long creditajeMaterias = creditajeOferta(inscrito);
            Integer monto = creditajeMaterias.intValue() * carrera.getCampus().getInstituto().getPrecioCredito();
            Integer cuotas = carrera.getRegimen().getCuotas();
            for (Concepto concepto : Concepto.values(carrera.getRegimen())) {
                Double montoCuotaSinRedondear = monto.doubleValue() / cuotas.doubleValue();
                Integer montoCuotaRedondeado = Redondeo.redondear_UP(montoCuotaSinRedondear, 0).intValue();

                Pago pago = new Pago(concepto, montoCuotaRedondeado, false, inscrito);
                em.persist(pago);

                monto -= montoCuotaRedondeado;
                cuotas--;
            }
        }
        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarEstudianteRegular(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        estudiante.setContrasena(null);
        em.merge(estudiante);

        Date fecha = Fecha.getDate();
        Integer c1 = inscritoFacade.cantidadInscritos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera()).intValue() + 1;
        String codigo = gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera() + String.format("%04d", c1);
        Inscrito inscrito = new Inscrito(fecha, Tipo.REGULAR, codigo, c1, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, false, inscrito);
            em.persist(pago);
        } else {
            Long creditajeMaterias = creditajeOferta(inscrito);
            Integer monto = creditajeMaterias.intValue() * carrera.getCampus().getInstituto().getPrecioCredito();
            Integer cuotas = carrera.getRegimen().getCuotas();
            for (Concepto concepto : Concepto.values(carrera.getRegimen())) {
                Double montoCuotaSinRedondear = monto.doubleValue() / cuotas.doubleValue();
                Integer montoCuotaRedondeado = Redondeo.redondear_UP(montoCuotaSinRedondear, 0).intValue();

                Pago pago = new Pago(concepto, montoCuotaRedondeado, false, inscrito);
                em.persist(pago);

                monto -= montoCuotaRedondeado;
                cuotas--;
            }
        }

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean cambioCarrera(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica) {
        estudiante.setContrasena(null);
        estudiante.getCarreras().add(carrera);
        em.merge(estudiante);

        Date fecha = Fecha.getDate();
        Integer c1 = inscritoFacade.cantidadInscritos(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera()).intValue() + 1;
        String codigo = gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera() + String.format("%04d", c1);
        Inscrito inscrito = new Inscrito(fecha, Tipo.NUEVO, codigo, c1, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, false, inscrito);
            em.persist(pago);
        } else {
            Long creditajeMaterias = creditajeOferta(inscrito);
            Integer monto = creditajeMaterias.intValue() * carrera.getCampus().getInstituto().getPrecioCredito();
            Integer cuotas = carrera.getRegimen().getCuotas();
            for (Concepto concepto : Concepto.values(carrera.getRegimen())) {
                Double montoCuotaSinRedondear = monto.doubleValue() / cuotas.doubleValue();
                Integer montoCuotaRedondeado = Redondeo.redondear_UP(montoCuotaSinRedondear, 0).intValue();

                Pago pago = new Pago(concepto, montoCuotaRedondeado, false, inscrito);
                em.persist(pago);

                monto -= montoCuotaRedondeado;
                cuotas--;
            }
        }

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Long creditajeOferta(Inscrito inscrito) {
        Long l = 0L;

        List<Materia> oferta = oferta(inscrito);
        for (Materia materia : oferta) {
            l += materia.getCreditajeMateria();
        }

        return l;

    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> oferta(Inscrito inscrito) {
        List<Materia> oferta = new ArrayList();

        List<Materia> listaMateriaAprobadas = materiaFacade.listaMateriaAprobadas(inscrito.getEstudiante().getId_persona(), inscrito.getCarrera().getId_carrera());
        List<Nivel> nivelesPendientes = materiaFacade.nivelesPendientes(inscrito.getEstudiante().getId_persona(), inscrito.getCarrera().getId_carrera());

        ListIterator<Nivel> listIterator = nivelesPendientes.listIterator();
        List<Materia> listaMaterias;
        if (listIterator.hasNext()) {
            listaMaterias = materiaFacade.listaMaterias(inscrito.getCarrera().getId_carrera(), listIterator.next());
            listaMaterias.removeAll(listaMateriaAprobadas);

            for (Materia materia : listaMaterias) {
                List<Materia> prerequisitos = materia.getPrerequisitos();
                if (listaMateriaAprobadas.containsAll(prerequisitos)) {
                    oferta.add(materia);
                }
            }
        }
        if (listIterator.hasNext() && oferta.size() <= inscrito.getCarrera().getRegimen().getCantidadMaximaReprobaciones()) {
            listaMaterias = materiaFacade.listaMaterias(inscrito.getCarrera().getId_carrera(), listIterator.next());
            listaMaterias.removeAll(listaMateriaAprobadas);

            for (Materia materia : listaMaterias) {
                List<Materia> prerequisitos = materia.getPrerequisitos();
                if (listaMateriaAprobadas.containsAll(prerequisitos)) {
                    oferta.add(materia);
                }
            }
        }

        return oferta;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean tomarMaterias(List<Nota> notas) {
        for (Nota nota : notas) {
            Grupo grupo = nota.getGrupo();
            long cantidadNotasGrupo = grupoFacade.cantidadNotasGrupo(grupo.getId_grupo());
            if (cantidadNotasGrupo + 1 < grupo.getCapacidad()) {
                em.persist(nota);
            } else if (cantidadNotasGrupo + 1 == grupo.getCapacidad()) {
                em.persist(nota);

                grupo.setAbierto(Boolean.FALSE);
                em.merge(grupo);
            } else {
                throw new EJBException("Grupo(s) lleno(s).");
            }
        }

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> ofertaTomaMaterias(Inscrito inscrito) {
        List<Materia> ofertaTomaMaterias = oferta(inscrito);

        List<Nota> estadoInscripcion = notaFacade.listaNotas(inscrito.getId_inscrito());
        for (Nota nota : estadoInscripcion) {
            ofertaTomaMaterias.remove(nota.getMateria());
        }

        return ofertaTomaMaterias;

    }
}
