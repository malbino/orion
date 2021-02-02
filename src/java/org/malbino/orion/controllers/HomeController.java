/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Sexo;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.pojos.NivelCuadroEstadistico;
import org.malbino.orion.util.Redondeo;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author Tincho
 */
@Named("HomeController")
@SessionScoped
public class HomeController extends AbstractController implements Serializable {

    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    CarreraFacade carreraFacade;
    @EJB
    InscritoFacade inscritoFacade;

    private Long totalEstudiantes;
    private Long totalVarones;
    private Double porcentajeVarones;
    private Long totalMujeres;
    private Double porcentajeMujeres;

    private BarChartModel hbarModel;
    private DonutChartModel donutModel;

    private List<NivelCuadroEstadistico> nivelesCuadroEstadistico;

    @PostConstruct
    public void init() {
        totalEstudiantes = estudianteFacade.cantidadEstudiantes();
        totalVarones = estudianteFacade.cantidadEstudiantes(Sexo.MASCULINO);
        porcentajeVarones = Redondeo.redondear_HALFUP(((totalVarones.doubleValue() / totalEstudiantes.doubleValue()) * 100.0), 2);
        totalMujeres = estudianteFacade.cantidadEstudiantes(Sexo.FEMENINO);
        porcentajeMujeres = Redondeo.redondear_HALFUP(((totalMujeres.doubleValue() / totalEstudiantes.doubleValue()) * 100.0), 2);

        createHorizontalBarModel();
        createDonutModel();
        
        crearCuadroEstadistico();
    }

    public void reinit() {

    }

    public void createHorizontalBarModel() {
        hbarModel = new HorizontalBarChartModel();
        ChartData data = new ChartData();

        HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
        hbarDataSet.setLabel("Cantidad");

        List<Carrera> carreras = carreraFacade.listaCarreras();

        List<Number> values = new ArrayList<>();
        for (Carrera carrera : carreras) {
            Long cantidadInscritos = inscritoFacade.cantidadInscritos(carrera.getId_carrera());
            values.add(cantidadInscritos);
        }
        hbarDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        hbarDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        hbarDataSet.setBorderColor(borderColor);
        hbarDataSet.setBorderWidth(1);

        data.addChartDataSet(hbarDataSet);

        List<String> labels = new ArrayList<>();
        for (Carrera carrera : carreras) {
            labels.add(carrera.getCodigo());
        }
        data.setLabels(labels);
        hbarModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Cantidad de Inscritos por Carrera");
        options.setTitle(title);

        hbarModel.setOptions(options);
    }

    public void createDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();

        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {
            Long cantidadInscritos = inscritoFacade.cantidadInscritos(carrera.getId_carrera());
            values.add(cantidadInscritos);
        }

        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        for (Carrera carrera : carreras) {
            labels.add(carrera.getCodigo());
        }
        data.setLabels(labels);

        donutModel.setData(data);
    }
    
    public void crearCuadroEstadistico() {
        nivelesCuadroEstadistico = new ArrayList();
        
        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {
            Nivel[] niveles = Nivel.values(carrera.getRegimen());
            for (Nivel nivel : niveles) {
                NivelCuadroEstadistico nivelCuadroEstadistico = new NivelCuadroEstadistico();
                
                nivelCuadroEstadistico.setCodigo(carrera.getCodigo());
                nivelCuadroEstadistico.setNombre(carrera.getNombre());
                nivelCuadroEstadistico.setNivel(nivel);
                
                nivelesCuadroEstadistico.add(nivelCuadroEstadistico);
            }
        }
    }

    /**
     * @return the totalEstudiantes
     */
    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }

    /**
     * @param totalEstudiantes the totalEstudiantes to set
     */
    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    /**
     * @return the totalVarones
     */
    public Long getTotalVarones() {
        return totalVarones;
    }

    /**
     * @param totalVarones the totalVarones to set
     */
    public void setTotalVarones(Long totalVarones) {
        this.totalVarones = totalVarones;
    }

    /**
     * @return the porcentajeVarones
     */
    public Double getPorcentajeVarones() {
        return porcentajeVarones;
    }

    /**
     * @param porcentajeVarones the porcentajeVarones to set
     */
    public void setPorcentajeVarones(Double porcentajeVarones) {
        this.porcentajeVarones = porcentajeVarones;
    }

    /**
     * @return the totalMujeres
     */
    public Long getTotalMujeres() {
        return totalMujeres;
    }

    /**
     * @param totalMujeres the totalMujeres to set
     */
    public void setTotalMujeres(Long totalMujeres) {
        this.totalMujeres = totalMujeres;
    }

    /**
     * @return the porcentajeMujeres
     */
    public Double getPorcentajeMujeres() {
        return porcentajeMujeres;
    }

    /**
     * @param porcentajeMujeres the porcentajeMujeres to set
     */
    public void setPorcentajeMujeres(Double porcentajeMujeres) {
        this.porcentajeMujeres = porcentajeMujeres;
    }

    /**
     * @return the hbarModel
     */
    public BarChartModel getHbarModel() {
        return hbarModel;
    }

    /**
     * @param hbarModel the hbarModel to set
     */
    public void setHbarModel(BarChartModel hbarModel) {
        this.hbarModel = hbarModel;
    }

    /**
     * @return the donutModel
     */
    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    /**
     * @param donutModel the donutModel to set
     */
    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    /**
     * @return the nivelesCuadroEstadistico
     */
    public List<NivelCuadroEstadistico> getNivelesCuadroEstadistico() {
        return nivelesCuadroEstadistico;
    }

    /**
     * @param nivelesCuadroEstadistico the nivelesCuadroEstadistico to set
     */
    public void setNivelesCuadroEstadistico(List<NivelCuadroEstadistico> nivelesCuadroEstadistico) {
        this.nivelesCuadroEstadistico = nivelesCuadroEstadistico;
    }
}
