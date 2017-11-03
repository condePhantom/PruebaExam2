/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ittepic.ejbs.EJBOperacionesBiblioteca;
import mx.edu.ittepic.utils.Message;

/**
 *
 * @author kon_n
 */
@WebServlet(name = "CrearLibro", urlPatterns = {"/CrearLibro"})
public class CrearLibro extends HttpServlet {
    @EJB
    private EJBOperacionesBiblioteca ejb;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter p = response.getWriter();
        
        Message m = new Message();
        m.setCode(401);
        m.setMessage("Don't enter");
        m.setDetail("Not autorized method");
        
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        p.print(gson.toJson(m));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        
        PrintWriter p = response.getWriter();
        
        String nombre = request.getParameter("nombre");
        String autor = request.getParameter("autor");
        
        if(nombre == null || nombre.equals("")){
            Message m = new Message();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("Parámetros incorrectos");
            m.setDetail("No se proporcionó ningún parámentro o el tipo de dato es incorrecto");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            p.write(gson.toJson(m));
        } else {
            p.write(ejb.createCategory(nombre, autor));
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
