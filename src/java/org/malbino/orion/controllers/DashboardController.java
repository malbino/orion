/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.enums.Condicion;
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

    private GestionAcademica seleccionGestionAcademica;

    private Integer totalInscritos;
    private Integer varones;
    private Double porcentajeVarones;
    private Integer mujeres;
    private Double porcentajeMujeres;
    private Integer efectivos;
    private Double porcentajeEfectivos;
    private Integer retirados;
    private Double porcentajeRetirados;
    private Integer aprobados;
    private Double porcentajeAprobados;
    private Integer reprobados;
    private Double porcentajeReprobados;

    private BarChartModel hbarModel;
    private PieChartModel pieModel;

    private DonutChartModel donutModel1;
    private DonutChartModel donutModel2;
    private DonutChartModel donutModel3;

    private List<NivelCuadroEstadistico> nivelesCuadroEstadistico;

    @PostConstruct
    public void init() {
        varones = 0;
        porcentajeVarones = 0.0;

        mujeres = 0;
        porcentajeMujeres = 0.0;

        retirados = 0;
        porcentajeRetirados = 0.0;

        efectivos = 0;
        porcentajeEfectivos = 0.0;

        aprobados = 0;
        porcentajeAprobados = 0.0;

        reprobados = 0;
        porcentajeReprobados = 0.0;

        createHorizontalBarModel();
        createPieModel();

        createDonutModel1();
        createDonutModel2();
        createDonutModel3();

        crearCuadroEstadistico();
    }

    public void reinit() {
        if (seleccionGestionAcademica != null) {
            List<Inscrito> listaInscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica());
            totalInscritos = listaInscritos.size();

            if (totalInscritos > 0) {
                List<Inscrito> listaVarones = listaInscritos.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.MASCULINO)).collect(Collectors.toList());
                varones = listaVarones.size();
                porcentajeVarones = Redondeo.redondear_HALFUP(((varones.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                List<Inscrito> listaMujeres = listaInscritos.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.FEMENINO)).collect(Collectors.toList());
                mujeres = listaMujeres.size();
                porcentajeMujeres = Redondeo.redondear_HALFUP(((mujeres.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                List<Inscrito> listaRetirados = listaInscritos.stream().filter(i -> i.condicion().equals(Condicion.ABANDONO)).collect(Collectors.toList());
                retirados = listaRetirados.size();
                porcentajeRetirados = Redondeo.redondear_HALFUP(((retirados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                efectivos = listaInscritos.size() - listaRetirados.size();
                porcentajeEfectivos = Redondeo.redondear_HALFUP(((efectivos.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                List<Inscrito> listaAprobados = listaInscritos.stream().filter(i -> i.condicion().equals(Condicion.APROBADO)).collect(Collectors.toList());
                aprobados = listaAprobados.size();
                porcentajeAprobados = Redondeo.redondear_HALFUP(((aprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                List<Inscrito> listaReprobados = listaInscritos.stream().filter(i -> i.condicion().equals(Condicion.REPROBADO)).collect(Collectors.toList());
                reprobados = listaReprobados.size();
                porcentajeReprobados = Redondeo.redondear_HALFUP(((reprobados.doubleValue() / totalInscritos.doubleValue()) * 100.0), 2);

                createHorizontalBarModel();
                createPieModel();

                createDonutModel1();
                createDonutModel2();
                createDonutModel3();

                crearCuadroEstadistico();
            } else {
                varones = 0;
                porcentajeVarones = 0.0;

                mujeres = 0;
                porcentajeMujeres = 0.0;

                retirados = 0;
                porcentajeRetirados = 0.0;

                efectivos = 0;
                porcentajeEfectivos = 0.0;

                aprobados = 0;
                porcentajeAprobados = 0.0;

                reprobados = 0;
                porcentajeReprobados = 0.0;

                createHorizontalBarModel();
                createPieModel();

                createDonutModel1();
                createDonutModel2();
                createDonutModel3();

                crearCuadroEstadistico();
            }
        }
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        return gestionAcademicaFacade.listaGestionAcademicaDashboard();
    }

    public void createHorizontalBarModel() {
        hbarModel = new HorizontalBarChartModel();
        ChartData data = new ChartData();

        HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
        hbarDataSet.setLabel("Cantidad");

        List<Carrera> carreras = new ArrayList<>();
        if (seleccionGestionAcademica != null) {
            carreras = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }

        List<Number> values = new ArrayList<>();
        for (Carrera carrera : carreras) {
            Long cantidadInscritos = inscritoFacade.cantidadInscritos(seleccionGestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
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

        List<Carrera> carreras = new ArrayList<>();
        if (seleccionGestionAcademica != null) {
            carreras = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }

        List<Number> values = new ArrayList<>();
        for (Carrera carrera : carreras) {
            Long cantidadInscritos = inscritoFacade.cantidadInscritos(seleccionGestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
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
        if (seleccionGestionAcademica != null) {
            nivelesCuadroEstadistico = new ArrayList();
            NivelCuadroEstadistico nivelCuadroEstadistico;

            Integer totalInscritos = 0;
            Integer totalInscritosVarones = 0;
            Integer totalInscritosMujeres = 0;
            Integer totalRetirados = 0;
            Integer totalRetiradosVarones = 0;
            Integer totalRetiradosMujeres = 0;
            Integer totalEfectivos = 0;
            Integer totalEfectivosVarones = 0;
            Integer totalEfectivosMujeres = 0;
            Integer totalAprobados = 0;
            Integer totalAprobadosVarones = 0;
            Integer totalAprobadosMujeres = 0;
            Integer totalReprobados = 0;
            Integer totalReprobadosVarones = 0;
            Integer totalReprobadosMujeres = 0;

            List<Carrera> carreras = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
            for (Carrera carrera : carreras) {

                Integer subtotalInscritos = 0;
                Integer subtotalInscritosVarones = 0;
                Integer subtotalInscritosMujeres = 0;
                Integer subtotalRetirados = 0;
                Integer subtotalRetiradosVarones = 0;
                Integer subtotalRetiradosMujeres = 0;
                Integer subtotalEfectivos = 0;
                Integer subtotalEfectivosVarones = 0;
                Integer subtotalEfectivosMujeres = 0;
                Integer subtotalAprobados = 0;
                Integer subtotalAprobadosVarones = 0;
                Integer subtotalAprobadosMujeres = 0;
                Integer subtotalReprobados = 0;
                Integer subtotalReprobadosVarones = 0;
                Integer subtotalReprobadosMujeres = 0;

                Nivel[] niveles = Nivel.values(carrera.getRegimen());
                for (Nivel nivel : niveles) {

                    List<String> paralelos = grupoFacade.listaParalelos(seleccionGestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel);
                    for (String paralelo : paralelos) {

                        List<Inscrito> inscritos = inscritoFacade.listaInscritos(seleccionGestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), nivel, paralelo);
                        if (!inscritos.isEmpty()) {
                            nivelCuadroEstadistico = new NivelCuadroEstadistico();

                            nivelCuadroEstadistico.setCodigo(carrera.getCodigo());
                            nivelCuadroEstadistico.setNombre(carrera.getNombre());
                            nivelCuadroEstadistico.setNivel(nivel);
                            nivelCuadroEstadistico.setParalelo(paralelo);

                            List<Inscrito> inscritosVarones = inscritos.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.MASCULINO)).collect(Collectors.toList());
                            List<Inscrito> inscritosMujeres = inscritos.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.FEMENINO)).collect(Collectors.toList());
                            nivelCuadroEstadistico.setInscritos(inscritos.size());
                            nivelCuadroEstadistico.setInscritosVarones(inscritosVarones.size());
                            nivelCuadroEstadistico.setInscritosMujeres(inscritosMujeres.size());
                            subtotalInscritos += inscritos.size();
                            totalInscritos += inscritos.size();
                            subtotalInscritosVarones += inscritosVarones.size();
                            totalInscritosVarones += inscritosVarones.size();
                            subtotalInscritosMujeres += inscritosMujeres.size();
                            totalInscritosMujeres += inscritosMujeres.size();

                            List<Inscrito> retirados = inscritos.stream().filter(i -> i.condicion().equals(Condicion.ABANDONO)).collect(Collectors.toList());
                            List<Inscrito> retiradosVarones = retirados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.MASCULINO)).collect(Collectors.toList());
                            List<Inscrito> retiradosMujeres = retirados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.FEMENINO)).collect(Collectors.toList());
                            nivelCuadroEstadistico.setRetirados(retirados.size());
                            nivelCuadroEstadistico.setRetiradosVarones(retiradosVarones.size());
                            nivelCuadroEstadistico.setRetiradosMujeres(retiradosMujeres.size());
                            subtotalRetirados += retirados.size();
                            totalRetirados += retirados.size();
                            subtotalRetiradosVarones += retiradosVarones.size();
                            totalRetiradosVarones += retiradosVarones.size();
                            subtotalRetiradosMujeres += retiradosMujeres.size();
                            totalRetiradosMujeres += retiradosMujeres.size();

                            Integer efectivos = inscritos.size() - retirados.size();
                            Integer efectivosVarones = inscritosVarones.size() - retiradosVarones.size();
                            Integer efectivosMujeres = inscritosMujeres.size() - retiradosMujeres.size();
                            nivelCuadroEstadistico.setEfectivos(efectivos);
                            nivelCuadroEstadistico.setEfectivosVarones(efectivosVarones);
                            nivelCuadroEstadistico.setEfectivosMujeres(efectivosMujeres);
                            subtotalEfectivos += efectivos;
                            totalEfectivos += efectivos;
                            subtotalEfectivosVarones += efectivosVarones;
                            totalEfectivosVarones += efectivosVarones;
                            subtotalEfectivosMujeres += efectivosMujeres;
                            totalEfectivosMujeres += efectivosMujeres;

                            List<Inscrito> aprobados = inscritos.stream().filter(i -> i.condicion().equals(Condicion.APROBADO)).collect(Collectors.toList());
                            List<Inscrito> aprobadosVarones = aprobados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.MASCULINO)).collect(Collectors.toList());
                            List<Inscrito> aprobadosMujeres = aprobados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.FEMENINO)).collect(Collectors.toList());
                            nivelCuadroEstadistico.setAprobados(aprobados.size());
                            nivelCuadroEstadistico.setAprobadosVarones(aprobadosVarones.size());
                            nivelCuadroEstadistico.setAprobadosMujeres(aprobadosMujeres.size());
                            subtotalAprobados += aprobados.size();
                            totalAprobados += aprobados.size();
                            subtotalAprobadosVarones += aprobadosVarones.size();
                            totalAprobadosVarones += aprobadosVarones.size();
                            subtotalAprobadosMujeres += aprobadosMujeres.size();
                            totalAprobadosMujeres += aprobadosMujeres.size();

                            List<Inscrito> reprobados = inscritos.stream().filter(i -> i.condicion().equals(Condicion.REPROBADO)).collect(Collectors.toList());
                            List<Inscrito> reprobadosVarones = reprobados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.MASCULINO)).collect(Collectors.toList());
                            List<Inscrito> reprobadosMujeres = reprobados.stream().filter(i -> i.getEstudiante().getSexo().equals(Sexo.FEMENINO)).collect(Collectors.toList());
                            nivelCuadroEstadistico.setReprobados(reprobados.size());
                            nivelCuadroEstadistico.setReprobadosVarones(reprobadosVarones.size());
                            nivelCuadroEstadistico.setReprobadosMujeres(reprobadosMujeres.size());
                            subtotalReprobados += reprobados.size();
                            totalReprobados += reprobados.size();
                            subtotalReprobadosVarones += reprobadosVarones.size();
                            totalReprobadosVarones += reprobadosVarones.size();
                            subtotalReprobadosMujeres += reprobadosMujeres.size();
                            totalReprobadosMujeres += reprobadosMujeres.size();

                            nivelesCuadroEstadistico.add(nivelCuadroEstadistico);
                        }
                    }
                }

                //subtotales
                if (subtotalInscritos > 0) {
                    nivelCuadroEstadistico = new NivelCuadroEstadistico();
                    nivelCuadroEstadistico.setCodigo("");
                    nivelCuadroEstadistico.setNombre("");
                    nivelCuadroEstadistico.setNivel(null);
                    nivelCuadroEstadistico.setInscritos(subtotalInscritos);
                    nivelCuadroEstadistico.setInscritosVarones(subtotalInscritosVarones);
                    nivelCuadroEstadistico.setInscritosMujeres(subtotalInscritosMujeres);
                    nivelCuadroEstadistico.setRetirados(subtotalRetirados);
                    nivelCuadroEstadistico.setRetiradosVarones(subtotalRetiradosVarones);
                    nivelCuadroEstadistico.setRetiradosMujeres(subtotalRetiradosMujeres);
                    nivelCuadroEstadistico.setEfectivos(subtotalEfectivos);
                    nivelCuadroEstadistico.setEfectivosVarones(subtotalEfectivosVarones);
                    nivelCuadroEstadistico.setEfectivosMujeres(subtotalEfectivosMujeres);
                    nivelCuadroEstadistico.setAprobados(subtotalAprobados);
                    nivelCuadroEstadistico.setAprobadosVarones(subtotalAprobadosVarones);
                    nivelCuadroEstadistico.setAprobadosMujeres(subtotalAprobadosMujeres);
                    nivelCuadroEstadistico.setReprobados(subtotalReprobados);
                    nivelCuadroEstadistico.setReprobadosVarones(subtotalReprobadosVarones);
                    nivelCuadroEstadistico.setReprobadosMujeres(subtotalReprobadosMujeres);
                    nivelesCuadroEstadistico.add(nivelCuadroEstadistico);
                }
            }
            
            //totales
            if (totalInscritos > 0) {
                nivelCuadroEstadistico = new NivelCuadroEstadistico();
                nivelCuadroEstadistico.setCodigo("");
                nivelCuadroEstadistico.setNombre("");
                nivelCuadroEstadistico.setNivel(null);
                nivelCuadroEstadistico.setInscritos(totalInscritos);
                nivelCuadroEstadistico.setInscritosVarones(totalInscritosVarones);
                nivelCuadroEstadistico.setInscritosMujeres(totalInscritosMujeres);
                nivelCuadroEstadistico.setRetirados(totalRetirados);
                nivelCuadroEstadistico.setRetiradosVarones(totalRetiradosVarones);
                nivelCuadroEstadistico.setRetiradosMujeres(totalRetiradosMujeres);
                nivelCuadroEstadistico.setEfectivos(totalEfectivos);
                nivelCuadroEstadistico.setEfectivosVarones(totalEfectivosVarones);
                nivelCuadroEstadistico.setEfectivosMujeres(totalEfectivosMujeres);
                nivelCuadroEstadistico.setAprobados(totalAprobados);
                nivelCuadroEstadistico.setAprobadosVarones(totalAprobadosVarones);
                nivelCuadroEstadistico.setAprobadosMujeres(totalAprobadosMujeres);
                nivelCuadroEstadistico.setReprobados(totalReprobados);
                nivelCuadroEstadistico.setReprobadosVarones(totalReprobadosVarones);
                nivelCuadroEstadistico.setReprobadosMujeres(totalReprobadosMujeres);
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
    public Integer getTotalInscritos() {
        return totalInscritos;
    }

    /**
     * @param totalInscritos the totalInscritos to set
     */
    public void setTotalInscritos(Integer totalInscritos) {
        this.totalInscritos = totalInscritos;
    }

    /**
     * @return the varones
     */
    public Integer getVarones() {
        return varones;
    }

    /**
     * @param varones the varones to set
     */
    public void setVarones(Integer varones) {
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
    public Integer getMujeres() {
        return mujeres;
    }

    /**
     * @param mujeres the mujeres to set
     */
    public void setMujeres(Integer mujeres) {
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
    public Integer getEfectivos() {
        return efectivos;
    }

    /**
     * @param efectivos the efectivos to set
     */
    public void setEfectivos(Integer efectivos) {
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
    public Integer getRetirados() {
        return retirados;
    }

    /**
     * @param retirados the retirados to set
     */
    public void setRetirados(Integer retirados) {
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
    public Integer getAprobados() {
        return aprobados;
    }

    /**
     * @param aprobados the aprobados to set
     */
    public void setAprobados(Integer aprobados) {
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
    public Integer getReprobados() {
        return reprobados;
    }

    /**
     * @param reprobados the reprobados to set
     */
    public void setReprobados(Integer reprobados) {
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
}
