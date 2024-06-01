package models;

public class Game {
    public Team team1;
    public Team team2;
    public IceTime iceTime;

    public Game(Team team1, Team team2, IceTime iceTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.iceTime = iceTime;
    }

    public String toString() {
        return """
                Team 1: %s
                Team 2: %s
                Ice Time: %s
                """.formatted(team1.name, team2.name, iceTime.dateTime);
    }
}
