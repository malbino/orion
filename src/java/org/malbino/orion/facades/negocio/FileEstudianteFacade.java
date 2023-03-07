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
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.facades.CarreraEstudianteFacade;
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
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

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
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getRecuperatorio() != null) {
                    if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                } else if (nota.getNotaFinal() != null) {
                    if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else if (nota.getNotaFinal() == 0) {
                        nota.setCondicion(Condicion.ABANDONO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                }
            }
        } else if (nota.getMateria().getCarrera().getRegimen().getCantidadParciales() == 4) {
            if (nota.getNota1() == null && nota.getNota2() == null && nota.getNota3() == null && nota.getNota4() == null) {
                nota.setNotaFinal(null);
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                Double promedio = notaFinal.doubleValue() / nota.getMateria().getCarrera().getRegimen().getCantidadParciales().doubleValue();
                Integer promedioRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
                nota.setNotaFinal(promedioRedondeado);

                if (nota.getRecuperatorio() != null) {
                    if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
                } else if (nota.getNotaFinal() != null) {
                    if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                        nota.setCondicion(Condicion.APROBADO);
                    } else if (nota.getNotaFinal() == 0) {
                        nota.setCondicion(Condicion.ABANDONO);
                    } else {
                        nota.setCondicion(Condicion.REPROBADO);
                    }
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
    public boolean registrarEstudiante(Estudiante estudiante, List<CarreraEstudiante> seleccionCarrerasEstudiante) {
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
        em.flush();

        for (CarreraEstudiante nuevaCarreraEstudiante : seleccionCarrerasEstudiante) {
            nuevaCarreraEstudiante.getCarreraEstudianteId().setId_persona(estudiante.getId_persona());

            CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(nuevaCarreraEstudiante.getCarreraEstudianteId());
            if (carreraEstudiante == null) {
                carreraEstudianteFacade.create(nuevaCarreraEstudiante);
            }
        }

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarEstudiante(Estudiante estudiante, List<CarreraEstudiante> seleccionCarrerasEstudiante) {
        estudianteFacade.edit(estudiante);

        List<CarreraEstudiante> carrerasEstudiante = carreraEstudianteFacade.listaCarrerasEstudiante(estudiante.getId_persona());
        for (CarreraEstudiante carreraEstudiante : carrerasEstudiante) {
            carreraEstudianteFacade.remove(carreraEstudiante);
            carreraEstudianteFacade.getEntityManager().flush();
        }

        for (CarreraEstudiante nuevaCarreraEstudiante : seleccionCarrerasEstudiante) {
            CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(nuevaCarreraEstudiante.getCarreraEstudianteId());
            if (carreraEstudiante == null) {
                carreraEstudianteFacade.create(nuevaCarreraEstudiante);
            }
        }

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Materia> oferta(Carrera carrera, Estudiante estudiante) {
        List<Materia> oferta = new ArrayList();

        CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
        carreraEstudianteId.setId_carrera(carrera.getId_carrera());
        carreraEstudianteId.setId_persona(estudiante.getId_persona());
        CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(carreraEstudianteId);
        if (carreraEstudiante != null) {
            // materias carrera
            List<Materia> listaMateriasCarrera;
            if (carreraEstudiante.getNivelInicio() != null) {
                List<Materia> listaMaterias = materiaFacade.listaMaterias(carrera, carreraEstudiante.getMencion());

                listaMateriasCarrera = new ArrayList<>();
                for (Materia materia : listaMaterias) {
                    if (materia.getNivel().getNivel() >= carreraEstudiante.getNivelInicio().getNivel()) {
                        listaMateriasCarrera.add(materia);
                    }
                }
            } else {
                listaMateriasCarrera = materiaFacade.listaMaterias(carrera, carreraEstudiante.getMencion());
            }

            // quitando materias aprobadas
            List<Materia> listaMateriaAprobadas = materiaFacade.listaMateriaAprobadas(estudiante.getId_persona(), carrera.getId_carrera());
            listaMateriasCarrera.removeAll(listaMateriaAprobadas);

            // control de prerequisitos
            if (carreraEstudiante.getNivelInicio() != null) {
                for (Materia materia : listaMateriasCarrera) {
                    List<Materia> prerequisitos = materia.getPrerequisitos();

                    List<Materia> prerequisitosNivelInicio = new ArrayList<>();
                    for (Materia prerequisito : prerequisitos) {
                        if (prerequisito.getNivel().getNivel() >= carreraEstudiante.getNivelInicio().getNivel()) {
                            prerequisitosNivelInicio.add(prerequisito);
                        }
                    }

                    if (listaMateriaAprobadas.containsAll(prerequisitosNivelInicio)) {
                        oferta.add(materia);
                    }
                }
            } else {
                for (Materia materia : listaMateriasCarrera) {
                    List<Materia> prerequisitos = materia.getPrerequisitos();
                    if (listaMateriaAprobadas.containsAll(prerequisitos)) {
                        oferta.add(materia);
                    }
                }
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
            } else if (nota.getNotaFinal() == 0) {
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }

        em.persist(nota);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean editarNota(Nota nota) {
        if (nota.getRecuperatorio() != null) {
            if (nota.getRecuperatorio() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        } else if (nota.getNotaFinal() != null) {
            if (nota.getNotaFinal() >= nota.getMateria().getCarrera().getRegimen().getNotaMinimaAprobacion()) {
                nota.setCondicion(Condicion.APROBADO);
            } else if (nota.getNotaFinal() == 0) {
                nota.setCondicion(Condicion.ABANDONO);
            } else {
                nota.setCondicion(Condicion.REPROBADO);
            }
        }

        em.merge(nota);

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean eliminarEstudiante(Estudiante estudiante) {
        List<CarreraEstudiante> carrerasEstudiante = carreraEstudianteFacade.listaCarrerasEstudiante(estudiante.getId_persona());
        for (CarreraEstudiante carreraEstudiante : carrerasEstudiante) {
            carreraEstudianteFacade.remove(carreraEstudiante);
            carreraEstudianteFacade.getEntityManager().flush();
        }

        estudianteFacade.remove(estudiante);

        return true;
    }
}
