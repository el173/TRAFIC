/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.action;

import controller.Tool;
import java.util.ArrayList;
import java.util.List;
import model.db.Driver;
import model.db.PoliceStation;
import model.db.Ticket;
import model.db.TrafficOfficer;
import model.db.User;
import model.db.WorkingHistory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Hashith
 */
public class GetDetail {

    public static final int ALL = 0;
    public Session session;

    public GetDetail() {
        if (session == null) {
            session = controller.Tool.getSession();
        }
    }

    public ArrayList<model.db.PoliceStation> getPoliceStation() {
        return (ArrayList<PoliceStation>) session.createCriteria(model.db.PoliceStation.class).list();
    }

    public ArrayList<model.db.Driver> getDriverList() {
        return (ArrayList<Driver>) session.createCriteria(model.db.Driver.class).list();
    }

    public model.db.User checkPoliceLogin(String username, String password) {
        Criteria criteria = session.createCriteria(model.db.User.class);
        criteria.add(Restrictions.eq("username", username)).
                add(Restrictions.eq("password", password)).
                //                add(Restrictions.eq("policeStation", getPoliceByName(police))).
                add(Restrictions.eq("userType", session.load(model.db.UserType.class, Tool.USER_TYPE_POLICE))).
                add(Restrictions.eq("status", session.load(model.db.Status.class, Tool.STATUS_ACTIVE)));

        return (User) criteria.uniqueResult();
    }

    private PoliceStation getPoliceByName(String police) {
        return (PoliceStation) session.createCriteria(model.db.PoliceStation.class).add(Restrictions.eq("stationName", police)).uniqueResult();
    }

    public List<model.db.UserLogin> getLoginsByUserType(model.db.User user, int userType, int count) {
        Criteria loginCriteria = session.createCriteria(model.db.UserLogin.class);
        try {
//            Criteria officer = session.createCriteria(model.db.TrafficOfficer.class).
//                    add(Restrictions.eq("policeStation", user.getPoliceStation()));
//            
//            loginCriteria.add(Restrictions.eq("user", officer));

//            if (count != ALL) {
//                loginCriteria.setMaxResults(count);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginCriteria.list();
    }

    public model.db.TrafficOfficer getOfficerByUser(model.db.User user) {
        return (TrafficOfficer) session.createCriteria(model.db.TrafficOfficer.class).
                add(Restrictions.eq("user", user)).uniqueResult();
    }

    public model.db.WorkingHistory getWorkingHistoryByUser(TrafficOfficer officer, PoliceStation policeStation) {
        return (WorkingHistory) session.createCriteria(WorkingHistory.class).
                add(Restrictions.eq("trafficOfficer", officer)).
                add(Restrictions.eq("policeStation", policeStation)).uniqueResult();
    }

    public ArrayList<model.db.TrafficOfficer> getHQOfficers() {
        return (ArrayList<TrafficOfficer>) session.createCriteria(TrafficOfficer.class).
                add(Restrictions.eq("policeStation", session.load(PoliceStation.class, Tool.HQ))).list();
    }

    public ArrayList<model.db.WorkingHistory> getOfficer(PoliceStation policeStation) {
        Criteria c = session.createCriteria(model.db.WorkingHistory.class);
        c.add(Restrictions.or(
                Restrictions.eq("policeStation", policeStation),
                Restrictions.eq("policeStation", session.load(PoliceStation.class, Tool.HQ))
        ));
        return (ArrayList<WorkingHistory>) c.list();
    }

    public Driver getDriverByLicense(String licenseNumber) {
        return (Driver) session.createCriteria(model.db.Driver.class).add(Restrictions.eq("licenceNumber", licenseNumber)).uniqueResult();
    }

    public ArrayList<model.db.Ticket> getTicketByPolice(PoliceStation policeStation) {
        return (ArrayList<Ticket>) session.createCriteria(model.db.Ticket.class).
                add(Restrictions.eq("policeStation", policeStation)).list();
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
