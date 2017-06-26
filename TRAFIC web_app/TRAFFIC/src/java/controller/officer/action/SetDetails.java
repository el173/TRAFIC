/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.officer.action;

import controller.Tool;
import model.db.Driver;
import model.db.Status;
import model.db.Ticket;
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

    public void changeLicenseStatus(int ticketId) {
        Criteria c = session.createCriteria(model.db.Ticket.class);
        c.add(Restrictions.or(
                Restrictions.and(
                        Restrictions.eq("idticket", ticketId),
                        Restrictions.eq("status", session.load(model.db.Status.class, Tool.WARRANT))),
                Restrictions.eq("status", session.load(model.db.Status.class, Tool.WARRANT))));
        Ticket ticket = (Ticket) c.uniqueResult();

        Criteria createCriteria = session.createCriteria(model.db.Ticket.class);
        createCriteria.add(Restrictions.and(Restrictions.eq("driver", ticket.getDriver()),
                Restrictions.eq("status", session.load(model.db.Status.class, Tool.WARRANT))));

        Ticket t = (Ticket) createCriteria.uniqueResult();
        t.setStatus((Status) session.load(model.db.Status.class, Tool.TICKET_PAID));
        session.save(t);
        transaction.commit();
    }
}
