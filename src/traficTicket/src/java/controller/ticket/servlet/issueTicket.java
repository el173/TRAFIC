/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ticket.servlet;

import controller.ticket.action.Ticket;
import controller.ticket.action.Vehicle;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author EL173
 */
@WebServlet(name = "issueTicket", urlPatterns = {"/issueTicket"})
public class issueTicket extends HttpServlet {

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
        response.addHeader("Access-Control-Allow-Origin", "*");

        JSONObject out_object = new JSONObject();
        if ("POST".equals(request.getMethod())) {
            try {
                String vehicleNumber = request.getParameter("vch_number");
                float speed = Float.parseFloat(request.getParameter("speed"));
                
                Vehicle vehicleAction = new Vehicle();
                model.Vehicle vehicleByNumber = vehicleAction.getVehicleByNumber(vehicleNumber);
                
                if(vehicleByNumber != null) {
                    Ticket ticketAction = new Ticket();
                    if(ticketAction.addSpeedFineToTicket(vehicleByNumber, speed)) {
                        out_object.put("success", true);
                        out_object.put("info", "ticket_updated");
                        out_object.put("ticket_no", vehicleByNumber.getTicket().getIdticket());
                    }
                    out_object.put("success", false);
                    out_object.put("error", "ticket_not_found");
                    out_object.put("info", "ticket_not_updated");
                }else {
                    out_object.put("success", false);
                    out_object.put("info", "vehicle_not_found");
                }
                
            } catch (Exception e) {
                out_object.put("success", false);
                out_object.put("info", "error_in_action");
                out_object.put("error", e.getMessage());
            }
        } else {
            out_object.put("success", false);
            out_object.put("info", "method_not_found");
        }

        response.getWriter().print(out_object);
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
