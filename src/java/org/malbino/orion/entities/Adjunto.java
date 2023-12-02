/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author malbino
 */
@Entity
@Table(name = "adjunto")
public class Adjunto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_adjunto;

    private String nombre;
    private String url;

    @JoinColumn(name = "id_notapasantia")
    @ManyToOne
    private NotaPasantia notaPasantia;

    public Adjunto() {
    }

    public Adjunto(String nombre, String url, NotaPasantia notaPasantia) {
        this.nombre = nombre;
        this.url = url;
        this.notaPasantia = notaPasantia;
    }

    /**
     * @return the id_adjunto
     */
    public Integer getId_adjunto() {
        return id_adjunto;
    }

    /**
     * @param id_adjunto the id_adjunto to set
     */
    public void setId_adjunto(Integer id_adjunto) {
        this.id_adjunto = id_adjunto;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the notaPasantia
     */
    public NotaPasantia getNotaPasantia() {
        return notaPasantia;
    }

    /**
     * @param notaPasantia the notaPasantia to set
     */
    public void setNotaPasantia(NotaPasantia notaPasantia) {
        this.notaPasantia = notaPasantia;
    }
    
}
