/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.middleware;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.malbino.orion.entities.Empleado;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.facades.AbstractFacade;
import org.malbino.orion.facades.InstitutoFacade;
import org.malbino.orion.util.Constantes;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tincho
 */
@Stateless
@Path("empleado")
public class EmpleadoFacadeREST extends AbstractFacade<Empleado> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EmpleadoFacadeREST.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    @EJB
    InstitutoFacade institutoFacade;

    public EmpleadoFacadeREST() {
        super(Empleado.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @GET
    @Path("listaEmpleados/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaEmpleados(@PathParam("token") String token) {
        List<Empleado> l = new ArrayList();

        Instituto instituto = institutoFacade.buscarPorId(Constantes.ID_INSTITUTO);
        if (token.equals(instituto.getToken())) {
            try {
                Query q = em.createQuery("SELECT new org.malbino.orion.middleware.pojos.Empleado_(e.id_persona, e.nombre, e.primerApellido, e.segundoApellido, e.dni, e.lugarExpedicion, e.fechaNacimiento, e.lugarNacimiento, e.nacionalidad, e.sexo, e.direccion, e.telefono, e.celular, e.email, e.foto, e.usuario, e.contrasena, e.codigo, e.abreviaturaProfesion) FROM Empleado e WHERE e.usuario IS NOT NULL ORDER BY e.primerApellido, e.segundoApellido, e.nombre");

                l = q.getResultList();
            } catch (Exception e) {
                log.error("listaEmpleados\n" + e.getMessage());
            }
        }

        return new Gson().toJson(l);
    }

}
