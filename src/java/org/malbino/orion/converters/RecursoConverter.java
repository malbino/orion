/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Recurso;
import org.malbino.orion.facades.RecursoFacade;

/**
 *
 * @author malbino
 */
@Named(value = "RecursoConverter")
@RequestScoped
public class RecursoConverter implements Converter {

    @EJB
    RecursoFacade recursoFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        Object o;

        if (submittedValue == null || submittedValue.isEmpty()) {
            o = null;
        } else {
            o = recursoFacade.find(Integer.valueOf(submittedValue));
        }

        return o;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String s;

        if (value == null || value.equals("")) {
            s = "";
        } else {
            s = String.valueOf(((Recurso) value).getId_recurso());
        }

        return s;
    }

}
