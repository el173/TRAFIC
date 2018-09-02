/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ticket.action;

import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.weld.util.collections.ArraySet;

/**
 *
 * @author EL173
 */
public class Ticket {

    private Session session;

    public Ticket() {
        if (session == null) {
            session = controller.Utils.getSession();
        }
    }
    
    public boolean addSpeedFineToTicket(model.Vehicle vehicle, float speed) {
        Transaction t = session.beginTransaction();
        
        model.Ticket ticket = (model.Ticket) session.load(model.Ticket.class, vehicle.getTicket().getIdticket());
        model.Fine fine = (model.Fine) session.load(model.Fine.class, model.Fine.HIGH_SPEED_FINE);
        Set<model.Fine> fines = new ArraySet<>();
        
        if(ticket.getAmount() == null) {
            ticket.setAmount(fine.getAmount());
        } else {
            ticket.setAmount((ticket.getAmount()+fine.getAmount()));
        }
        
        fines.add(fine);
        ticket.setFines(fines);
        session.update(ticket);
        t.commit();
        return true;
    }
}
