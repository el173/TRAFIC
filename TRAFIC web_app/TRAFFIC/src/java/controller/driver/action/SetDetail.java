/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.driver.action;

import controller.Tool;
import java.util.Date;
import model.db.Payment;
import model.db.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Hashith
 */
public class SetDetail {

    public Session session;
    Transaction transaction;

    public SetDetail() {
        if (session == null) {
            session = Tool.getSession();
        }

        if (transaction == null) {
            transaction = session.beginTransaction();
        }
    }

    public boolean addTicketPayment(int ticketId) {
        try {
            model.db.Ticket ticket = (model.db.Ticket) session.load(model.db.Ticket.class, ticketId);

            model.db.Payment payment = new Payment();
            payment.setDriver(ticket.getDriver());
            payment.setPaymentDate(new Date());
            payment.setTicket(ticket);
            session.save(payment);

            ticket.setStatus((Status) session.load(Status.class, Tool.TICKET_PAID));
            session.saveOrUpdate(ticket);

            transaction.commit();
            System.gc();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
