/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author EL173
 */
public class Utils {
    
    public static org.hibernate.Session getSession() {
        return controller.Connection.getSessionFactory().openSession();
    }
}
