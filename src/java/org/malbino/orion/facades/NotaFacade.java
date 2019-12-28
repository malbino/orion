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
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Nota;

/**
 *
 * @author malbino
 */
@Stateless
@LocalBean
public class NotaFacade extends AbstractFacade<Nota> {

    @PersistenceContext(unitName = "orionPU")
    private EntityManager em;

    public NotaFacade() {
        super(Nota.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Nota> listaNotas(int id_inscrito) {
        List<Nota> l = new ArrayList();

        try {
            Query q = em.createQuery("SELECT n FROM Nota n JOIN n.inscrito i JOIN n.grupo g JOIN g.materia m WHERE i.id_inscrito=:id_inscrito ORDER BY m.nivel, m.numero");
            q.setParameter("id_inscrito", id_inscrito);

            l = q.getResultList();
        } catch (Exception e) {

        }

        return l;
    }
}
