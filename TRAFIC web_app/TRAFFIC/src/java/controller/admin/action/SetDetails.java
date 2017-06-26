/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin.action;

import controller.Tool;
import model.db.Fine;
import model.db.PoliceStation;
import model.db.Status;
import model.db.User;
import model.db.UserType;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
    
    public void setPolice(String policeName,String userName,String password){
        model.db.User user = new User();
        user.setPassword(password);
        user.setUsername(userName);
        user.setStatus((Status) session.load(model.db.Status.class, Tool.STATUS_ACTIVE));
        user.setUserType((UserType) session.load(model.db.UserType.class, Tool.USER_TYPE_POLICE));
        session.save(user);
        
        model.db.PoliceStation policeStation = new PoliceStation();
        policeStation.setStationName(policeName);
        policeStation.setUser(user);
        session.save(policeStation);
        
        transaction.commit();
    }
    
    public void setFine(String description, Double amount){
        model.db.Fine fine = new Fine();
        fine.setDescription(description);
        fine.setFine(amount);
        session.save(fine);
        
        transaction.commit();
    }
}
