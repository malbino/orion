/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.malbino.orion.enums.NivelAcademico;

/**
 *
 * @author malbino
 */
@Named(value = "NivelAcademicoConverter")
@RequestScoped
public class NivelAcademicoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        Object o;

        if (submittedValue == null || submittedValue.isEmpty()) {
            o = null;
        } else {
            o = NivelAcademico.valueOf(submittedValue);
        }

        return o;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String s;

        if (value == null || value.equals("")) {
            s = "";
        } else {
            s = String.valueOf(((NivelAcademico) value).name());
        }

        return s;
    }

}
