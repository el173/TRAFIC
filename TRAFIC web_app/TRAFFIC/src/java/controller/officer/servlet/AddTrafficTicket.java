/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.officer.servlet;

import controller.Tool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.db.Driver;
import model.db.Fine;
import model.db.Status;
import model.db.Ticket;
import model.db.TrafficOfficer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Hashith
 */
@WebServlet(name = "AddTrafficTicket", urlPatterns = {"/AddTrafficTicket"})
public class AddTrafficTicket extends HttpServlet {

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
//        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        try (PrintWriter out = response.getWriter()) {
            try {
                int officerId = Integer.parseInt(request.getParameter("officerID"));
                String licenseNumber = request.getParameter("DLnumber");
                double totalAmount = Double.parseDouble(request.getParameter("total"));
                String logitude = request.getParameter("logi");
                String latitude = request.getParameter("lati");
                String vehicleNumber = request.getParameter("vchNum");
                String[] offenses = request.getParameterValues("ofens[]");
//            out.write(Arrays.toString(offenses)+"\n"+licenseNumber+
//                    "\n"+officerId+"\n"+totalAmount+"\n"+logitude+"\n"+latitude+"\n"+vehicleNumber);

                Session s = Tool.getSession();
                
                TrafficOfficer officer = (TrafficOfficer) s.load(TrafficOfficer.class, officerId);
                Driver driverByLicense = (Driver) s.createCriteria(model.db.Driver.class).
                        add(Restrictions.eq("licenceNumber", licenseNumber)).uniqueResult();
                
                Transaction transaction = s.beginTransaction();
                Ticket ticket = new Ticket();
                ticket.setDriver(driverByLicense);
                ticket.setDrivingLicenceIddrivingLicence(licenseNumber);
                ticket.setIssueDate(new Date());
                ticket.setLatitude(latitude);
                ticket.setLongitude(logitude);
                ticket.setTotalAmount(totalAmount);
                ticket.setTrafficOfficer(officer);
                ticket.setPoliceStation(officer.getPoliceStation());
                ticket.setVechicleNumber(vehicleNumber);
                ticket.setStatus((Status) s.load(model.db.Status.class, Tool.TICKET_UNPAID));
                Set<Fine> fines = new HashSet<>();
                for (String f : offenses) {
                    fines.add((Fine) s.load(Fine.class, Integer.parseInt(f)));
                }
                ticket.setFines(fines);
                s.save(ticket);

                Driver driver = driverByLicense;
                driver.setStatus((Status) s.load(model.db.Status.class, Tool.STATUS_DEACTIVE));
                s.save(driverByLicense);

                transaction.commit();

                System.gc();
                out.write("1");
            } catch (Exception e) {
                e.printStackTrace();
                out.write("0");
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
