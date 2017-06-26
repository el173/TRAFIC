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
import model.db.Fine;

/**
 *
 * @author Hashith
 */
public class GetFines extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        String html = "";
        ArrayList<Fine> fines = new GetDetails().getFines();
        for (Fine f : fines) {
            html += "<tr>";
            html += "<td>" + f.getDescription() + "</td>";
            html += "<td>" + f.getFine() + "</td>";
            html += "</tr>";
        }
        getJspContext().getOut().write(html);
    }

}
