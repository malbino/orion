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
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Egresado;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.facades.EgresadoFacade;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.facades.NotaFacade;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class TitulacionFacade {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    MateriaFacade materiaFacade;
    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    EgresadoFacade egresadoFacade;

    public void actualizarEgresados(Carrera carrera, Mencion mencion) {
        List<Materia> listaMaterias = materiaFacade.listaMaterias(carrera, mencion);

        List<Estudiante> listaEstudiantes = estudianteFacade.listaEstudiantes(carrera.getId_carrera());
        for (Estudiante estudiante : listaEstudiantes) {
            List<Materia> listaMateriasAprobadas = materiaFacade.listaMateriaAprobadas(estudiante, carrera, mencion);

            if (listaMateriasAprobadas.containsAll(listaMaterias)) {
                GestionAcademica finFormacion = notaFacade.finFormacion(carrera, mencion, estudiante);

                Egresado g = egresadoFacade.buscar(estudiante, carrera, mencion, finFormacion);
                if (g == null) {
                    Egresado egresado = new Egresado(estudiante, carrera, mencion, finFormacion);
                    em.persist(egresado);
                }
            }
        }
    }
}
