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
        Integer nota1 = 0;
        if (nota.getTeoria1() != null) {
            nota1 += nota.getTeoria1();
        }
        if (nota.getPractica1() != null) {
            nota1 += nota.getPractica1();
        }
        if (nota.getTeoria1() == null && nota.getPractica1() == null) {
            nota.setNota1(null);
        } else {
            nota.setNota1(nota1);
        }

        Integer nota2 = 0;
        if (nota.getTeoria2() != null) {
            nota2 += nota.getTeoria2();
        }
        if (nota.getPractica2() != null) {
            nota2 += nota.getPractica2();
        }
        if (nota.getTeoria2() == null && nota.getPractica2() == null) {
            nota.setNota2(null);
        } else {
            nota.setNota2(nota2);
        }

        Integer nota3 = 0;
        if (nota.getTeoria3() != null) {
            nota3 += nota.getTeoria3();
        }
        if (nota.getPractica3() != null) {
            nota3 += nota.getPractica3();
        }
        if (nota.getTeoria3() == null && nota.getPractica3() == null) {
            nota.setNota3(null);
        } else {
            nota.setNota3(nota3);
        }

        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            Integer nota4 = 0;
            if (nota.getTeoria4() != null) {
                nota4 += nota.getTeoria4();
            }
            if (nota.getPractica4() != null) {
                nota4 += nota.getPractica4();
            }
            if (nota.getTeoria4() == null && nota.getPractica4() == null) {
                nota.setNota4(null);
            } else {
                nota.setNota4(nota4);
            }
        }

        Integer notaFinal = 0;
        if (nota.getNota1() != null) {
            notaFinal += nota.getNota1();
        }
        if (nota.getNota2() != null) {
            notaFinal += nota.getNota2();
        }
        if (nota.getNota3() != null) {
            notaFinal += nota.getNota3();
        }
        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getNota4() != null) {
                notaFinal += nota.getNota4();
            }
        }

        if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 3) {
            if (nota.getNota1() == null && nota.getNota2() == null && nota.getNota3() == null) {
                nota.setNotaFinal(null);
                nota.setCondicion(Condicion.REPROBADO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                    nota.setCondicion(Condicion.APROBADO);
                } else {
                    nota.setCondicion(Condicion.REPROBADO);
                }
            }
        } else if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getNota1() == null && nota.getNota2() == null && nota.getNota3() == null && nota.getNota4() == null) {
                nota.setNotaFinal(null);
                nota.setCondicion(Condicion.REPROBADO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                    nota.setCondicion(Condicion.APROBADO);
                } else {
                    nota.setCondicion(Condicion.REPROBADO);
                }
            }
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
        } else if (nota.getNotaFinal() != null) {
            if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
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
        if (nota.getRecuperatorio() != null) {
            if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        } else if (nota.getNotaFinal() != null) {
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
