/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.driver.action;

import java.util.ArrayList;
import model.db.Driver;
import model.db.Ticket;
import model.db.TrafficOfficer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Hashith
 */
public class GetDetail {

    public Session session;

    public GetDetail() {
        if (session == null) {
            session = controller.Tool.getSession();
        }
    }

    /**
     *
     * @param driverId
     * @return Driver Object
     */
    public Driver getDriverById(String driverId) {
        Criteria c = session.createCriteria(model.db.Driver.class);
        c.add(Restrictions.eq("licenceNumber", driverId.toUpperCase()));
        return (model.db.Driver) c.uniqueResult();
    }

    /**
     *
     * @param driver
     * @return Ticket Array List
     */
    public ArrayList<model.db.Ticket> getTicketByDriver(Driver driver) {
        Criteria c = session.createCriteria(model.db.Ticket.class);
        c.add(Restrictions.eq("driver", driver));
        c.addOrder(Order.desc("issueDate"));
        return (ArrayList<Ticket>) c.list();
    }

    public TrafficOfficer getOfficerById(int id){
        return (TrafficOfficer) session.load(model.db.TrafficOfficer.class, id);
    }
}
