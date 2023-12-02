/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.util.Fecha;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Tincho
 */
@Named("ReporteCertificadoPasantiaController")
@SessionScoped
public class ReporteCertificadoPasantiaController extends AbstractController implements Serializable {

    private static final String PATHNAME = File.separator + "resources" + File.separator + "uploads" + File.separator + "certificado_pasantia.docx";

    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;
    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private GrupoPasantia seleccionGrupoPasantia;
    private List<NotaPasantia> notasPasantias;
    private NotaPasantia seleccionNotaPasantia;

    private String keyword;

    private StreamedContent download;
    private UploadedFile upload;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        notasPasantias = new ArrayList<>();
        seleccionNotaPasantia = null;

        keyword = null;
    }

    public void reinit() {
        notasPasantias = notaPasantiaFacade.listaNotasPasantias(seleccionGrupoPasantia.getId_grupopasantia());
        seleccionNotaPasantia = null;

        keyword = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public List<GrupoPasantia> listaGruposPasantias() {
        List<GrupoPasantia> l = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            l = grupoPasantiaFacade.listaGrupoPasantias(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public List<GrupoPasantia> listaGruposPasantiasEditarPasantia() {
        List<GrupoPasantia> l = new ArrayList<>();
        if (seleccionNotaPasantia != null) {
            l = grupoPasantiaFacade.listaGrupoPasantiasAbiertos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public void buscar() {
        if (seleccionGrupoPasantia != null) {
            notasPasantias = notaPasantiaFacade.buscar(seleccionGrupoPasantia, keyword);
        }
    }

    public XWPFDocument leerArchivo(String pathname) {
        XWPFDocument document = null;

        try (FileInputStream file = new FileInputStream(this.realPath() + pathname)) {
            document = new XWPFDocument(file);
        } catch (IOException e) {
            this.mensajeDeError("Error: No se pudo leer el archivo.");
        }

        return document;
    }

    public void descargarArchivo(XWPFDocument document, NotaPasantia notaPasantia) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            document.close();

            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            download = DefaultStreamedContent.builder()
                    .name(notaPasantia.getEstudiante().toString() + " - " + notaPasantia.getEmpresa().toString() + ".docx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> is)
                    .build();
        } catch (IOException ex) {
            this.mensajeDeError("Error: No se pudo descargar el archivo.");
        }
    }

    public void bajarCertificado(NotaPasantia notaPasantia) throws IOException, InvalidFormatException {
        XWPFDocument document = leerArchivo(PATHNAME);

        Iterator<XWPFParagraph> iterator = document.getParagraphsIterator();

        if (iterator.hasNext()) {
            XWPFParagraph p = iterator.next();
            XWPFRun run = p.createRun();
            p.setAlignment(ParagraphAlignment.LEFT);

            String imgFile = File.separator + "resources" + File.separator + "uploads" + File.separator + notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getLogo();
            FileInputStream is = new FileInputStream(this.realPath() + imgFile);
            run.addBreak();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(80), Units.toEMU(80));
            is.close();
        }

        while (iterator.hasNext()) {
            XWPFParagraph p = iterator.next();

            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);

                    if (text != null && text.contains("<<NOMBRE_INSTITUTO>>")) {
                        text = text.replace("<<NOMBRE_INSTITUTO>>", notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getNombreRegulador());
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<RM>>")) {
                        text = text.replace("<<RM>>", notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getResolucionMinisterial());
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<NOMBRE_EMPRESA>>")) {
                        text = text.replace("<<NOMBRE_EMPRESA>>", notaPasantia.getEmpresa().toString());
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<NOMBRE_ESTUDIANTE>>")) {
                        text = text.replace("<<NOMBRE_ESTUDIANTE>>", notaPasantia.getEstudiante().toString());
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<CARRERA_ESTUDIANTE>>")) {
                        text = text.replace("<<CARRERA_ESTUDIANTE>>", notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getNombre());
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<FECHA_INICIO>>")) {
                        if (notaPasantia.getInicio() != null) {
                            text = text.replace("<<FECHA_INICIO>>", Fecha.formatearFecha_ddMMyyyy(notaPasantia.getInicio()));
                        } else {
                            text = text.replace("<<FECHA_INICIO>>", " ");
                        }
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<FECHA_FIN>>")) {
                        if (notaPasantia.getFin() != null) {
                            text = text.replace("<<FECHA_FIN>>", Fecha.formatearFecha_ddMMyyyy(notaPasantia.getFin()));
                        } else {
                            text = text.replace("<<FECHA_FIN>>", " ");
                        }
                        r.setText(text, 0);
                    }
                    if (text != null && text.contains("<<FECHA_ACTUAL>>")) {
                        text = text.replace("<<FECHA_ACTUAL>>", Fecha.formatearFecha_ddMMMMyyyy(Fecha.getDate()));
                        r.setText(text, 0);
                    }

                }
            }
        }

        descargarArchivo(document, notaPasantia);

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte certificado pasantia", loginController.getUsr().toString()));
    }

    public void toReporteFichaInscripcion() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/pasantias/fichaInscripcion/reporteFichaInscripcion");
    }

    /**
     * @return the seleccionGestionAcademica
     */
    public GestionAcademica getSeleccionGestionAcademica() {
        return seleccionGestionAcademica;
    }

    /**
     * @param seleccionGestionAcademica the seleccionGestionAcademica to set
     */
    public void setSeleccionGestionAcademica(GestionAcademica seleccionGestionAcademica) {
        this.seleccionGestionAcademica = seleccionGestionAcademica;
    }

    /**
     * @return the notasPasantias
     */
    public List<NotaPasantia> getNotasPasantias() {
        return notasPasantias;
    }

    /**
     * @param notasPasantias the notasPasantias to set
     */
    public void setNotasPasantias(List<NotaPasantia> notasPasantias) {
        this.notasPasantias = notasPasantias;
    }

    /**
     * @return the seleccionNotaPasantia
     */
    public NotaPasantia getSeleccionNotaPasantia() {
        return seleccionNotaPasantia;
    }

    /**
     * @param seleccionNotaPasantia the seleccionNotaPasantia to set
     */
    public void setSeleccionNotaPasantia(NotaPasantia seleccionNotaPasantia) {
        this.seleccionNotaPasantia = seleccionNotaPasantia;
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

    /**
     * @return the seleccionCarrera
     */
    public Carrera getSeleccionCarrera() {
        return seleccionCarrera;
    }

    /**
     * @param seleccionCarrera the seleccionCarrera to set
     */
    public void setSeleccionCarrera(Carrera seleccionCarrera) {
        this.seleccionCarrera = seleccionCarrera;
    }

    /**
     * @return the seleccionGrupoPasantia
     */
    public GrupoPasantia getSeleccionGrupoPasantia() {
        return seleccionGrupoPasantia;
    }

    /**
     * @param seleccionGrupoPasantia the seleccionGrupoPasantia to set
     */
    public void setSeleccionGrupoPasantia(GrupoPasantia seleccionGrupoPasantia) {
        this.seleccionGrupoPasantia = seleccionGrupoPasantia;
    }

    /**
     * @return the download
     */
    public StreamedContent getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(StreamedContent download) {
        this.download = download;
    }

    /**
     * @return the upload
     */
    public UploadedFile getUpload() {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(UploadedFile upload) {
        this.upload = upload;
    }

}
