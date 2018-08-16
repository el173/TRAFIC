package model.db;
// Generated 21-May-2017 23:55:06 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * WorkingHistory generated by hbm2java
 */
public class WorkingHistory  implements java.io.Serializable {


     private Integer idworkingHistory;
     private PoliceStation policeStation;
     private TrafficOfficer trafficOfficer;
     private Date startDate;
     private Date endDate;

    public WorkingHistory() {
    }

	
    public WorkingHistory(PoliceStation policeStation, TrafficOfficer trafficOfficer) {
        this.policeStation = policeStation;
        this.trafficOfficer = trafficOfficer;
    }
    public WorkingHistory(PoliceStation policeStation, TrafficOfficer trafficOfficer, Date startDate, Date endDate) {
       this.policeStation = policeStation;
       this.trafficOfficer = trafficOfficer;
       this.startDate = startDate;
       this.endDate = endDate;
    }
   
    public Integer getIdworkingHistory() {
        return this.idworkingHistory;
    }
    
    public void setIdworkingHistory(Integer idworkingHistory) {
        this.idworkingHistory = idworkingHistory;
    }
    public PoliceStation getPoliceStation() {
        return this.policeStation;
    }
    
    public void setPoliceStation(PoliceStation policeStation) {
        this.policeStation = policeStation;
    }
    public TrafficOfficer getTrafficOfficer() {
        return this.trafficOfficer;
    }
    
    public void setTrafficOfficer(TrafficOfficer trafficOfficer) {
        this.trafficOfficer = trafficOfficer;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }




}

