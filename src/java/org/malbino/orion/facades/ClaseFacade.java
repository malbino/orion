/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.Iterator;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Aula;
import org.malbino.orion.entities.Clase;
import org.malbino.orion.entities.Empleado;
import org.malbino.orion.entities.Periodo;
import org.malbino.orion.enums.Dia;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class ClaseFacade extends AbstractFacade<Clase> {
    
    private static final Logger log = LoggerFactory.getLogger(ClaseFacade.class);
    
    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;
    
    public ClaseFacade() {
        super(Clase.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public Clase buscar(int id_gestionacademica, int id_carrera, Nivel nivel, Turno turno, String paralelo, Periodo periodo, Dia dia) {
        Clase c = null;
        
        try {
            Query q = em.createQuery("SELECT c FROM Clase c JOIN c.grupo g JOIN g.gestionAcademica ga JOIN g.materia m JOIN m.carrera a WHERE ga.id_gestionacademica=:id_gestionacademica AND a.id_carrera=:id_carrera AND m.nivel=:nivel AND g.turno=:turno AND g.codigo=:paralelo AND c.periodo=:periodo AND c.dia=:dia");
            q.setParameter("id_gestionacademica", id_gestionacademica);
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("nivel", nivel);
            q.setParameter("turno", turno);
            q.setParameter("paralelo", paralelo);
            q.setParameter("periodo", periodo);
            q.setParameter("dia", dia);
            
            q.setMaxResults(1);
            
            List<Clase> l = q.getResultList();
            Iterator<Clase> i = l.iterator();
            if (i.hasNext()) {
                c = i.next();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        
        return c;
    }
    
    public Clase buscar(Periodo periodo, Dia dia, Aula aula) {
        Clase c = null;
        
        try {
            Query q = em.createQuery("SELECT c FROM Clase c JOIN c.grupo g JOIN g.gestionAcademica ga WHERE ga.vigente=:vigente AND c.periodo=:periodo AND c.dia=:dia AND c.aula=:aula");
            q.setParameter("vigente", Boolean.TRUE);
            q.setParameter("periodo", periodo);
            q.setParameter("dia", dia);
            q.setParameter("aula", aula);
            
            q.setMaxResults(1);
            
            List<Clase> l = q.getResultList();
            Iterator<Clase> i = l.iterator();
            if (i.hasNext()) {
                c = i.next();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        
        return c;
    }
    
    public Clase buscar(Periodo periodo, Dia dia, Empleado empleado) {
        Clase c = null;
        
        try {
            Query q = em.createQuery("SELECT c FROM Clase c JOIN c.grupo g JOIN g.gestionAcademica ga WHERE ga.vigente=:vigente AND c.periodo=:periodo AND c.dia=:dia AND g.empleado=:empleado");
            q.setParameter("vigente", Boolean.TRUE);
            q.setParameter("periodo", periodo);
            q.setParameter("dia", dia);
            q.setParameter("empleado", empleado);
            
            q.setMaxResults(1);
            
            List<Clase> l = q.getResultList();
            Iterator<Clase> i = l.iterator();
            if (i.hasNext()) {
                c = i.next();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        
        return c;
    }
    
    public Clase buscar(Periodo periodo, Dia dia, Aula aula, int id_clase) {
        Clase c = null;
        
        try {
            Query q = em.createQuery("SELECT c FROM Clase c JOIN c.grupo g JOIN g.gestionAcademica ga WHERE ga.vigente=:vigente AND c.periodo=:periodo AND c.dia=:dia AND c.aula=:aula AND c.id_clase!=:id_clase");
            q.setParameter("vigente", Boolean.TRUE);
            q.setParameter("periodo", periodo);
            q.setParameter("dia", dia);
            q.setParameter("aula", aula);
            q.setParameter("id_clase", id_clase);
            
            q.setMaxResults(1);
            
            List<Clase> l = q.getResultList();
            Iterator<Clase> i = l.iterator();
            if (i.hasNext()) {
                c = i.next();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        
        return c;
    }
    
    public Clase buscar(Periodo periodo, Dia dia, Empleado empleado, int id_clase) {
        Clase c = null;
        
        try {
            Query q = em.createQuery("SELECT c FROM Clase c JOIN c.grupo g JOIN g.gestionAcademica ga WHERE ga.vigente=:vigente AND c.periodo=:periodo AND c.dia=:dia AND g.empleado=:empleado AND c.id_clase!=:id_clase");
            q.setParameter("vigente", Boolean.TRUE);
            q.setParameter("periodo", periodo);
            q.setParameter("dia", dia);
            q.setParameter("empleado", empleado);
            q.setParameter("id_clase", id_clase);
            
            q.setMaxResults(1);
            
            List<Clase> l = q.getResultList();
            Iterator<Clase> i = l.iterator();
            if (i.hasNext()) {
                c = i.next();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        
        return c;
    }
}
