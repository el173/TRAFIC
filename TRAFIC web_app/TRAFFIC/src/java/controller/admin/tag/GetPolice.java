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
import model.db.PoliceStation;

/**
 *
 * @author Hashith
 */
public class GetPolice extends SimpleTagSupport{

    @Override
    public void doTag() throws JspException, IOException {
        String html = "";
        ArrayList<PoliceStation> police = new GetDetails().getPolice();
        for (PoliceStation p : police) {
            html += "<tr>";
            html += "<td>"+p.getUserIdusers()+"</td>";
            html += "<td>"+p.getStationName()+"</td>";
            html += "</tr>";
        }
        getJspContext().getOut().write(html);
    }
    
}
