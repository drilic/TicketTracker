package model;

/**
 * Created by gisko on 29-Apr-16.
 */
public class Bet {
    private long id;
    private String betName;

    public Bet(){

    }

    public Bet(String betName){
        this.betName = betName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "id=" + id +
                ", betName='" + betName + '\'' +
                '}';
    }
}
