package models;

public class Match implements Comparable<Match> {
    public Team team1;
    public Team team2;

    public int numberOfDaysSinceLastGame;
    public int numberOfEarlyGames;
    public int numberOfLateGames;
    public int numberOfScheduledGames;

    public IceTime nextIceTime;

    public Match(Team team1, Team team2, IceTime nextIceTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.nextIceTime = nextIceTime;
    }

    public String toString() {
        return """
                Team 1: %s
                Team 2: %s
                """.formatted(team1, team2);
    }

    @Override
    public int compareTo(Match other) {
        // A team should have at least 3 days break before they play again.
        // Max distance between playing the same team (5 games).
        // A team should have an equal number of early and late games.

        boolean thisHasBreakRule = this.team1.numberOfDaysSinceLastGame >= 3 && this.team2.numberOfDaysSinceLastGame >= 3;
        boolean otherHasBreakRule = other.team1.numberOfDaysSinceLastGame >= 3 && other.team2.numberOfDaysSinceLastGame >= 3;

        boolean thisHasMaxDistanceRule = this.numberOfScheduledGames <= 5;
        boolean otherHasMaxDistanceRule = other.numberOfScheduledGames <= 5;

        boolean thisCouldUseMoreEarlyGames = this.numberOfEarlyGames < this.numberOfLateGames;
        boolean otherCouldUseMoreEarlyGames = other.numberOfEarlyGames < other.numberOfLateGames;

        boolean thisCouldUseNextIceTime = this.nextIceTime.isEarly && thisCouldUseMoreEarlyGames;
        boolean otherCouldUseNextIceTime = other.nextIceTime.isEarly && otherCouldUseMoreEarlyGames;

        if (thisHasBreakRule && otherHasBreakRule) {
            if (thisHasMaxDistanceRule && otherHasMaxDistanceRule) {
                if (thisCouldUseNextIceTime && otherCouldUseNextIceTime) {
                    return 0;
                }
                else if (thisCouldUseNextIceTime) {
                    return -1;
                }
                else if (otherCouldUseNextIceTime) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else if (thisHasMaxDistanceRule) {
                return -1;
            }
            else if (otherHasMaxDistanceRule) {
                return 1;
            }
            else {
                return 0;
            }
        }
        else if (thisHasBreakRule) {
            return -1;
        }
        else if (otherHasBreakRule) {
            return 1;
        }
        else {
            return 0;
        }
    }

}
