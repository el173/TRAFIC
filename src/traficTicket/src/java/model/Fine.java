package model;
// Generated Mar 18, 2018 7:17:56 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Fine generated by hbm2java
 */
public class Fine  implements java.io.Serializable {


     private Integer idfine;
     private String fine;
     private Double amount;
     private Set tickets = new HashSet(0);

    public Fine() {
    }

    public Fine(String fine, Double amount, Set tickets) {
       this.fine = fine;
       this.amount = amount;
       this.tickets = tickets;
    }
   
    public Integer getIdfine() {
        return this.idfine;
    }
    
    public void setIdfine(Integer idfine) {
        this.idfine = idfine;
    }
    public String getFine() {
        return this.fine;
    }
    
    public void setFine(String fine) {
        this.fine = fine;
    }
    public Double getAmount() {
        return this.amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Set getTickets() {
        return this.tickets;
    }
    
    public void setTickets(Set tickets) {
        this.tickets = tickets;
    }




}


