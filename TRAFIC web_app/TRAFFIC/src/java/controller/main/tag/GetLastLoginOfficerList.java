/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.tag;

import controller.Tool;
import controller.main.action.GetDetail;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import model.db.UserLogin;

/**
 *
 * @author Hashith
 */
public class GetLastLoginOfficerList extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        try {
            
            model.db.User user = (model.db.User) getJspContext().getAttribute("user", PageContext.SESSION_SCOPE);
            GetDetail getDetail = new controller.main.action.GetDetail();
            List<model.db.UserLogin> logins = getDetail.getLoginsByUserType(user, Tool.USER_TYPE_OFFICER, 6);
//            List<model.db.UserLogin> logins = Tool.getSession().createCriteria(model.db.UserLogin.class).list();
            String html = "";
            
            if (logins.isEmpty()) {
                html = "<li>"
                        + "No Record Found"
                        + "</li>";
            } else {
                for (UserLogin l : logins) {
//                    System.out.println(getDetail.getOfficerByUser(l.getUser()).getName());
                    if(l.getUser().getUserType().getIduserType() == Tool.USER_TYPE_OFFICER 
                            && l.getUser().getTrafficOfficer().getPoliceStation().getUserIdusers() == user.getPoliceStation().getUserIdusers()){
                        html = "<li>"
                            + "<i class=\"fa fa-user fa-4x pull-left\"></i>"
                            + " <div class=\"news-item-info\">"
                            + "     <div class=\"name\"><a href=\"#\">" + l.getUser().getTrafficOfficer().getOfficerId() + "</a></div>"
                            + "     <div class=\"time\">Last logged-in: <br>" + controller.Tool.getDate(l.getLoggedDatetime()) + "</div>"
                            + " </div>"
                            + "</li>";
                    }
                }
            }

            getJspContext().getOut().write(html);
        } catch (Exception e) {
        }
    }

}
