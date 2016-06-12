package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Bet class, for betting types.
 */
@Table(name = "Bet", id = "_id")
public class Bet extends Model {

    @Column(name = "betName", notNull = true)
    public String betName;

    public List<Match> matches() {
        return getMany(Match.class, "bet");
    }

    public Bet() {
        super();
    }

    public Bet(String betName) {
        super();
        this.betName = betName;
    }

    @Override
    public String toString() {
        return betName;
    }
}
