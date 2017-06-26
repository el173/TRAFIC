/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.servlet;

import controller.Encrypter;
import controller.Tool;
import controller.main.action.GetDetail;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.db.User;

/**
 *
 * @author Hashith
 */
@WebServlet(name = "ActionController", urlPatterns = {"/ActionController"})
public class ActionController extends HttpServlet {

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
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        try (PrintWriter out = response.getWriter()) {
            String sParam = request.getParameter("sParam");
            controller.Encrypter encrypter = new Encrypter();
            if (sParam == null) {
                try {
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");

                    if (!(username.isEmpty() || password.isEmpty())) {
                        controller.main.action.GetDetail detail = new GetDetail();
                        User userPolice = detail.checkPoliceLogin(username, password);
                        if (userPolice != null) {
                            HttpSession session = request.getSession();
                            session.setAttribute("user", userPolice);
                            addUserLoging(userPolice);
                            request.getRequestDispatcher("home.jsp").forward(request, response);
                        } else {
                            request.setAttribute("login", "faild");
                            request.getRequestDispatcher("index.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("login", "faild");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    if (request.getSession().getAttribute("user") == null) {
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    } else {
                        addUserLoging((User) request.getSession().getAttribute("user"));
                        request.getRequestDispatcher("home.jsp").forward(request, response);
                    }
                }
            } else if (sParam.equalsIgnoreCase(encrypter.encrypt("logout"))) {
                out.write(sParam);
                HttpSession httpSession = request.getSession();
                httpSession.invalidate();
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else if (sParam.equalsIgnoreCase(encrypter.encrypt("home"))) {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            } else if (sParam.equalsIgnoreCase(encrypter.encrypt("reg"))) {
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else if (sParam.equalsIgnoreCase(encrypter.encrypt("tick"))) {
                request.getRequestDispatcher("tickets.jsp").forward(request, response);
            } else if (sParam.equalsIgnoreCase(encrypter.encrypt("driver"))) {
                request.getRequestDispatcher("drivers.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void addUserLoging(User user) {
        new controller.main.action.SetDetails().addLogin(user);
    }
}
