package tw.edu.fju.miniclinic.model;

import java.util.Map;

/**
 * 統計摘要回傳物件
 */
public class StatsResponse {
    private int totalDoctors;
    private int totalPatients;
    private int totalAppointments;
    private Map<String, Integer> byStatus;

    public StatsResponse() {}

    public StatsResponse(int totalDoctors, int totalPatients, int totalAppointments, 
                         Map<String, Integer> byStatus) {
        this.totalDoctors = totalDoctors;
        this.totalPatients = totalPatients;
        this.totalAppointments = totalAppointments;
        this.byStatus = byStatus;
    }

    public int getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(int totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Map<String, Integer> getByStatus() {
        return byStatus;
    }

    public void setByStatus(Map<String, Integer> byStatus) {
        this.byStatus = byStatus;
    }
}
