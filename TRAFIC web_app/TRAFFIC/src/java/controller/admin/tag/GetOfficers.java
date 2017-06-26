/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin.tag;

import controller.admin.action.GetDetails;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import model.db.TrafficOfficer;

/**
 *
 * @author Hashith
 */
public class GetOfficers extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        String html = "";
        ArrayList<TrafficOfficer> officers = new GetDetails().getOfficers();
        for (TrafficOfficer f : officers) {
            html += "<tr>";
            html += "<td>" + f.getName() + "</td>";
            html += "<td>" + f.getOfficerId() + "</td>";
            html += "<td>" + f.getPoliceStation().getStationName() + "</td>";
            html += "</tr>";
        }
        getJspContext().getOut().write(html);
    }

}
