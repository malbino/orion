/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.facades.EmpresaFacade;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Tincho
 */
@Named("EmpresaController")
@SessionScoped
public class EmpresaController extends AbstractController implements Serializable {

    @EJB
    EmpresaFacade empresaFacade;

    private List<Empresa> empresas;
    private Empresa nuevaEmpresa;
    private Empresa seleccionEmpresa;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        empresas = empresaFacade.listaEmpresas();
        nuevaEmpresa = new Empresa();
        seleccionEmpresa = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        empresas = empresaFacade.listaEmpresas();
        nuevaEmpresa = new Empresa();
        seleccionEmpresa = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            empresas = empresaFacade.listaEmpresas();
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        empresas = empresaFacade.buscar(keyword);
    }

    public void subirArchivo_NuevaEmpresa(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/empresas");
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        Path file = null;
        try (InputStream input = event.getFile().getInputStream()) {
            file = Files.createTempFile(folder, null, "." + extension);
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);

            nuevaEmpresa.setLogo(file.getFileName().toString());
        } catch (IOException ex) {
            Logger.getLogger(InstitutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void subirArchivo_EditarEmpresa(FileUploadEvent event) {
        Path folder = Paths.get(realPath() + "/resources/uploads/empresas");
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        Path file = null;
        try (InputStream input = event.getFile().getInputStream()) {
            file = Files.createTempFile(folder, null, "." + extension);
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);

            seleccionEmpresa.setLogo(file.getFileName().toString());
        } catch (IOException ex) {
            Logger.getLogger(InstitutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearEmpresa() throws IOException {
        if (empresaFacade.buscarPorDni(nuevaEmpresa.getDni()) == null) {
            if (empresaFacade.create(nuevaEmpresa)) {
                this.toEmpresas();
            }
        } else {
            this.mensajeDeError("Empresa repetido.");
        }
    }

    public void editarEmpresa() throws IOException {
        if (empresaFacade.buscarPorDni(seleccionEmpresa.getDni(), seleccionEmpresa.getId_persona()) == null) {
            if (empresaFacade.edit(seleccionEmpresa)) {
                this.toEmpresas();
            }
        } else {
            this.mensajeDeError("Empresa repetido.");
        }
    }

    public void toNuevaEmpresa() throws IOException {
        this.redireccionarViewId("/administrador/empresa/nuevaEmpresa");
    }

    public void toEditarEmpresa() throws IOException {
        this.redireccionarViewId("/administrador/empresa/editarEmpresa");
    }

    public void toEmpresas() throws IOException {
        reinit();

        this.redireccionarViewId("/administrador/empresa/empresas");
    }

    /**
     * @return the empresas
     */
    public List<Empresa> getEmpresas() {
        return empresas;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    /**
     * @return the crearEmpresa
     */
    public Empresa getNuevaEmpresa() {
        return nuevaEmpresa;
    }

    /**
     * @param nuevaEmpresa the crearEmpresa to set
     */
    public void setNuevaEmpresa(Empresa nuevaEmpresa) {
        this.nuevaEmpresa = nuevaEmpresa;
    }

    /**
     * @return the seleccionEmpresa
     */
    public Empresa getSeleccionEmpresa() {
        return seleccionEmpresa;
    }

    /**
     * @param seleccionEmpresa the seleccionEmpresa to set
     */
    public void setSeleccionEmpresa(Empresa seleccionEmpresa) {
        this.seleccionEmpresa = seleccionEmpresa;
    }

    /**
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}