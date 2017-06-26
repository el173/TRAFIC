/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.officer.action;

import controller.Tool;
import java.util.ArrayList;
import model.db.Driver;
import model.db.Fine;
import model.db.PoliceStation;
import model.db.Ticket;
import model.db.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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

    public model.db.User checkPoliceLogin(String username, String password) {
        Criteria criteria = session.createCriteria(model.db.User.class);
        criteria.add(Restrictions.eq("username", username)).
                add(Restrictions.eq("password", password)).
                //                add(Restrictions.eq("policeStation", getPoliceByName(police))).
                add(Restrictions.eq("userType", session.load(model.db.UserType.class, Tool.USER_TYPE_OFFICER))).
                add(Restrictions.eq("status", session.load(model.db.Status.class, Tool.STATUS_ACTIVE)));

        return (User) criteria.uniqueResult();
    }

    private PoliceStation getPoliceByName(String police) {
        return (PoliceStation) session.createCriteria(model.db.PoliceStation.class).add(Restrictions.eq("stationName", police)).uniqueResult();
    }

    public Driver getDriverByLicense(String licenseNumber) {
        return (Driver) session.createCriteria(model.db.Driver.class).add(Restrictions.eq("licenceNumber", licenseNumber)).uniqueResult();
    }

    public ArrayList<model.db.Ticket> checkLicenseHistory(String licenseNumber) {
        return (ArrayList<model.db.Ticket>) session.createCriteria(model.db.Ticket.class).
                add(Restrictions.eq("drivingLicenceIddrivingLicence", licenseNumber)).list();
    }

    public ArrayList<model.db.Fine> getOfenseList() {
        return (ArrayList<Fine>) session.createCriteria(model.db.Fine.class).list();
    }

    public boolean getWarrant(String licenseNumber) {
        Driver driver = getDriverByLicense(licenseNumber);
        Criteria createCriteria = session.createCriteria(model.db.Ticket.class);
        createCriteria.add(Restrictions.and(Restrictions.eq("driver", driver),
                Restrictions.eq("status", session.load(model.db.Status.class, Tool.WARRANT))));

        Ticket t = (Ticket) createCriteria.uniqueResult();
        
        return t == null;
    }
}
