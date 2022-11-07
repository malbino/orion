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
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.facades.GrupoPasantiaFacade;

/**
 *
 * @author malbino
 */
@Named(value = "GrupoPasantiaConverter")
@RequestScoped
public class GrupoPasantiaConverter implements Converter {

    @EJB
    GrupoPasantiaFacade grupoFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        Object o;

        if (submittedValue == null || submittedValue.isEmpty()) {
            o = null;
        } else {
            o = grupoFacade.find(Integer.valueOf(submittedValue));
        }

        return o;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String s;

        if (value == null || value.equals("")) {
            s = "";
        } else {
            s = String.valueOf(((GrupoPasantia) value).getId_grupopasantia());
        }

        return s;
    }

}
