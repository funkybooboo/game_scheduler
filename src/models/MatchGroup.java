package models;

import heap.leftist_heap.LeftistHeap;

public class MatchGroup implements Comparable<MatchGroup> {
    public Team team;
    // Heap matches between this team and the other team
    public LeftistHeap<Match> matchHeap;

    public int numberOfScheduledGames;

    public MatchGroup(Team team, LeftistHeap<Match> matchHeap) {
        this.team = team;
        this.matchHeap = matchHeap;
    }

    public String toString() {
        return """
                Team: %s
                Matches: %s
                """.formatted(team, matchHeap);
    }

    @Override
    public int compareTo(MatchGroup other) {
        // A team isn't allowed to play the same opponent again until they've played all other teams in the league.
        return this.numberOfScheduledGames - other.numberOfScheduledGames;
    }
}
