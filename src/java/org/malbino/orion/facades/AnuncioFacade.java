/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import org.malbino.orion.entities.Anuncio;
import org.malbino.orion.enums.CategoriaAnuncio;
import org.malbino.orion.enums.Estado;
import org.malbino.orion.util.Fecha;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class AnuncioFacade extends AbstractFacade<Anuncio> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AnuncioFacade.class);

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public AnuncioFacade() {
        super(Anuncio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Anuncio> listaAnunciosPublicados() {
        List<Anuncio> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Anuncio a WHERE a.vencimiento>=:fecha AND a.estado=:estado ORDER BY a.publicacion DESC");
            q.setParameter("fecha", Fecha.getDate());
            q.setParameter("estado", Estado.PUBLICADO);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }

    public List<Anuncio> listaAnuncios() {
        List<Anuncio> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Anuncio a ORDER BY a.publicacion");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("listaAnuncio\n" + e.getMessage());
        }

        return l;
    }

    public List<Anuncio> buscar(String keyword) {
        List<Anuncio> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Anuncio a WHERE "
                    + "LOWER(a.titulo) LIKE LOWER(:keyword) "
                    + "ORDER BY a.titulo");

            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }

    public List<Anuncio> buscarPublicados(String keyword) {
        List<Anuncio> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Anuncio a WHERE a.vencimiento>=:fecha AND a.estado=:estado AND "
                    + "LOWER(a.titulo) LIKE LOWER(:keyword) "
                    + "ORDER BY a.titulo");

            q.setParameter("fecha", Fecha.getDate());
            q.setParameter("estado", Estado.PUBLICADO);

            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }

    public List<Anuncio> buscarPublicados(CategoriaAnuncio categoriaAnuncio, String keyword) {
        List<Anuncio> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a FROM Anuncio a WHERE a.vencimiento>=:fecha AND a.estado=:estado AND a.categoriaAnuncio=:categoriaAnuncio AND "
                    + "LOWER(a.titulo) LIKE LOWER(:keyword) "
                    + "ORDER BY a.titulo");

            q.setParameter("fecha", Fecha.getDate());
            q.setParameter("estado", Estado.PUBLICADO);

            q.setParameter("categoriaAnuncio", categoriaAnuncio);
            q.setParameter("keyword", "%" + keyword + "%");

            l = q.getResultList();
        } catch (Exception e) {
            log.error("buscar\n" + e.getMessage());
        }

        return l;
    }

    public Long maximoCodigo(Date fecha) {
        Long l = null;

        try {
            Query q = em.createQuery("SELECT MAX(a.codigo) FROM Anuncio a WHERE a.creacion BETWEEN :inicio AND :fin");
            q.setParameter("inicio", Fecha.getInicioAño(fecha));
            q.setParameter("fin", Fecha.getFinAño(fecha));

            l = (Long) q.getSingleResult();
        } catch (Exception e) {

        }

        return l;
    }
}
