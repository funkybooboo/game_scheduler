import heap.leftist_heap.LeftistHeap;
import heap.leftist_heap.MinLeftistHeap;
import models.Game;
import models.IceTime;
import models.League;
import models.Team;
import java.time.LocalDateTime;
import java.util.*;

public class Scheduler {

    private static final int MAX_NUMBER_OF_GAMES = 10;
    private final  List<League> leagues;
    private final List<IceTime> iceTimes;

    public Scheduler(List<League> leagues, List<IceTime> iceTimes) {
        this.leagues = leagues;
        this.iceTimes = iceTimes;
        this.iceTimes.sort(Comparator.comparing(iceTime -> iceTime.dateTime));
    }

    public List<Game> schedule() {
        List<LeftistHeap<Team>> leagueHeaps = new ArrayList<>();
        Map<LeftistHeap<Team>, League> heapLeagueMap = new HashMap<>();
        for (League league : leagues) {
            LeftistHeap<Team> heap = new MinLeftistHeap<>();
            heapLeagueMap.put(heap, league);
            for (Team team : league.teams) {
                heap.insert(team);
            }
            leagueHeaps.add(heap);
        }
        return makeGames(leagueHeaps, heapLeagueMap);
    }

    private List<Game> makeGames(List<LeftistHeap<Team>> leagueHeaps, Map<LeftistHeap<Team>, League> heapLeagueMap) {
        int gameCount = 0;
        List<Game> games = new ArrayList<>();
        LocalDateTime lastDay = iceTimes.getLast().dateTime;
        Queue<IceTime> iceTimeQueue = new LinkedList<>(iceTimes);

        for (LocalDateTime currentDay = iceTimes.getFirst().dateTime; currentDay.isBefore(lastDay); currentDay.plusDays(1)) {
            if (iceTimeQueue.isEmpty()) {
                throw new IllegalArgumentException("Not enough ice times for all games.");
            }
            IceTime iceTime = iceTimeQueue.peek();
            LeftistHeap<Team> heap = leagueHeaps.get(gameCount % leagues.size());
            if (iceTime.dateTime.equals(currentDay)) {
                iceTimeQueue.poll();
                makeGame(iceTime, heap, games, heapLeagueMap);
                gameCount++;
            }
            else {
                League league = heapLeagueMap.get(heap);
                for (Team team : league.teams) {
                    team.numberOfDaysSinceLastGame++;
                }
            }
        }
        return games;
    }

    private void makeGame(IceTime iceTime, LeftistHeap<Team> heap, List<Game> games, Map<LeftistHeap<Team>, League> heapLeagueMap) {
        Team team1 = heap.delete();
        Team team2 = heap.delete();
        if (team1 != null && team2 != null) {
            Game game = new Game(team1, team2, iceTime);
            games.add(game);

            team1.numberOfScheduledGames++;
            team2.numberOfScheduledGames++;
            team1.numberOfDaysSinceLastGame = 0;
            team2.numberOfDaysSinceLastGame = 0;
            team1.gamesPlayedAgainst.put(team2.id, team1.gamesPlayedAgainst.getOrDefault(team2.id, 0) + 1);
            team2.gamesPlayedAgainst.put(team1.id, team2.gamesPlayedAgainst.getOrDefault(team1.id, 0) + 1);

            if (iceTime.isEarly) {
                team1.numberOfEarlyGames++;
                team2.numberOfEarlyGames++;
            }
            else {
                team1.numberOfLateGames++;
                team2.numberOfLateGames++;
            }

            League league = heapLeagueMap.get(heap);

            for (Team team : league.teams) {
                if (team != team1 && team != team2) {
                    team.numberOfDaysSinceLastGame++;
                }
            }
        }
        if (team1 != null && team1.numberOfScheduledGames < MAX_NUMBER_OF_GAMES) {
            heap.insert(team1);
        }
        if (team2 != null && team2.numberOfScheduledGames < MAX_NUMBER_OF_GAMES) {
            heap.insert(team2);
        }
    }
}
