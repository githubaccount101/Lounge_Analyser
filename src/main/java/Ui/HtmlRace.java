package Ui;

public class HtmlRace{
    int race;
    boolean satOut;
    String track;
    int players;
    int finish = 0;

    public HtmlRace(int race, String track) {
        this.race = race;
        this.track = track;
    }

    public void setTrack(String track) {
        track = track;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public void setSatOut(boolean satOut) {
        this.satOut = satOut;
    }

    public int getRace() {
        return race;
    }

    public boolean getSatout() {
        return satOut;
    }

    public String getTrack() {
        return track;
    }

    public int getPlayers() {
        return players;
    }

    public int getFinish() {
        return finish;
    }

    @Override
    public String toString() {
        return "HtmlRace{" +
                "race=" + race +
                ", satOut=" + satOut +
                ", Track='" + track + '\'' +
                ", players=" + players +
                ", finish=" + finish +
                '}';
    }
}
