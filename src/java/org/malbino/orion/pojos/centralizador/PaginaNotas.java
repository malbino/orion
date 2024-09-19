/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.pojos.centralizador;

/**
 *
 * @author Tincho
 */
public class PaginaNotas extends PaginaCentralizador{

    private String codigoRegistro;
    private String titulo;
    private Integer numeroLibro;
    private Integer numeroFolio;
    private String turno;
    private String gestion;
    private String nivel;
    private String carrera;
    private String regimen;
    private String curso;
    private String nota;

    private MateriaCentralizador[] materiasCentralizador;
    private EstudianteCentralizador[] estudiantesCentralizador;

    private int cantidadMaximaMaterias;
    private int cantidadMaximaEstudiantes;

    public PaginaNotas(String codigoRegistro, String titulo, Integer numeroLibro, Integer numeroFolio, String turno, String gestion, String nivel, String carrera, String regimen, String curso, String nota, int cantidadMaximaMaterias, int cantidadMaximaEstudiantes) {
        this.codigoRegistro = codigoRegistro;
        this.titulo = titulo;
        this.numeroLibro = numeroLibro;
        this.numeroFolio = numeroFolio;
        this.turno = turno;
        this.gestion = gestion;
        this.nivel = nivel;
        this.carrera = carrera;
        this.regimen = regimen;
        this.curso = curso;
        this.nota = nota;

        this.materiasCentralizador = new MateriaCentralizador[cantidadMaximaMaterias];
        this.estudiantesCentralizador = new EstudianteCentralizador[cantidadMaximaEstudiantes];
    }

    /**
     * @return the codigoRegistro
     */
    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    /**
     * @param codigoRegistro the codigoRegistro to set
     */
    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the numeroLibro
     */
    public Integer getNumeroLibro() {
        return numeroLibro;
    }

    /**
     * @param numeroLibro the numeroLibro to set
     */
    public void setNumeroLibro(Integer numeroLibro) {
        this.numeroLibro = numeroLibro;
    }

    /**
     * @return the numeroFolio
     */
    public Integer getNumeroFolio() {
        return numeroFolio;
    }

    /**
     * @param numeroFolio the numeroFolio to set
     */
    public void setNumeroFolio(Integer numeroFolio) {
        this.numeroFolio = numeroFolio;
    }

    /**
     * @return the turno
     */
    public String getTurno() {
        return turno;
    }

    /**
     * @param turno the turno to set
     */
    public void setTurno(String turno) {
        this.turno = turno;
    }

    /**
     * @return the gestion
     */
    public String getGestion() {
        return gestion;
    }

    /**
     * @param gestion the gestion to set
     */
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }

    /**
     * @return the nivel
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the carrera
     */
    public String getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    /**
     * @return the regimen
     */
    public String getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * @return the materiasCentralizador
     */
    public MateriaCentralizador[] getMateriasCentralizador() {
        return materiasCentralizador;
    }

    /**
     * @param materiasCentralizador the materiasCentralizador to set
     */
    public void setMateriasCentralizador(MateriaCentralizador[] materiasCentralizador) {
        this.materiasCentralizador = materiasCentralizador;
    }

    /**
     * @return the estudiantesCentralizador
     */
    public EstudianteCentralizador[] getEstudiantesCentralizador() {
        return estudiantesCentralizador;
    }

    /**
     * @param estudiantesCentralizador the estudiantesCentralizador to set
     */
    public void setEstudiantesCentralizador(EstudianteCentralizador[] estudiantesCentralizador) {
        this.estudiantesCentralizador = estudiantesCentralizador;
    }

    /**
     * @return the cantidadMaximaMaterias
     */
    public int getCantidadMaximaMaterias() {
        return cantidadMaximaMaterias;
    }

    /**
     * @param cantidadMaximaMaterias the cantidadMaximaMaterias to set
     */
    public void setCantidadMaximaMaterias(int cantidadMaximaMaterias) {
        this.cantidadMaximaMaterias = cantidadMaximaMaterias;
    }

    /**
     * @return the cantidadMaximaEstudiantes
     */
    public int getCantidadMaximaEstudiantes() {
        return cantidadMaximaEstudiantes;
    }

    /**
     * @param cantidadMaximaEstudiantes the cantidadMaximaEstudiantes to set
     */
    public void setCantidadMaximaEstudiantes(int cantidadMaximaEstudiantes) {
        this.cantidadMaximaEstudiantes = cantidadMaximaEstudiantes;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

}
