/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ticket.action;

import org.hibernate.Session;

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
        model.Ticket ticket = (model.Ticket) session.load(model.Ticket.class, vehicle.getTicket().getIdticket());
        model.Fine fine = (model.Fine) session.load(model.Fine.class, model.Fine.HIGH_SPEED_FINE);
        return false;
    }
}
