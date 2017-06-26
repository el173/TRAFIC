/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin.action;

import java.util.ArrayList;
import model.db.Fine;
import model.db.PoliceStation;
import model.db.TrafficOfficer;
import org.hibernate.Session;

/**
 *
 * @author Hashith
 */
public class GetDetails {
    public Session session;

    public GetDetails() {
        if (session == null) {
            session = controller.Tool.getSession();
        }
    }
    
    public ArrayList<model.db.TrafficOfficer> getOfficers(){
        return (ArrayList<TrafficOfficer>) session.createCriteria(model.db.TrafficOfficer.class).list();
    }
    
    public ArrayList<model.db.PoliceStation> getPolice(){
        return (ArrayList<PoliceStation>) session.createCriteria(model.db.PoliceStation.class).list();
    }
    
    public ArrayList<model.db.Fine> getFines(){
        return (ArrayList<Fine>) session.createCriteria(model.db.Fine.class).list();
    }
}
