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
import org.malbino.orion.enums.Turno;

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

    public long cantidadGrupos(int id_gestionacademica, int id_materia) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(g) FROM Grupo g JOIN g.gestionAcademica ga JOIN g.materia m WHERE ga.id_gestionacademica=:id_gestionacademica AND m.id_materia=:id_materia");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_materia", id_materia);

            l = (long) q.getSingleResult();
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
}