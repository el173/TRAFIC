/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import model.db.User;

/**
 *
 * @author Hashith
 */
public class Tool {

    public static User loggedUser;
    public static int STATUS_ACTIVE = 1;
    public static int STATUS_DEACTIVE = 2;
    public static int TICKET_PAID = 3;
    public static int TICKET_UNPAID = 4;
    public static final int USER_TYPE_POLICE = 1;
    public static final int USER_TYPE_OFFICER = 2;
    public static final int HQ = 9;
    public static final int WARRANT = 6;

    public static org.hibernate.Session getSession() {
        return controller.Connection.getSessionFactory().openSession();
    }

    public static String getDate(Date date) {

        return new SimpleDateFormat("yyyy-MM-dd hh:MM:ss a").format(date);
    }

    public static int dateCal(String dateStart, String dateStop) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss a");
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            long diff = d2.getTime() - d1.getTime();

//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

//            System.out.print(diffDays + " days, ");
            return (int) diffDays;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
