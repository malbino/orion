/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.File;
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
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.facades.InstitutoFacade;
import org.malbino.orion.util.Constantes;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Tincho
 */
@Named("InstitutoController")
@SessionScoped
public class InstitutoController extends AbstractController implements Serializable {

    @EJB
    InstitutoFacade insitutoFacade;

    private Instituto instituto;

    @PostConstruct
    public void init() {
        instituto = insitutoFacade.buscarPorId(Constantes.ID_INSTITUTO);
    }

    public void reinit() {
        instituto = insitutoFacade.buscarPorId(Constantes.ID_INSTITUTO);
    }

    public void subirArchivo(FileUploadEvent event) {
        Path folder = Paths.get(System.getProperty("catalina.base") + File.separator + "docroot" + File.separator + "files");
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        Path file = null;
        try (InputStream input = event.getFile().getInputstream()) {
            file = Files.createTempFile(folder, null, "." + extension);
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);

            instituto.setLogo(file.getFileName().toString());
        } catch (IOException ex) {
            Logger.getLogger(InstitutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarInstituto() {
        if (insitutoFacade.edit(instituto)) {
            reinit();

            this.mensajeDeInformacion("Guardado.");
        }
    }
    
    /**
     * @return the instituto
     */
    public Instituto getInstituto() {
        return instituto;
    }

    /**
     * @param instituto the instituto to set
     */
    public void setInstituto(Instituto instituto) {
        this.instituto = instituto;
    }
}
