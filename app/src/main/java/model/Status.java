package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by gisko on 29-Apr-16.
 */

@Table(name = "Status")
public class Status extends Model{

    @Column(name = "status", notNull = true)
    public String status;

    public List<Match> matches() {
        return getMany(Match.class, "status");
    }

    public List<Ticket> tickets() {
        return getMany(Ticket.class, "status");
    }

    public Status() {
        super();
    }

    public Status(String status) {
        super();
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                ", status='" + status + '\'' +
                '}';
    }
}
