import heap.leftist_heap.LeftistHeap;
import heap.leftist_heap.MinLeftistHeap;
import models.*;

import java.time.LocalDateTime;
import java.util.*;

public class Scheduler {

    private static final int MAX_NUMBER_OF_MATCHES = 2;
    private final  List<League> leagues;
    private final List<IceTime> iceTimes;
    Map<LeftistHeap<Match>, List<Match>> heapToMatches;

    public Scheduler(List<League> leagues, List<IceTime> iceTimes) {
        this.leagues = leagues;
        this.iceTimes = iceTimes;
        this.iceTimes.sort(Comparator.comparing(iceTime -> iceTime.dateTime));
    }

    public List<Game> schedule() {
        List<LeftistHeap<Match>> leagueHeaps = new ArrayList<>();
        heapToMatches = new HashMap<>();
        for (League league : leagues) {
            List<Match> matches = new ArrayList<>();
            LeftistHeap<Match> matchHeap = new MinLeftistHeap<>();
            for (int i = 0; i < league.teams.size(); i++) {
                for (int j = 0; j < league.teams.size(); j++) {
                    if (i != j) {
                        Match match = new Match(league.teams.get(i), league.teams.get(j));
                        matchHeap.insert(match);
                        matches.add(match);
                    }
                }
            }
            heapToMatches.put(matchHeap, matches);
            leagueHeaps.add(matchHeap);
        }
        return makeGames(leagueHeaps);
    }

    private List<Game> makeGames(List<LeftistHeap<Match>> leagueHeaps) {
        List<Game> games = new ArrayList<>();
        LocalDateTime lastDay = iceTimes.getLast().dateTime;
        Queue<IceTime> iceTimeQueue = new LinkedList<>(iceTimes);

        for (LocalDateTime currentDay = iceTimes.getFirst().dateTime; currentDay.isBefore(lastDay); currentDay.plusDays(1)) {
            if (iceTimeQueue.isEmpty()) {
                throw new IllegalArgumentException("Not enough ice times for all games.");
            }
            IceTime iceTime = iceTimeQueue.peek();
            LeftistHeap<Match> heap = leagueHeaps.get(games.size() % leagues.size());
            if (iceTime.dateTime.equals(currentDay)) {
                iceTimeQueue.poll();
                games.add(makeGame(iceTime, heap));
            }
            else {
                List<Match> matches = heapToMatches.get(heap);
                for (Match match : matches) {
                    match.numberOfDaysSinceLastGame++;
                    match.team1.numberOfDaysSinceLastGame++;
                    match.team2.numberOfDaysSinceLastGame++;
                }
            }
        }
        return games;
    }

    private Game makeGame(IceTime iceTime, LeftistHeap<Match> heap) {
        List<Match> matches = heapToMatches.get(heap);
        for (Match match : matches) {
            match.nextIceTime = iceTime;
        }
        Match match = heap.delete();
        if (match == null) {
            throw new IllegalArgumentException("Not enough matches for all games.");
        }
        Team team1 = match.team1;
        Team team2 = match.team2;
        Game game = new Game(team1, team2, iceTime);

        match.numberOfScheduledGames++;
        team1.numberOfScheduledGames++;
        team2.numberOfScheduledGames++;

        match.numberOfDaysSinceLastGame = 0;
        team1.numberOfDaysSinceLastGame = 0;
        team2.numberOfDaysSinceLastGame = 0;

        if (iceTime.isEarly) {
            match.numberOfEarlyGames++;
            team1.numberOfEarlyGames++;
            team2.numberOfEarlyGames++;
        }
        else {
            match.numberOfLateGames++;
            team1.numberOfLateGames++;
            team2.numberOfLateGames++;
        }

        for (Match otherMatch : matches) {
            if (match != otherMatch) {
                otherMatch.numberOfDaysSinceLastGame++;
                otherMatch.team1.numberOfDaysSinceLastGame++;
                otherMatch.team2.numberOfDaysSinceLastGame++;
            }
        }

        if (match.numberOfScheduledGames < MAX_NUMBER_OF_MATCHES) {
            heap.insert(match);
        }

        return game;
    }
}
