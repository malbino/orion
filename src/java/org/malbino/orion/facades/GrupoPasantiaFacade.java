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
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class GrupoPasantiaFacade extends AbstractFacade<GrupoPasantia> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public GrupoPasantiaFacade() {
        super(GrupoPasantia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<GrupoPasantia> listaGrupoPasantias(int id_gestionacademica, int id_carrera) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY p.nivel, p.mencion, gp.turno, gp.codigo, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<GrupoPasantia> listaGrupoPasantiasAbiertos(int id_gestionacademica, int id_carrera) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND gp.abierto=:abierto ORDER BY p.nivel, p.mencion, gp.turno, gp.codigo, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("abierto", Boolean.TRUE);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GrupoPasantia> listaGrupoPasantias(int id_gestionacademica, int id_carrera, Nivel nivel) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.nivel=:nivel ORDER BY p.nombre, gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadGruposPasantias(int id_gestionacademica, int id_pasantia, Turno turno) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(gp) FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p WHERE ga.id_gestionacademica=:id_gestionacademica AND p.id_pasantia=:id_pasantia AND gp.turno=:turno");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_pasantia", id_pasantia);
            q.setParameter("turno", turno);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GrupoPasantia> buscar(String keyword, int id_gestionacademica, int id_carrera) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND "
                    + "(LOWER(gp.codigo) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.nombre) LIKE LOWER(:keyword)) "
                    + "ORDER BY p.nivel, gp.turno, gp.codigo, p.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
        }

        return l;
    }

    public List<GrupoPasantia> listaGrupoPasantias(int id_gestionacademica, int id_carrera, int id_pasantia) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.id_pasantia=:id_pasantia ORDER BY gp.turno, gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_pasantia", id_pasantia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GrupoPasantia> listaGrupoPasantiasAbiertos(int id_gestionacademica, int id_carrera, int id_pasantia) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.id_pasantia=:id_pasantia AND gp.abierto=TRUE ORDER BY gp.turno, gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_pasantia", id_pasantia);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public long cantidadNotasPasantiaGrupoPasantia(int id_grupopasantia) {
        long l = 0;

        try {
            Query q = em.createQuery("SELECT COUNT(np) FROM NotaPasantia np JOIN np.grupoPasantia gp WHERE gp.id_grupopasantia=:id_grupopasantia");
            q.setParameter("id_grupopasantia", id_grupopasantia);

            l = (long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GrupoPasantia> listaGrupoPasantiasEmpleado(int id_gestionacademica, int id_carrera, int id_persona) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c JOIN gp.empleado e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND e.id_persona=:id_persona ORDER BY p.nombre, gp.turno");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<GrupoPasantia> listaGrupoPasantiasAbiertos(int id_gestionacademica, int id_pasantia, String codigo) {
        List<GrupoPasantia> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT gp FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p WHERE ga.id_gestionacademica=:id_gestionacademica AND p.id_pasantia=:id_pasantia AND gp.codigo=:codigo AND gp.abierto=TRUE ORDER BY gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_pasantia", id_pasantia);
            q.setParameter("codigo", codigo);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Long cantidadGrupoPasantias(int id_carrera, Nivel nivel) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(gp.codigo)) FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND p.nivel=:nivel");
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
            Query q = em.createQuery("SELECT DISTINCT gp.codigo FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.nivel=:nivel ORDER BY gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<String> listaParalelos(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno) {
        List<String> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT DISTINCT gp.codigo FROM GrupoPasantia gp JOIN gp.gestionAcademica ga JOIN gp.pasantia p JOIN p.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND p.nivel=:nivel AND gp.turno=:turno ORDER BY gp.codigo");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
