package models;

public class Match implements Comparable<Match> {
    public Team team1;
    public Team team2;

    public int numberOfDaysSinceLastGame;
    public int numberOfEarlyGames;
    public int numberOfLateGames;
    public int numberOfScheduledGames;

    public IceTime nextIceTime;

    public Match(Team team1, Team team2) {
        if (!team1.league.equals(team2.league)) {
            throw new IllegalArgumentException("Teams must be in the same league to compare.");
        }
        this.team1 = team1;
        this.team2 = team2;
    }

    public String toString() {
        return """
                Team 1: %s
                Team 2: %s
                """.formatted(team1, team2);
    }

    @Override
    public int compareTo(Match other) {
        // - 1 if this is greater, 0 if equal, -1 if less
        // - 1 means this should be scheduled before other
        // - -1 means this should be scheduled after other
        // - 0 means they can be scheduled in any order

        // A team should have at least 3 days break before they play again.
        // Max distance between playing the same team (5 games).
        // A team isn't allowed to play the same opponent again until they've played all other teams in the league.
        // A team should have an equal number of early and late games.

        boolean thisHasBreakRule = this.team1.numberOfDaysSinceLastGame >= 3 && this.team2.numberOfDaysSinceLastGame >= 3;
        boolean otherHasBreakRule = other.team1.numberOfDaysSinceLastGame >= 3 && other.team2.numberOfDaysSinceLastGame >= 3;

        boolean thisHasMaxDistanceRule = this.numberOfScheduledGames <= 5;
        boolean otherHasMaxDistanceRule = other.numberOfScheduledGames <= 5;

        return 0;
    }
}
