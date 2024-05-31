import heap.leftist_heap.LeftistHeap;
import heap.leftist_heap.MinLeftistHeap;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Scheduler {

    public static Map<String, Object> readYamlFile(String yamlFile) throws Exception {
        try {
            // Create a YAML object
            Yaml yaml = new Yaml();

            // Load YAML file into a Map
            FileInputStream inputStream = new FileInputStream(yamlFile);
            return yaml.load(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new Exception("didnt get data");
    }

    public static List<Team> getTeams(Object leagues) {
        List<Team> teams = new ArrayList<>();

        // Check if leagues is a list
        if (leagues instanceof List) {
            // Iterate over each league
            for (Object leagueObj : (List<?>) leagues) {
                if (leagueObj instanceof Map<?, ?> leagueMap) {
                    // Check if leagueMap contains "league" and "teams" keys
                    if (leagueMap.containsKey("league") && leagueMap.containsKey("teams")) {
                        Object leagueNameObj = leagueMap.get("league");
                        Object teamsObj = leagueMap.get("teams");
                        // Check if leagueNameObj is a string and teamsObj is a list
                        if (leagueNameObj instanceof String leagueName && teamsObj instanceof List) {
                            // Iterate over teams in the league
                            for (Object teamObj : (List<?>) teamsObj) {
                                if (teamObj instanceof Map<?, ?> teamMap) {
                                    // Check if teamMap contains "name" and "id" keys
                                    if (teamMap.containsKey("name") && teamMap.containsKey("id")) {
                                        Object nameObj = teamMap.get("name");
                                        Object idObj = teamMap.get("id");
                                        // Check if nameObj is a string and idObj is an integer
                                        if (nameObj instanceof String name && idObj instanceof Integer id) {
                                            // Create Team object and add to list
                                            teams.add(new Team(id, name, leagueName));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return teams;
    }

    public static List<IceTime> getIceTimes(Object iceTimes) {
        List<IceTime> iceTimesList = new ArrayList<>();
        if (iceTimes instanceof List<?> iceTimeObjects) {
            for (Object iceTimeObj : iceTimeObjects) {
                if (iceTimeObj instanceof String iceTimeString) {
                    IceTime iceTime = new IceTime(iceTimeString);
                    iceTimesList.add(iceTime);
                }
            }
        }
        return iceTimesList;
    }

    public static List<Game> getGames(String title, List<Team> Teams, List<IceTime> iceTimes) {
        System.out.println();
        System.out.println(title);
        iceTimes.sort(Comparator.comparing(iceTime -> iceTime.dateTime));
        LeftistHeap<Team> queue = new MinLeftistHeap<>();

        for (Team team : Teams) {
            queue.insert(team);
        }

        List<Game> games = new ArrayList<>();

        int totalNumberOfGamesPerTeam = 10;

        for (IceTime iceTime : iceTimes) {
            System.out.print(iceTime + " : ");
            for (Team team : Teams) {
                team.numberOfDaysSinceLastGame++;
            }
            while (!queue.isEmpty()) {
                Team team1 = queue.delete();
                Team team2 = queue.delete();
                games.add(new Game(team1, team2, iceTime));
                System.out.print(team1.name + " vs " + team2.name + ", ");
                if (iceTime.isEarly) {
                    team1.numberOfEarlyGames++;
                    team2.numberOfEarlyGames++;
                }
                team1.numberOfGames++;
                team2.numberOfGames++;
                team1.numberOfDaysSinceLastGame = 0;
                team2.numberOfDaysSinceLastGame = 0;
                team1.numberOfGamesPlayedPerTeam.put(team2.name, team1.numberOfGamesPlayedPerTeam.get(team2.name) + 1);
                team2.numberOfGamesPlayedPerTeam.put(team1.name, team2.numberOfGamesPlayedPerTeam.get(team1.name) + 1);
                if (team1.numberOfGames < totalNumberOfGamesPerTeam) {
                    queue.insert(team1);
                }
                if (team2.numberOfGames < totalNumberOfGamesPerTeam) {
                    queue.insert(team2);
                }

            }
            System.out.println();
        }
        return games;
    }

    public static void main(String args[]) throws Exception {
        // "data/spring_2024_config.yml", "data/summer_2024_config.yml"
        String [] filenames = {"data/fall_2023_config.yml"};
        for (String filename : filenames) {
            Map<String, Object> yamlMap = readYamlFile(filename);
            List<Team> teams = getTeams(yamlMap.get("leagues"));
            List<IceTime> iceTimes = getIceTimes(yamlMap.get("ice_times"));
            List<Game> games = getGames("Priority "+ filename, teams, iceTimes);
            System.out.println("Games: " + games.size());
        }
    }
}
