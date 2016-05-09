package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by gisko on 29-Apr-16.
 */

@Table(name = "Ticket", id="_id")
public class Ticket extends Model {

    @Column(name = "ticketName", notNull = true)
    public String ticketName;

    @Column(name = "possibleGain")
    public double possibleGain;

    @Column(name = "status", notNull = true, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Status status;

    public List<Match> matches() {
        return getMany(Match.class, "ticket");
    }

    public Ticket() {
        super();
    }

    public Ticket(String ticketName, Status status, double possibleGain) {
        super();
        this.ticketName = ticketName;
        this.status = status;
        this.possibleGain = possibleGain;
    }

    public Ticket(String ticketName, Status status) {
        super();
        this.ticketName = ticketName;
        this.status = status;
    }

    public static List<Ticket> getAll() {
        return new Select()
                .from(Ticket.class)
                .orderBy("_id DESC")
                .execute();
    }

    public static List<Ticket> getAllActive() {
        Status status = new Select()
                .from(Status.class)
                .where("status = ?", "Active")
                .executeSingle();

        return new Select()
                .from(Ticket.class)
                .where("status= ?", status.getId())
                .orderBy("_id DESC")
                .execute();
    }

    public static List<Ticket> getAllWin() {
        Status status = new Select()
                .from(Status.class)
                .where("status = ?", "Win")
                .executeSingle();

        return new Select()
                .from(Ticket.class)
                .where("status= ?", status.getId())
                .orderBy("_id DESC")
                .execute();
    }

    public static List<Ticket> getAllLose() {
        Status status = new Select()
                .from(Status.class)
                .where("status = ?", "Lose")
                .executeSingle();

        return new Select()
                .from(Ticket.class)
                .where("status= ?", status.getId())
                .orderBy("_id DESC")
                .execute();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                ", ticketName='" + ticketName + '\'' +
                ", status=" + status.status +
                ", possibleGain=" + possibleGain +
                '}';
    }
}
