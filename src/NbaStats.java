import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NbaStats {

    // Data can be found here: https://www.kaggle.com/drgilermo/nba-players-stats

    private static String PATH = "Seasons_Stats.csv";

    public static void main(String[] args) {
        calculateMinutesAdjustedAge();
        calculateMostCareer3PM();
        calculateMostBlocksInOneSeason();

    }

    private static void calculateMostBlocksInOneSeason(){

        String player = "";
        int blocks = 0;

       List<String[]> rows = getSplitLines()
                .filter(NbaStats::isNonNullRow)
                .filter(arr -> !arr[49].equals("") && !arr[49].equals("BLK"))
                .collect(Collectors.toList());

       for(String[] arr : rows){
           int blks = Integer.parseInt(arr[49]);
           if(blks > blocks){
               blocks = blks;
               player = arr[2];
           }
       }

       System.out.println("The player with the most blocks in a single season is " + player + " with " + blocks + " blocks");
    }

    private static void calculateMostCareer3PM() {

        Map<String, Integer> playerThreePts = new HashMap<>();

        getSplitLines()
                .filter(NbaStats::isNonNullRow)
                .filter(arr -> !arr[34].equals("") && !arr[34].equals("3P")  )
                .forEach(
                        arr -> {
                            Integer made = playerThreePts.getOrDefault(arr[2], 0);
                            playerThreePts.put(arr[2], made + Integer.parseInt(arr[34]));
                        }
                );



        int maxThrees = 0;
        String maxPlayer = "";
        for(Map.Entry<String, Integer> entry : playerThreePts.entrySet()){
            if(entry.getValue() > maxThrees){
                maxThrees = entry.getValue();
                maxPlayer = entry.getKey();
            }
        }

        System.out.println("Player with the most career three pointers made is " + maxPlayer + " with " + maxThrees + " threes made");
    }


    private static void calculateMinutesAdjustedAge() {
        Map<NbaTeamKey, NbaTeamMinutes> minutesMap = new HashMap<>();

            getSplitLines()
                    .filter(NbaStats::isNonNullRow)
                    .filter(NbaStats::hasNeededFields)
                    .filter(NbaStats::isYearWithData)
                    .forEach(arr -> {
                        NbaTeamKey key = new NbaTeamKey(Integer.parseInt(arr[1]), arr[5]);
                        NbaTeamMinutes teamMinutes = minutesMap.getOrDefault(key, new NbaTeamMinutes());
                        teamMinutes.addAgeMinutes(Integer.parseInt(arr[4]), Integer.parseInt(arr[8]));
                        minutesMap.put(key, teamMinutes);
                    });

        double maxAge = 0.0d;
        NbaTeamKey key = new NbaTeamKey(0, "");

        for (Map.Entry<NbaTeamKey, NbaTeamMinutes> entry : minutesMap.entrySet()) {
            double ageMinutes = entry.getValue().calculateAgeMinutes();

            if (ageMinutes > maxAge) {
                maxAge = ageMinutes;
                key = entry.getKey();
            }

        }

        System.out.println("The oldest minutes adjusted team in history is: " + key + " with a minutes adjusted age of " + maxAge);
    }

    private static boolean isYearWithData(String[] array) {
        return !(array[1].equals("1950") || array[1].equals("1951"));
    }

    private static boolean isNonNullRow(String[] array) {
        return array.length > 2;
    }

    private static boolean hasNeededFields(String[] array) {
        return !(array[0].equals("") || array[4].equals("") || array[8].equals(""));
    }

    private static Stream<String[]> getSplitLines() {
        try {
            return Files.lines(Paths.get(PATH))
                    .map(l -> l.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Stream.empty();
    }


}

