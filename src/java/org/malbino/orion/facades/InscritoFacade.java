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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Sexo;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class InscritoFacade extends AbstractFacade<Inscrito> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public InscritoFacade() {
        super(Inscrito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Inscrito> listaInscritos(int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona ORDER BY c.nombre, ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Inscrito buscarInscrito(int id_persona, int id_carrera, int id_gestionacademica) {
        Inscrito i = null;
        
        System.out.println("id_persona: " + id_persona);
        System.out.println("id_carrera: " + id_carrera);
        System.out.println("id_gestionacademica: " + id_gestionacademica);

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera AND ga.id_gestionacademica=:id_gestionacademica");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_gestionacademica", id_gestionacademica);

            i = (Inscrito) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public List<Inscrito> listaInscritosPruebaRecuperacion(GestionAcademica gestionAcademica, int id_persona) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN n.grupo g JOIN g.empleado e WHERE ga.id_gestionacademica=:id_gestionacademica AND e.id_persona=:id_persona AND n.notaFinal<:notaMinimaAprobacion GROUP BY i HAVING COUNT(n) >= 1 AND COUNT(n) <=:cantidadMaximaReprobaciones");
            q.setParameter("id_gestionacademica", gestionAcademica.getId_gestionacademica());
            q.setParameter("id_persona", id_persona);
            q.setParameter("notaMinimaAprobacion", gestionAcademica.getRegimen().getNotaMinimaAprobacion());
            q.setParameter("cantidadMaximaReprobaciones", gestionAcademica.getRegimen().getCantidadMaximaReprobaciones());

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY i.numero");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
    
    public List<Inscrito> listaInscritosCarrera(int id_gestionacademica, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Inscrito> listaInscritosPorEstudianteCarrera(int id_persona, int id_carrera) {
        List<Inscrito> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT i FROM Inscrito i JOIN i.estudiante e JOIN i.carrera c JOIN i.gestionAcademica ga WHERE e.id_persona=:id_persona AND c.id_carrera=:id_carrera ORDER BY ga.gestion, ga.periodo");
            q.setParameter("id_persona", id_persona);
            q.setParameter("id_carrera", id_carrera);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public Integer maximoNumero(int id_gestioncademica, int id_carrera) {
        Integer i = null;

        try {
            Query q = em.createQuery("SELECT MAX(i.numero) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            i = (Integer) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }

    public Integer maximoCodigo(int id_gestioncademica, int id_carrera) {
        Integer i = null;

        try {
            Query q = em.createQuery("SELECT MAX(i.codigo) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera");
            q.setParameter("id_gestionacademica", id_gestioncademica);
            q.setParameter("id_carrera", id_carrera);

            i = (Integer) q.getSingleResult();
        } catch (Exception e) {

        }

        return i;
    }
    
    public Long cantidadInscritos() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE");

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritos(Sexo sexo) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.estudiante e WHERE ga.vigente=TRUE AND e.sexo=:sexo");
            q.setParameter("sexo", sexo);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritosEfectivos() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(i)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE AND n.notaFinal!=0");

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritosReprobados() {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(i)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga WHERE ga.vigente=TRUE AND n.condicion=:condicion");
            q.setParameter("condicion", Condicion.REPROBADO);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritos(int id_carrera) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(i) FROM Inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera");
            q.setParameter("id_carrera", id_carrera);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritos(int id_carrera, Nivel nivel) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(e)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN n.materia m JOIN i.estudiante e WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritos(int id_carrera, Nivel nivel, Sexo sexo) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(e)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN n.materia m JOIN i.estudiante e WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND e.sexo=:sexo");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("sexo", sexo);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritosEfectivos(int id_carrera, Nivel nivel) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(e)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN n.materia m JOIN i.estudiante e WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND n.notaFinal!=0");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public Long cantidadInscritosReprobados(int id_carrera, Nivel nivel) {
        Long l = 0l;

        try {
            Query q = em.createQuery("SELECT COUNT(DISTINCT(e)) FROM Nota n JOIN n.inscrito i JOIN i.gestionAcademica ga JOIN i.carrera c JOIN n.materia m JOIN i.estudiante e WHERE ga.vigente=TRUE AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND n.condicion=:condicion");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("condicion", Condicion.REPROBADO);

            l = (Long) q.getSingleResult();
        } catch (Exception e) {
            
        }

        return l;
    }
    
    public List<Inscrito> listaInscritos(int id_gestionacademica, int id_carrera, Nivel nivel, String paralelo) {
        List<Inscrito> l = new ArrayList<>();

        try {
            Query q = em.createQuery("SELECT DISTINCT i FROM Nota n JOIN n.grupo g JOIN g.materia m JOIN m.carrera c JOIN g.gestionAcademica ga JOIN n.inscrito i JOIN i.estudiante e WHERE ga.id_gestionacademica=:id_gestionacademica AND c.id_carrera=:id_carrera AND m.nivel=:nivel AND g.codigo=:paralelo ORDER BY e.primerApellido, e.segundoApellido, e.nombre");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("paralelo", paralelo);

            l = q.getResultList();
        } catch (Exception e) {
            
        }

        return l;
    }
    
}
