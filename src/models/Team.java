package models;

public class Team implements Comparable<Team> {
    public int id;
    public String name;
    public String league;

    public Team(int id, String name, String league) {
        this.id = id;
        this.name = name;
        this.league = league;
    }

    public String toString() {
        return """
                ID: %d
                Name: %s
                League: %s
                """.formatted(id, name, league);
    }

    public int compareTo(Team other) {
        // Max distance between playing the same team (5 games).
        // A team should have at least 3 days break before they play again.
        // A team isn't allowed to play the same opponent again until they've played all other teams in the league.
        // A team should have an equal number of early and late games.

        // TODO Implement compareTo method

    }

}
