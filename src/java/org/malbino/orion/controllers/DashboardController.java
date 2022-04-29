/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
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
import org.malbino.orion.facades.GrupoFacade;
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
import org.primefaces.model.charts.pie.PieChartModel;

/**
 *
 * @author Tincho
 */
@Named("DashboardController")
@SessionScoped
public class DashboardController extends AbstractController implements Serializable {

    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    GrupoFacade grupoFacade;

    private Long totalInscritos;
    private Long varones;
    private Double porcentajeVarones;
    private Long mujeres;
    private Double porcentajeMujeres;
    private Long efectivos;
    private Double porcentajeEfectivos;
    private Long retirados;
    private Double porcentajeRetirados;
    private Long aprobados;
    private Double porcentajeAprobados;
    private Long reprobados;
    private Double porcentajeReprobados;

    private BarChartModel hbarModel;
    private PieChartModel pieModel;

    private DonutChartModel donutModel1;
    private DonutChartModel donutModel2;
    private DonutChartModel donutModel3;

    private List<NivelCuadroEstadistico> nivelesCuadroEstadistico;

    @PostConstruct
    public void init() {
        totalInscritos = inscritoFacade.cantidadInscritos();

        varones = inscritoFacade.cantidadInscritos(Sexo.MASCULINO);
        porcentajeVarones = Redondeo.redondear_HALFUP(((varones.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        mujeres = inscritoFacade.cantidadInscritos(Sexo.FEMENINO);
        porcentajeMujeres = Redondeo.redondear_HALFUP(((mujeres.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        efectivos = inscritoFacade.cantidadInscritosEfectivos();
        porcentajeEfectivos = Redondeo.redondear_HALFUP(((efectivos.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        retirados = totalInscritos - efectivos;
        porcentajeRetirados = Redondeo.redondear_HALFUP(((retirados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        reprobados = inscritoFacade.cantidadInscritosReprobados();
        aprobados = totalInscritos - reprobados;
        porcentajeAprobados = Redondeo.redondear_HALFUP(((aprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        porcentajeReprobados = Redondeo.redondear_HALFUP(((reprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        createHorizontalBarModel();
        createPieModel();

        createDonutModel1();
        createDonutModel2();
        createDonutModel3();

        crearCuadroEstadistico();
    }

    public void reinit() {
        totalInscritos = inscritoFacade.cantidadInscritos();

        varones = inscritoFacade.cantidadInscritos(Sexo.MASCULINO);
        porcentajeVarones = Redondeo.redondear_HALFUP(((varones.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        mujeres = inscritoFacade.cantidadInscritos(Sexo.FEMENINO);
        porcentajeMujeres = Redondeo.redondear_HALFUP(((mujeres.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        efectivos = inscritoFacade.cantidadInscritosEfectivos();
        porcentajeEfectivos = Redondeo.redondear_HALFUP(((efectivos.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        retirados = totalInscritos - efectivos;
        porcentajeRetirados = Redondeo.redondear_HALFUP(((retirados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        reprobados = inscritoFacade.cantidadInscritosReprobados();
        aprobados = totalInscritos - reprobados;
        porcentajeAprobados = Redondeo.redondear_HALFUP(((aprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);
        porcentajeReprobados = Redondeo.redondear_HALFUP(((reprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

        createHorizontalBarModel();
        createPieModel();

        createDonutModel1();
        createDonutModel2();
        createDonutModel3();

        crearCuadroEstadistico();
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
        bgColor.add("#1b9e77");
        bgColor.add("#d95f02");
        bgColor.add("#7570b3");
        bgColor.add("#e7298a");
        bgColor.add("#66a61e");
        bgColor.add("#e6ab02");
        bgColor.add("#a6761d");
        bgColor.add("#666666");
        hbarDataSet.setBackgroundColor(bgColor);

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

    public void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();

        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {
            Long cantidadInscritos = inscritoFacade.cantidadInscritos(carrera.getId_carrera());
            values.add(cantidadInscritos);
        }

        dataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("#1b9e77");
        bgColor.add("#d95f02");
        bgColor.add("#7570b3");
        bgColor.add("#e7298a");
        bgColor.add("#66a61e");
        bgColor.add("#e6ab02");
        bgColor.add("#a6761d");
        bgColor.add("#666666");
        dataSet.setBackgroundColor(bgColor);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        for (Carrera carrera : carreras) {
            labels.add(carrera.getCodigo());
        }
        data.setLabels(labels);

        pieModel.setData(data);
    }

    public void createDonutModel1() {
        donutModel1 = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(varones);
        values.add(mujeres);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("#1abb9c");
        bgColors.add("#73879c");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Varones");
        labels.add("Mujeres");
        data.setLabels(labels);

        donutModel1.setData(data);
    }

    public void createDonutModel2() {
        donutModel2 = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(efectivos);
        values.add(retirados);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("#1abb9c");
        bgColors.add("#73879c");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Efectivos");
        labels.add("Retirados");
        data.setLabels(labels);

        donutModel2.setData(data);
    }

    public void createDonutModel3() {
        donutModel3 = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(aprobados);
        values.add(reprobados);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("#1abb9c");
        bgColors.add("#73879c");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Aprobados");
        labels.add("Reprobados");
        data.setLabels(labels);

        donutModel3.setData(data);
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

                Long numeroParalelos = grupoFacade.cantidadGrupos(carrera.getId_carrera(), nivel);
                nivelCuadroEstadistico.setNumeroParalelos(numeroParalelos);

                Long totalInscritos = inscritoFacade.cantidadInscritos(carrera.getId_carrera(), nivel);
                nivelCuadroEstadistico.setTotalInscritos(totalInscritos);

                Long varones = inscritoFacade.cantidadInscritos(carrera.getId_carrera(), nivel, Sexo.MASCULINO);
                nivelCuadroEstadistico.setVarones(varones);
                Long mujeres = inscritoFacade.cantidadInscritos(carrera.getId_carrera(), nivel, Sexo.FEMENINO);
                nivelCuadroEstadistico.setMujeres(mujeres);

                Long efectivos = inscritoFacade.cantidadInscritosEfectivos(carrera.getId_carrera(), nivel);
                nivelCuadroEstadistico.setEfectivos(efectivos);
                Long retirados = totalInscritos - efectivos;
                nivelCuadroEstadistico.setRetirados(retirados);

                Long reprobados = inscritoFacade.cantidadInscritosReprobados(carrera.getId_carrera(), nivel);
                Long aprobados = totalInscritos - reprobados;
                nivelCuadroEstadistico.setAprobados(aprobados);
                nivelCuadroEstadistico.setReprobados(reprobados);

                nivelesCuadroEstadistico.add(nivelCuadroEstadistico);
            }
        }
    }
    
    public void toDashboard() throws IOException {
        reinit();
        
        this.redireccionarViewId("/reportes/dashboard");
    }

    /**
     * @return the totalInscritos
     */
    public Long getTotalInscritos() {
        return totalInscritos;
    }

    /**
     * @param totalInscritos the totalInscritos to set
     */
    public void setTotalInscritos(Long totalInscritos) {
        this.totalInscritos = totalInscritos;
    }

    /**
     * @return the varones
     */
    public Long getVarones() {
        return varones;
    }

    /**
     * @param varones the varones to set
     */
    public void setVarones(Long varones) {
        this.varones = varones;
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
     * @return the mujeres
     */
    public Long getMujeres() {
        return mujeres;
    }

    /**
     * @param mujeres the mujeres to set
     */
    public void setMujeres(Long mujeres) {
        this.mujeres = mujeres;
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
     * @return the pieModel
     */
    public PieChartModel getPieModel() {
        return pieModel;
    }

    /**
     * @param pieModel the pieModel to set
     */
    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
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

    /**
     * @return the efectivos
     */
    public Long getEfectivos() {
        return efectivos;
    }

    /**
     * @param efectivos the efectivos to set
     */
    public void setEfectivos(Long efectivos) {
        this.efectivos = efectivos;
    }

    /**
     * @return the porcentajeEfectivos
     */
    public Double getPorcentajeEfectivos() {
        return porcentajeEfectivos;
    }

    /**
     * @param porcentajeEfectivos the porcentajeEfectivos to set
     */
    public void setPorcentajeEfectivos(Double porcentajeEfectivos) {
        this.porcentajeEfectivos = porcentajeEfectivos;
    }

    /**
     * @return the retirados
     */
    public Long getRetirados() {
        return retirados;
    }

    /**
     * @param retirados the retirados to set
     */
    public void setRetirados(Long retirados) {
        this.retirados = retirados;
    }

    /**
     * @return the porcentajeRetirados
     */
    public Double getPorcentajeRetirados() {
        return porcentajeRetirados;
    }

    /**
     * @param porcentajeRetirados the porcentajeRetirados to set
     */
    public void setPorcentajeRetirados(Double porcentajeRetirados) {
        this.porcentajeRetirados = porcentajeRetirados;
    }

    /**
     * @return the aprobados
     */
    public Long getAprobados() {
        return aprobados;
    }

    /**
     * @param aprobados the aprobados to set
     */
    public void setAprobados(Long aprobados) {
        this.aprobados = aprobados;
    }

    /**
     * @return the porcentajeAprobados
     */
    public Double getPorcentajeAprobados() {
        return porcentajeAprobados;
    }

    /**
     * @param porcentajeAprobados the porcentajeAprobados to set
     */
    public void setPorcentajeAprobados(Double porcentajeAprobados) {
        this.porcentajeAprobados = porcentajeAprobados;
    }

    /**
     * @return the reprobados
     */
    public Long getReprobados() {
        return reprobados;
    }

    /**
     * @param reprobados the reprobados to set
     */
    public void setReprobados(Long reprobados) {
        this.reprobados = reprobados;
    }

    /**
     * @return the porcentajeReprobados
     */
    public Double getPorcentajeReprobados() {
        return porcentajeReprobados;
    }

    /**
     * @param porcentajeReprobados the porcentajeReprobados to set
     */
    public void setPorcentajeReprobados(Double porcentajeReprobados) {
        this.porcentajeReprobados = porcentajeReprobados;
    }

    /**
     * @return the donutModel1
     */
    public DonutChartModel getDonutModel1() {
        return donutModel1;
    }

    /**
     * @param donutModel1 the donutModel1 to set
     */
    public void setDonutModel1(DonutChartModel donutModel1) {
        this.donutModel1 = donutModel1;
    }

    /**
     * @return the donutModel2
     */
    public DonutChartModel getDonutModel2() {
        return donutModel2;
    }

    /**
     * @param donutModel2 the donutModel2 to set
     */
    public void setDonutModel2(DonutChartModel donutModel2) {
        this.donutModel2 = donutModel2;
    }

    /**
     * @return the donutModel3
     */
    public DonutChartModel getDonutModel3() {
        return donutModel3;
    }

    /**
     * @param donutModel3 the donutModel3 to set
     */
    public void setDonutModel3(DonutChartModel donutModel3) {
        this.donutModel3 = donutModel3;
    }
}
