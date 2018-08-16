/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ticket.action;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author EL173
 */
public class Vehicle {
    
    private Session session;

    public Vehicle() {
        if(session == null) {
            session = controller.Utils.getSession();
        }
    }
    
    public model.Vehicle getVehicleByNumber(String vehicleNum) {
        Criteria c = session.createCriteria(model.Vehicle.class);
        c.add(Restrictions.eq("number", vehicleNum.toUpperCase()));
        model.Vehicle vehicle = (model.Vehicle) c.uniqueResult();
        session.close();
        return vehicle;
    }
    
}
