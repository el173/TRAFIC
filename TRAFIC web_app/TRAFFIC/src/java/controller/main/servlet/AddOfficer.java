/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.servlet;

import controller.Tool;
import controller.main.action.SetDetails;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.db.TrafficOfficer;
import model.db.User;

/**
 *
 * @author Hashith
 */
@WebServlet(name = "AddOfficer", urlPatterns = {"/AddOfficer"})
public class AddOfficer extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                String officerId = request.getParameter("txtOfficerId");
                String name = request.getParameter("txtName");
                String userName = request.getParameter("txtUserName");
                String password = request.getParameter("txtPassword");

                System.out.println(name);
                
                model.db.User user = new User();
                user.setPassword(password);
                user.setUsername(userName);

                model.db.TrafficOfficer officer = new TrafficOfficer();
                officer.setName(name);
                officer.setOfficerId(officerId);
                officer.setPoliceStation(((User)request.getSession().getAttribute("user")).getPoliceStation());

                new SetDetails().addNewOfficer(user, officer,name);
                out.write("1");
            } catch (Exception e) {
                out.write("0");
                e.printStackTrace();
            }
        }
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
        processRequest(request, response);
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
