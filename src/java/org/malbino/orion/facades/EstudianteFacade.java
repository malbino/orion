/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.enums.Sexo;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class EstudianteFacade extends AbstractFacade<Estudiante> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public EstudianteFacade() {
        super(Estudiante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Estudiante buscarPorDni(String dni) {
        Estudiante usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE e.dni=:dni");
            q.setParameter("dni", dni);

            usr = (Estudiante) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public Estudiante buscarPorDni(String dni, int id_persona) {
        Estudiante usr = null;

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE e.dni=:dni AND e.id_persona!=:id_persona");
            q.setParameter("dni", dni);
            q.setParameter("id_persona", id_persona);

            usr = (Estudiante) q.getSingleResult();
        } catch (Exception e) {

        }

        return usr;
    }

    public List<Estudiante> listaEstudiantes() {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Estudiante> buscar(String keyword) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e WHERE "
                    + "CAST(e.matricula AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "CAST(e.telefono AS CHAR) LIKE LOWER(:keyword) OR "
                    + "CAST(e.celular AS CHAR) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Estudiante> buscar(int id_carrera, String keyword) {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e, CarreraEstudiante ce WHERE (e.id_persona=ce.carreraEstudianteId.id_persona) AND ce.carreraEstudianteId.id_carrera=:id_carrera AND "
                    + "(CAST(e.matricula AS CHAR) LIKE :keyword OR "
                    + "LOWER(FUNCTION('REPLACE', CONCAT(e.primerApellido, e.segundoApellido, e.nombre), ' ', '')) LIKE :keyword OR "
                    + "LOWER(e.dni) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.direccion) LIKE LOWER(:keyword) OR "
                    + "CAST(e.telefono AS CHAR) LIKE :keyword OR "
                    + "CAST(e.celular AS CHAR) LIKE :keyword OR "
                    + "LOWER(e.email) LIKE LOWER(:keyword)) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword.replace(" ", "").toLowerCase() + "%");

            l = q.getResultList();
        } catch (Exception e) {
            
        }

        return l;
    }

    public List<Estudiante> listaEstudiantesCentralizadorCalificaciones() {
        List<Estudiante> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Integer maximaMatricula(Date fecha) {
        Integer i = null;

        try {
            Query q = em.createQuery("SELECT MAX(e.matricula) FROM Estudiante e WHERE e.fecha BETWEEN :inicio AND :fin");
            q.setParameter("inicio", Fecha.getInicioAño(fecha));
            q.setParameter("fin", Fecha.getFinAño(fecha));

            i = (Integer) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public Long cantidadEstudiantes() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(e) FROM Estudiante e");

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadEstudiantes(Sexo sexo) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(e) FROM Estudiante e WHERE e.sexo=:sexo");
            q.setParameter("sexo", sexo);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
}
