/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.CarreraEstudiante;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class CarreraEstudianteFacade extends AbstractFacade<CarreraEstudiante> {
    
    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;
    
    @EJB
    CarreraFacade carreraFacade;
    
    public CarreraEstudianteFacade() {
        super(CarreraEstudiante.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<CarreraEstudiante> listaCarrerasEstudiante(int id_persona) {
        List<CarreraEstudiante> l = new ArrayList();
        
        try {
            Query q = em.createQuery("SELECT ce FROM CarreraEstudiante ce WHERE ce.carreraEstudianteId.id_persona=:id_persona");
            q.setParameter("id_persona", id_persona);
            
            l = q.getResultList();
            
            for (CarreraEstudiante carreraEstudiante : l) {
                Carrera carrera = carreraFacade.find(carreraEstudiante.getCarreraEstudianteId().getId_carrera());
                carreraEstudiante.setCarrera(carrera);
            }
        } catch (Exception e) {
            
        }
        
        return l;
    }
    
    public CarreraEstudiante buscarCarrerasEstudiante(int id_carrera, int id_persona) {
        CarreraEstudiante ce = null;
        
        try {
            Query q = em.createQuery("SELECT ce FROM CarreraEstudiante ce WHERE ce.carreraEstudianteId.id_carrera=:id_carrera AND ce.carreraEstudianteId.id_persona=:id_persona");
            q.setParameter("id_carrera", id_carrera);
            q.setParameter("id_persona", id_persona);
            
            ce = (CarreraEstudiante) q.getSingleResult();
        } catch (Exception e) {
            
        }
        
        return ce;
    }
    
}
