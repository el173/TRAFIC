package model;
// Generated Mar 18, 2018 7:17:56 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Ticket generated by hbm2java
 */
public class Ticket  implements java.io.Serializable {


     private Integer idticket;
     private EntranceExit entranceExitByEnd;
     private EntranceExit entranceExitByStart;
     private Vehicle vehicle;
     private Double amount;
     private Set fines = new HashSet(0);

    public Ticket() {
    }

	
    public Ticket(EntranceExit entranceExitByEnd, EntranceExit entranceExitByStart, Vehicle vehicle) {
        this.entranceExitByEnd = entranceExitByEnd;
        this.entranceExitByStart = entranceExitByStart;
        this.vehicle = vehicle;
    }
    public Ticket(EntranceExit entranceExitByEnd, EntranceExit entranceExitByStart, Vehicle vehicle, Double amount, Set fines) {
       this.entranceExitByEnd = entranceExitByEnd;
       this.entranceExitByStart = entranceExitByStart;
       this.vehicle = vehicle;
       this.amount = amount;
       this.fines = fines;
    }
   
    public Integer getIdticket() {
        return this.idticket;
    }
    
    public void setIdticket(Integer idticket) {
        this.idticket = idticket;
    }
    public EntranceExit getEntranceExitByEnd() {
        return this.entranceExitByEnd;
    }
    
    public void setEntranceExitByEnd(EntranceExit entranceExitByEnd) {
        this.entranceExitByEnd = entranceExitByEnd;
    }
    public EntranceExit getEntranceExitByStart() {
        return this.entranceExitByStart;
    }
    
    public void setEntranceExitByStart(EntranceExit entranceExitByStart) {
        this.entranceExitByStart = entranceExitByStart;
    }
    public Vehicle getVehicle() {
        return this.vehicle;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Double getAmount() {
        return this.amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Set getFines() {
        return this.fines;
    }
    
    public void setFines(Set fines) {
        this.fines = fines;
    }




}

