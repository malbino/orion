/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.filters;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.controllers.LoginController;
import org.malbino.orion.entities.Recurso;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.facades.RecursoFacade;

/**
 *
 * @author malbino
 */
@WebFilter(urlPatterns = {
    "/administrador/*",
    "/admisiones/*",
    "/estudiante/*",
    "/fileEstudiante/*",
    "/gestionesAcademicas/*",
    "/horarios/*",
    "/inscripciones/*",
    "/pagos/*",
    "/pasantias/*",
    "/planesEstudio/*",
    "/registroDocente/*",
    "/registroNotas/*",
    "/reportes/*",
    "/titulacion/*",
    "/calendarioAcademico.xhtml",
    "/cambiarContrasena.xhtml",
    "/home.xhtml"
})
public class SecurityFilter implements Filter {

    @Inject
    LoginController loginController;
    @EJB
    RecursoFacade recursoFacade;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Usuario usr = loginController.getUsr();
        if (usr != null) {
            String servletPath = req.getServletPath();
            List<Recurso> listaRecursos = recursoFacade.buscarPorPersonaServletPath(usr.getId_persona(), servletPath);
            if (!listaRecursos.isEmpty()) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/page_403.xhtml");
            }
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
