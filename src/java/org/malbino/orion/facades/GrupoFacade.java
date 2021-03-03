/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.enums.Nivel;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class GrupoFacade extends AbstractFacade<Grupo> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public GrupoFacade() {
        super(Grupo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Grupo> listaGrupos(int id_gestionacademica, int id_carrera) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY m.nivel, g.turno, g.codigo, m.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Grupo> listaGrupos(int id_gestionacademica, int id_carrera, Nivel nivel) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel ORDER BY m.nombre, g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadGrupos(int id_gestionacademica, int id_materia) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(g) FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m WHERE ga.id_gestionacademica=:id_gestionacademica AND m.id_materia=:id_materia");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_materia", id_materia);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Grupo> buscar(String keyword, int id_gestionacademica, int id_carrera) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND "
                    + "(LOWER(g.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(m.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY m.nivel, g.turno, g.codigo, m.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<Grupo> listaGrupos(int id_gestionacademica, int id_carrera, int id_materia) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.id_materia=:id_materia ORDER BY g.turno, g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Grupo> listaGruposAbiertos(int id_gestionacademica, int id_carrera, int id_materia) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.id_materia=:id_materia AND g.abierto=TRUE ORDER BY g.turno, g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_materia", id_materia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public long cantidadNotasGrupo(int id_grupo) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(n) FROM Nota n JOIN n.grupo g WHERE g.id_grupo=:id_grupo");
            q.setParameter("id_grupo", id_grupo);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Grupo> listaGruposEmpleado(int id_gestionacademica, int id_carrera, int id_persona) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c JOIN g.empleado e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND e.id_persona=:id_persona ORDER BY m.nombre, g.turno");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Grupo> listaGruposAbiertos(int id_gestionacademica, int id_materia, String codigo) {
        List<Grupo> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT g FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m WHERE ga.id_gestionacademica=:id_gestionacademica AND m.id_materia=:id_materia AND g.codigo=:codigo AND g.abierto=TRUE ORDER BY g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_materia", id_materia);
            q.setParameter("codigo", codigo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadGrupos(int id_carrera, Nivel nivel) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(g.codigo)) FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<String> listaParalelos(int id_gestionacademica, int id_carrera, Nivel nivel) {
        List<String> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT g.codigo FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel ORDER BY g.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
