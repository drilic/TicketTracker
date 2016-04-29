package model;

/**
 * Created by gisko on 29-Apr-16.
 */
public class Status {
    private long id;
    private String status;

    public Status(){

    }

    public Status(String status){
        this.status=status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
