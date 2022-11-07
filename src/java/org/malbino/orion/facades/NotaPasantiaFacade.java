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
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.NotaPasantia;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class NotaPasantiaFacade extends AbstractFacade<NotaPasantia> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NotaPasantiaFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public NotaPasantiaFacade() {
        super(NotaPasantia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public NotaPasantia buscarNotaPasantia(Estudiante estudiante, GrupoPasantia grupoPasantia, Empresa empresa) {
        NotaPasantia i = null;

        try {
            Query q = em.createQuery("SELECT np FROM NotaPasantia np WHERE np.estudiante=:estudiante AND np.grupoPasantia=:grupoPasantia AND np.empresa=:empresa");
            q.setParameter("estudiante", estudiante);
            q.setParameter("grupoPasantia", grupoPasantia);
            q.setParameter("empresa", empresa);

            i = (NotaPasantia) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public Long maximoCodigo(int id_gestioncademica, int id_carrera) {
        Long l = null;

        try {
            Query q = em.createQuery("SELECT MAX(np.codigo) FROM NotaPasantia np JOIN np.grupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            log.error("maximoCodigo\n" + e.getMessage());
        }

        return l;
    }

    public List<NotaPasantia> listaNotasPasantias(int id_grupopasantia) {
        List<NotaPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT np FROM NotaPasantia np JOIN np.grupoPasantia gp JOIN np.estudiante e WHERE gp.id_grupopasantia=:id_grupopasantia ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_grupopasantia", id_grupopasantia);

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaNotasPasantias\n" + e.getMessage());
        }

        return l;
    }

    public List<NotaPasantia> listaNotaPasantiasEstudiante(int id_persona) {
        List<NotaPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT np FROM NotaPasantia np JOIN np.estudiante e JOIN np.grupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE e.id_persona=:id_persona ORDER BY c.nombre, ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaNotaPasantiasEstudiante\n" + e.getMessage());
        }

        return l;
    }

    public List<NotaPasantia> buscar(GrupoPasantia grupoPasantia, String keyword) {
        List<NotaPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT np FROM NotaPasantia np JOIN np.estudiante e WHERE np.grupoPasantia=:grupoPasantia AND "
                    + "(CAST(np.codigo AS CHAR) LIKE :keyword OR "
                    + "LOWER(e.primerApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.segundoApellido) LIKE LOWER(:keyword) OR "
                    + "LOWER(e.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("grupoPasantia", grupoPasantia);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }
}
