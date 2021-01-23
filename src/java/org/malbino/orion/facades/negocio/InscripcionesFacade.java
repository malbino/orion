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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Detalle;
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
import org.malbino.orion.facades.ComprobanteFacade;
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
    @EJB
    ComprobanteFacade comprobanteFacade;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean registrarEstudianteNuevo(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica, Comprobante comprobante) {
        Integer maximaMatricula = estudianteFacade.maximaMatricula(estudiante.getFecha());
        Integer matricula;
        if (maximaMatricula == null) {
            matricula = (Fecha.extrarAño(estudiante.getFecha()) * 10000) + 1;
        } else {
            matricula = maximaMatricula + 1;
        }
        estudiante.setMatricula(matricula);
        estudiante.setUsuario(String.valueOf(matricula));
        List<Rol> roles = new ArrayList();
        roles.add(rolFacade.find(Constantes.ID_ROL_ESTUDIANTE));
        estudiante.setRoles(roles);
        List<Carrera> carreras = new ArrayList();
        carreras.add(carrera);
        estudiante.setCarreras(carreras);
        em.persist(estudiante);

        Date fecha = estudiante.getFecha();
        Integer maximoNumero = inscritoFacade.maximoNumero(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer maximoCodigo = inscritoFacade.maximoCodigo(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer codigo;
        Integer numero;
        if (maximoNumero == null && maximoCodigo == null) {
            codigo = (Integer.valueOf(gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera().toString()) * 10000) + 1;
            numero = 1;
        } else {
            codigo = maximoCodigo + 1;
            numero = maximoNumero + 1;
        }
        Inscrito inscrito = new Inscrito(fecha, Tipo.NUEVO, codigo, numero, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, true, inscrito);
            em.persist(pago);

            //comprobante
            Integer c1 = comprobanteFacade.cantidadComprobantes(comprobante.getFecha()).intValue() + 1;
            comprobante.setCodigo(String.format("%05d", c1) + "/" + Fecha.extrarAño(comprobante.getFecha()));
            comprobante.setInscrito(inscrito);
            em.persist(comprobante);

            Detalle detalle = new Detalle(pago.getConcepto(), pago.getMonto(), comprobante, pago);
            em.persist(detalle);
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
    public boolean registrarEstudianteRegular(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica, Comprobante comprobante) {
        if (estudiante.getMatricula() == null && estudiante.getUsuario() == null) {
            Date fecha = notaFacade.fechaInicio(estudiante.getId_persona());
            if (fecha == null) {
                estudiante.setFecha(estudiante.getFechaInscripcion()); //fecha de inscripcion
            } else {
                estudiante.setFecha(fecha);
            }

            Integer maximaMatricula = estudianteFacade.maximaMatricula(estudiante.getFecha());
            Integer matricula;
            if (maximaMatricula == null) {
                matricula = (Fecha.extrarAño(estudiante.getFecha()) * 10000) + 1;
            } else {
                matricula = maximaMatricula + 1;
            }
            estudiante.setMatricula(matricula);
            estudiante.setUsuario(String.valueOf(matricula));
        }

        em.merge(estudiante);

        Date fecha = estudiante.getFechaInscripcion(); //fecha de inscripcion
        Integer maximoNumero = inscritoFacade.maximoNumero(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer maximoCodigo = inscritoFacade.maximoCodigo(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer codigo;
        Integer numero;
        if (maximoNumero == null && maximoCodigo == null) {
            codigo = (Integer.valueOf(gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera().toString()) * 10000) + 1;
            numero = 1;
        } else {
            codigo = maximoCodigo + 1;
            numero = maximoNumero + 1;
        }
        Inscrito inscrito = new Inscrito(fecha, Tipo.REGULAR, codigo, numero, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, true, inscrito);
            em.persist(pago);

            //comprobante
            Integer c1 = comprobanteFacade.cantidadComprobantes(comprobante.getFecha()).intValue() + 1;
            comprobante.setCodigo(String.format("%05d", c1) + "/" + Fecha.extrarAño(comprobante.getFecha()));
            comprobante.setInscrito(inscrito);
            em.persist(comprobante);

            Detalle detalle = new Detalle(pago.getConcepto(), pago.getMonto(), comprobante, pago);
            em.persist(detalle);
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
    public boolean cambioCarrera(Estudiante estudiante, Carrera carrera, GestionAcademica gestionAcademica, Comprobante comprobante) {
        if (estudiante.getMatricula() == null && estudiante.getUsuario() == null) {
            Date fecha = notaFacade.fechaInicio(estudiante.getId_persona());
            if (fecha == null) {
                estudiante.setFecha(estudiante.getFechaInscripcion()); // fecha de inscripcion
            } else {
                estudiante.setFecha(fecha);
            }

            Integer maximaMatricula = estudianteFacade.maximaMatricula(estudiante.getFecha());
            Integer matricula;
            if (maximaMatricula == null) {
                matricula = (Fecha.extrarAño(estudiante.getFecha()) * 10000) + 1;
            } else {
                matricula = maximaMatricula + 1;
            }
            estudiante.setMatricula(matricula);
            estudiante.setUsuario(String.valueOf(matricula));
        }

        estudiante.getCarreras().add(carrera);
        em.merge(estudiante);

        Date fecha = estudiante.getFechaInscripcion(); // fecha de inscripcion
        Integer maximoNumero = inscritoFacade.maximoNumero(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer maximoCodigo = inscritoFacade.maximoCodigo(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        Integer codigo;
        Integer numero;
        if (maximoNumero == null && maximoCodigo == null) {
            codigo = (Integer.valueOf(gestionAcademica.getGestion().toString() + gestionAcademica.getPeriodo().getPeriodoEntero().toString() + carrera.getId_carrera().toString()) * 10000) + 1;
            numero = 1;
        } else {
            codigo = maximoCodigo + 1;
            numero = maximoNumero + 1;
        }
        Inscrito inscrito = new Inscrito(fecha, Tipo.NUEVO, codigo, numero, estudiante, carrera, gestionAcademica);
        em.persist(inscrito);

        if (carrera.getCampus().getInstituto().getCaracter().equals(Caracter.CONVENIO) || carrera.getCampus().getInstituto().getCaracter().equals(Caracter.PUBLICO)) {
            Integer monto = carrera.getCreditajeMatricula() * carrera.getCampus().getInstituto().getPrecioCredito();

            Pago pago = new Pago(Concepto.MATRICULA, monto, true, inscrito);
            em.persist(pago);

            //comprobante
            Integer c1 = comprobanteFacade.cantidadComprobantes(comprobante.getFecha()).intValue() + 1;
            comprobante.setCodigo(String.format("%05d", c1) + "/" + Fecha.extrarAño(comprobante.getFecha()));
            comprobante.setInscrito(inscrito);
            em.persist(comprobante);

            Detalle detalle = new Detalle(pago.getConcepto(), pago.getMonto(), comprobante, pago);
            em.persist(detalle);
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
        if (inscrito.getTipo().equals(Tipo.REGULAR) && listIterator.hasNext() && oferta.size() <= inscrito.getCarrera().getRegimen().getCantidadMaximaReprobaciones()) {
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean retirarMateria(Nota nota) {
        Grupo grupo = nota.getGrupo();
        long cantidadNotasGrupo = grupoFacade.cantidadNotasGrupo(grupo.getId_grupo());

        if (cantidadNotasGrupo < grupo.getCapacidad()) {
            em.remove(em.merge(nota));
        } else if (cantidadNotasGrupo == grupo.getCapacidad()) {
            em.remove(em.merge(nota));

            grupo.setAbierto(Boolean.TRUE);
            em.merge(grupo);
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
