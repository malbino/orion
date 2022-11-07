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
import org.malbino.orion.entities.IndicadorPasantia;
import org.malbino.orion.facades.IndicadorPasantiaFacade;

/**
 *
 * @author malbino
 */
@Named(value = "IndicadorPasantiaConverter")
@RequestScoped
public class IndicadorPasantiaConverter implements Converter {

    @EJB
    IndicadorPasantiaFacade indicadorPasantiaFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        Object o;

        if (submittedValue == null || submittedValue.isEmpty()) {
            o = null;
        } else {
            o = indicadorPasantiaFacade.find(Integer.valueOf(submittedValue));
        }

        return o;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String s;

        if (value == null || value.equals("")) {
            s = "";
        } else {
            s = String.valueOf(((IndicadorPasantia) value).getId_indicadorpasantia());
        }

        return s;
    }

}
