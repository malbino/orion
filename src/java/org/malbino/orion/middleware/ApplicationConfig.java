/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.middleware;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.malbino.orion.middleware.filters.CorsFilter;

/**
 *
 * @author Tincho
 */
@javax.ws.rs.ApplicationPath("middleware")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        resources.add(CorsFilter.class);
        
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.malbino.orion.middleware.EmpleadoFacadeREST.class);
        resources.add(org.malbino.orion.middleware.EstudianteFacadeREST.class);
    }
    
}
