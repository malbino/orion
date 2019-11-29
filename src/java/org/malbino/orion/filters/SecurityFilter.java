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
@WebFilter(urlPatterns = {"/home.xhtml", "/administrador/*"})
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
            String uri = req.getRequestURI();
            List<Recurso> listaRecursos = recursoFacade.buscarPorPersonaUri(usr.getId_persona(), uri);
            if(!listaRecursos.isEmpty()){
                chain.doFilter(request, response);
            } else {
                res.sendRedirect("/orion/page_403.xhtml");
            }
        } else {
            res.sendRedirect("/orion/login.xhtml");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
