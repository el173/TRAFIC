/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.tag;

import controller.Tool;
import controller.main.action.GetDetail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import model.db.Fine;
import model.db.Ticket;

/**
 *
 * @author Hashith
 */
public class GetTickets extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        model.db.User user = (model.db.User) getJspContext().getAttribute("user", PageContext.SESSION_SCOPE);
        GetDetail detail = new GetDetail();
        ArrayList<Ticket> ticketByPolice = detail.getTicketByPolice(user.getPoliceStation());
        String html = "";
        for (Ticket t : ticketByPolice) {
            Set<model.db.Fine> fines = t.getFines();
            int dateDif = Tool.dateCal(Tool.getDate(t.getIssueDate()), Tool.getDate(new Date()));
            String dis = dateDif > 14 ? "disabled" : "";
            String err = dateDif > 14 ? "class='error'" : "";
            boolean warrant = detail.getWarrant(t.getDrivingLicenceIddrivingLicence());
            String warnt = "";
            if (!warrant) {
                warnt += "<p>Under Warent</p>";
                warnt += "<button onclick('markAsWarrant(" + t.getIdticket() + ")') class='btn btn-sm btn-danger'>Mark As Warrant<button>";
            }
            html += "<tr>";
            html += "<td> <p " + err + ">" + Tool.getDate(t.getIssueDate()) + "</p>" + t.getStatus().getStatus();
            html += "<br>" + warnt;
            html += "</td>";
            html += "<td>" + t.getDriver().getLicenceNumber() + "/" + t.getDriver().getName();
            html += "<br>" + (t.getStatus().getIdstatus() == Tool.TICKET_UNPAID
                    ? "<button " + dis + " onclick='markAsPaid(" + t.getIdticket() + ")' class='btn btn-sm btn-warning'>Mark as PAID</button>" : "");
            html += (!warrant
                    ? "<button " + dis + " onclick='closeWarrant(" + t.getIdticket() + ");' class='btn btn-sm btn-warning'>Close Warrant</button>"
                    : "");
            html += "</td>";
            html += "<td>" + t.getTrafficOfficer().getOfficerId() + "/" + t.getTrafficOfficer().getName() + "</td>";
            html += "<td>";
            for (Fine f : fines) {
                html += f.getDescription() + "-" + f.getFine() + "<br>";
            }
            html += "Total :" + t.getTotalAmount();
            html += "</td>";
            html += "<td>" + t.getVechicleNumber() + "</td>";
            html += "<td>";
            html += "<div id=\"map" + t.getIdticket() + "\" style=\"width:200px;height:200px;background:yellow\"></div>\n"
                    + "\n"
                    + "        <script>\n"
                    + "            function myMap" + t.getIdticket() + "() {\n"
                    + "                var myLatLng" + t.getIdticket() + " = {lat: " + t.getLatitude() + ", lng: " + t.getLongitude() + "};"
                    + "                var mapOptions" + t.getIdticket() + "= {\n"
                    + "                    center: new google.maps.LatLng(myLatLng" + t.getIdticket() + "),\n"
                    + "                    zoom: 15,\n"
                    + "                    mapTypeId: google.maps.MapTypeId.HYBRID\n"
                    + "                }\n"
                    + "                var map" + t.getIdticket() + " = new google.maps.Map(document.getElementById(\"map" + t.getIdticket() + "\"), mapOptions" + t.getIdticket() + ");\n"
                    + "                var marker" + t.getIdticket() + " = new google.maps.Marker({\n"
                    + "                     position: myLatLng" + t.getIdticket() + ",\n"
                    + "                     map: map" + t.getIdticket() + ",\n"
                    + "                     title: ''\n"
                    + "                 });"
                    + "            }\n"
                    + "        </script>\n"
                    + "        <script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCwjb8INn0GSRBM2gyGM4M1MxVw5xaJxKA&callback=myMap" + t.getIdticket() + "\"></script>";
            html += "</td>";
            html += "</tr>";
        }
        getJspContext().getOut().write(html);
    }

}
