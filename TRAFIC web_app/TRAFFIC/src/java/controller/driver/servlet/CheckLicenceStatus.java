/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.driver.servlet;

import controller.Tool;
import controller.driver.action.GetDetail;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.db.Driver;
import model.db.Ticket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Hashith
 */
@WebServlet(name = "CheckLicenceStatus", urlPatterns = {"/CheckLicenceStatus"})
public class CheckLicenceStatus extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            String licenceNumber = request.getParameter("txtCheckNumber");
            System.out.println(licenceNumber);
            if (licenceNumber != null) {
                GetDetail detail = new GetDetail();
                Driver driver = detail.getDriverById(licenceNumber);
                if (driver != null) {
                    ArrayList<Ticket> tickets = detail.getTicketByDriver(driver);
                    JSONArray ticket_array = new JSONArray();
                    JSONObject ticket_list = new JSONObject();
                    for (Ticket t : tickets) {
                        int dateCal = Tool.dateCal(Tool.getDate(t.getIssueDate()), Tool.getDate(new Date()));
                        JSONObject ticket = new JSONObject();
                        ticket.put("id", t.getIdticket() + "");
                        ticket.put("date", Tool.getDate(t.getIssueDate()));
                        ticket.put("status", t.getStatus().getIdstatus() == Tool.TICKET_PAID ? "Paid" : "Unpaid");
                        ticket.put("amount", t.getTotalAmount() + "");
                        ticket.put("police", t.getPoliceStation().getStationName());
                        ticket.put("officer", t.getTrafficOfficer().getOfficerId());
                        ticket.put("paid", (dateCal > 14 ?"outdate":"ok"));
                        ticket_array.add(ticket);
                    }

                    ticket_list.put("ticket", ticket_array);

                    JSONObject jsono1 = new JSONObject();
                    jsono1.put("name", driver.getName());
                    jsono1.put("tickelist", ticket_list);

                    JSONObject jsono2 = new JSONObject();
                    jsono2.put("all", jsono1);

                    System.out.println(jsono2);
                    System.gc();
                    out.print(jsono2);
                } else {
                    out.write("0");
                }
                detail.session.close();
            }
            System.gc();
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
