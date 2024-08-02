/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Tincho
 */
@Named("PlantillasController")
@SessionScoped
public class PlantillasController extends AbstractController implements Serializable {

    private StreamedContent file;

    @PostConstruct
    public void init() {
    }

    public void reinit() {
    }
    
    public void descargarPlantillaCE() {
        file = DefaultStreamedContent.builder()
                .name("certificado_egreso.xlsx")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/uploads/certificado_egreso.xlsx"))
                .build();
    }

    public void subirPlantillaCE(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/certificado_egreso.xlsx");
        try ( InputStream input = event.getFile().getInputStream()) {
            Files.copy(input, folder, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(PlantillasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarPlantillaCCSemestral() {
        file = DefaultStreamedContent.builder()
                .name("certificado_calificaciones_semestral.xlsx")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/uploads/certificado_calificaciones_semestral.xlsx"))
                .build();
    }

    public void subirPlantillaCCSemestral(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/certificado_calificaciones_semestral.xlsx");
        try ( InputStream input = event.getFile().getInputStream()) {
            Files.copy(input, folder, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(PlantillasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarPlantillaCCAnual() {
        file = DefaultStreamedContent.builder()
                .name("certificado_calificaciones_anual.xlsx")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/uploads/certificado_calificaciones_anual.xlsx"))
                .build();
    }

    public void subirPlantillaCCAnual(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/certificado_calificaciones_anual.xlsx");
        try ( InputStream input = event.getFile().getInputStream()) {
            Files.copy(input, folder, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(PlantillasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the file
     */
    public StreamedContent getFile() {
        return file;
    }

}
