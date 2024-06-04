package models;

public class Team {
    public int id;
    public String name;

    public int numberOfEarlyGames;
    public int numberOfLateGames;
    public int numberOfScheduledGames;
    public int numberOfDaysSinceLastGame;

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return """
                ID: %d
                Name: %s
                """.formatted(id, name);
    }
}
