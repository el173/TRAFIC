/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.main.action;

import controller.Tool;
import java.util.Date;
import model.db.PoliceStation;
import model.db.Status;
import model.db.Ticket;
import model.db.TrafficOfficer;
import model.db.User;
import model.db.UserLogin;
import model.db.UserType;
import model.db.WorkingHistory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Hashith
 */
public class SetDetails {

    public Session session;
    Transaction transaction;

    public SetDetails() {
        if (session == null) {
            session = Tool.getSession();
        }

        if (transaction == null) {
            transaction = session.beginTransaction();
        }
    }

    public void addLogin(model.db.User user) {
        model.db.UserLogin login = new UserLogin();
        login.setLoggedDatetime(new Date());
        login.setUser(user);

        session.save(login);
        transaction.commit();
        System.gc();
    }

    public void addNewOfficer(User u, TrafficOfficer o, String name) {
        User user = new User();
        user.setPassword(u.getPassword());
        user.setUsername(u.getUsername());
        user.setStatus((Status) session.load(model.db.Status.class, Tool.STATUS_ACTIVE));
        user.setUserType((UserType) session.load(model.db.UserType.class, Tool.USER_TYPE_OFFICER));
        session.save(user);

        TrafficOfficer officer = new TrafficOfficer();
        officer.setName(name);
        officer.setPoliceStation(o.getPoliceStation());
        officer.setOfficerId(o.getOfficerId());
        officer.setUser(user);
        session.save(officer);

        WorkingHistory history = new WorkingHistory();
        history.setStartDate(new Date());
        history.setTrafficOfficer(officer);
        session.save(history);

        transaction.commit();
        System.gc();
    }

    public void changeUserStaus(int userId, int status) {
        User user = (User) session.load(User.class, userId);
        user.setStatus((Status) session.load(Status.class, status));
        session.save(user);
        transaction.commit();
    }

    public void setToHQ(int officerId, PoliceStation policeStation) {
        TrafficOfficer officer = (TrafficOfficer) session.load(TrafficOfficer.class, officerId);
        officer.setPoliceStation((PoliceStation) session.load(PoliceStation.class, Tool.HQ));
        session.save(officer);

        Criteria createCriteria = session.createCriteria(model.db.WorkingHistory.class);
        createCriteria.add(Restrictions.and(
                Restrictions.eq("trafficOfficer", ((TrafficOfficer) session.load(model.db.TrafficOfficer.class, officerId))),
                Restrictions.eq("policeStation", policeStation))
        );
        WorkingHistory history = (WorkingHistory) createCriteria.uniqueResult();
        history.setEndDate(new Date());
        session.save(history);

        transaction.commit();
    }

    public void assignFromHQ(int officerId, PoliceStation policeStation) {
        TrafficOfficer officer = (TrafficOfficer) session.load(TrafficOfficer.class, officerId);
        officer.setPoliceStation(policeStation);
        session.save(officer);

        WorkingHistory history = new WorkingHistory();
        history.setStartDate(new Date());
        history.setPoliceStation(policeStation);
        history.setTrafficOfficer(officer);
        session.save(history);

        transaction.commit();
        System.gc();
    }

    public void updatePassword(int officerId, String pass) {
        User user = (User) session.load(model.db.User.class, officerId);
        user.setPassword(pass);
        session.save(user);
        transaction.commit();
    }

    public void markAsPaid(int ticketId) {
        Ticket ticket = (Ticket) session.load(model.db.Ticket.class, ticketId);
        ticket.setStatus((Status) session.load(model.db.Status.class, Tool.TICKET_PAID));
        session.save(ticket);
        transaction.commit();
    }

    public void markAsWarrant(int ticketId) {
        Ticket ticket = (Ticket) session.load(model.db.Ticket.class, ticketId);
        ticket.setStatus((Status) session.load(model.db.Status.class, Tool.WARRANT));
        session.save(ticket);
        transaction.commit();
    }
}
