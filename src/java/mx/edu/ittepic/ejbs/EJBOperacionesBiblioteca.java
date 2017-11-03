/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ittepic.entitites.Libros;
import mx.edu.ittepic.utils.Message;

/**
 *
 * @author kon_n
 */
@Stateless
public class EJBOperacionesBiblioteca {

    @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public String getLibros() {
        Query q;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<Libros> listLibros = null;
        Message m = new Message();

        try {
            q = em.createNamedQuery("Libros.findAll");
            listLibros = q.getResultList();
        } catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException | PessimisticLockException | LockTimeoutException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("No se pudieron obtener los libros, intente nuevamente.");
            m.setDetail(e.toString());
            return gson.toJson(m);
        } catch (PersistenceException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("No se pudieron obtener los libros, intente nuevamente.");
            m.setDetail(e.toString());
            return gson.toJson(m);
        }
        return gson.toJson(listLibros);
    }

    public String createCategory(String nombre, String autor) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Message m = new Message();
        Libros libros = new Libros();
        libros.setNombre(nombre);
        libros.setAutor(autor);
        libros.setEstado(true);
        try {
            em.persist(libros);
            em.flush();
            m.setCode(HttpServletResponse.SC_OK); //200
            m.setMessage("El libro se creó correctamente con la clave " + libros.getClave());
            m.setDetail(gson.toJson(libros));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST); //400
            m.setMessage("No se pudo guardar el libro, intente nuevamente.");
            m.setDetail(e.toString());
        } catch (PersistenceException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST); //400
            m.setMessage("No se pudo guardar el libro, intente nuevamente.");
            m.setDetail(e.toString());
        }
        return gson.toJson(m);
    }

    public String updateLibro(int clave) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Message m = new Message();
        Libros l = em.find(Libros.class, clave);
        try {
            if (l != null) {
                l.setEstado(!l.getEstado());
                em.merge(l);

                m.setCode(HttpServletResponse.SC_OK);
                if (l.getEstado()) {
                    m.setMessage("El libro " + l.getNombre() + " se activo correctamente");
                } else {
                    m.setMessage("El usuario " + l.getNombre() + " se desactivo correctamente");
                }
                m.setDetail(gson.toJson(l));
            } else {
                m.setCode(HttpServletResponse.SC_BAD_REQUEST);
                m.setMessage("El libro no existe");
                m.setDetail("Libro no encontrado");
            }
            //Crear un nuevo registro en la BD
        } catch (IllegalArgumentException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("No se pudo actualizar el libro, intente nuevamente");
            m.setDetail(e.toString());
        }
        return gson.toJson(m);
    }
}
