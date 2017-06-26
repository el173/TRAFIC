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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import model.db.WorkingHistory;

/**
 *
 * @author Hashith
 */
public class GetOfficerList extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        model.db.User user = (model.db.User) getJspContext().getAttribute("user", PageContext.SESSION_SCOPE);
        GetDetail detail = new GetDetail();

        ArrayList<model.db.WorkingHistory> list = detail.getOfficer(user.getPoliceStation());
        String html = "";
        for (WorkingHistory t : list) {

            Date d = t.getEndDate();

            String removed = d == null ? "" : "disabled";
            String removedName = d == null ? "Remove" : "Transferred";
            String endDate = d == null ? "N/A" : Tool.getDate(d);

            html += "<tr>";
            html += "<td>" + t.getTrafficOfficer().getName() + "-" + t.getTrafficOfficer().getOfficerId() + "</td>";
            html += "<td>" + Tool.getDate(t.getStartDate()) + "</td>";
            html += "<td>" + endDate +"</td>";
            html += "<td>"
                    + (t.getTrafficOfficer().getUser().getStatus().getIdstatus() == Tool.STATUS_ACTIVE
                            ? "<button " + removed + " onclick='changeStatus(" + t.getTrafficOfficer().getUser().getIdusers() + "," + t.getTrafficOfficer().getUser().getStatus().getIdstatus() + ")' class='btn btn-sm btn-success'>Active"
                            : "<button " + removed + " onclick='changeStatus(" + t.getTrafficOfficer().getUser().getIdusers() + "," + t.getTrafficOfficer().getUser().getStatus().getIdstatus() + ")' class='btn btn-sm btn-primary'>Deactive")
                    + "</button>"
                    + "</td>";
            html += "<td>"
                    + "<button onclick='resetPassword(" + t.getTrafficOfficer().getUser().getIdusers() + ")' class='btn btn-sm btn-warning' " + removed + ">"
                    + "Reset Password"
                    + "</td>";
            html += "<td><button onclick='removeOfficer(" + t.getTrafficOfficer().getUser().getIdusers() + ")' class='btn btn-sm btn-danger' " + removed + ">"
                    + removedName
                    + "</td>";
            html += "</tr>";
        }
        getJspContext().getOut().write(html);
    }
}
