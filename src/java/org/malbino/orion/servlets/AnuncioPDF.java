package org.malbino.orion.servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.entities.Anuncio;
import org.malbino.orion.facades.AnuncioFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "AnuncioPDF", urlPatterns = {"/reportes/AnuncioPDF"})
public class AnuncioPDF extends HttpServlet {
    
    private static final String CONTENIDO_PDF = "application/pdf";
    
    private static final int MARGEN_IZQUIERDO = 40;
    private static final int MARGEN_DERECHO = 40;
    private static final int MARGEN_SUPERIOR = 40;
    private static final int MARGEN_INFERIOR = 40;
    
    @EJB
    AnuncioFacade anuncioFacade;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.generarPDF(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(AnuncioPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.generarPDF(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(AnuncioPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generarPDF(HttpServletRequest request, HttpServletResponse response) throws DocumentException, IOException {
        Integer id_anuncio = (Integer) request.getSession().getAttribute("id_anuncio");
        
        if (id_anuncio != null) {
            Anuncio anuncio = anuncioFacade.find(id_anuncio);
            
            response.setContentType(CONTENIDO_PDF);
            
            Document document = new Document(PageSize.LETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
            PdfWriter instance = PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(instance, document, new StringReader(anuncio.toHTML()));

            // step 4
            document.close();
        }
    }
    
}
