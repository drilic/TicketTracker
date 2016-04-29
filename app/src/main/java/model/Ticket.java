package model;

/**
 * Created by gisko on 29-Apr-16.
 */
public class Ticket {
    private long id;
    private String ticketName;
    private Status status;
    private double possibleGain;

    public Ticket(){

    }

    public Ticket(String ticketName, Status status, double possibleGain){
        this.ticketName=ticketName;
        this.status=status;
        this.possibleGain = possibleGain;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getPossibleGain() {
        return possibleGain;
    }

    public void setPossibleGain(double possibleGain) {
        this.possibleGain = possibleGain;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketName='" + ticketName + '\'' +
                ", status=" + status +
                ", possibleGain=" + possibleGain +
                '}';
    }
}
