import java.util.HashMap;
import java.util.Map;

class NbaTeamMinutes {
    private Map<Integer, Integer> ageMap = new HashMap<>();

    void addAgeMinutes(int age, int minutesPlayed) {
        int currentMinutes = ageMap.getOrDefault(age, 0);

        ageMap.put(age, currentMinutes + minutesPlayed);
    }

    double calculateAgeMinutes() {
        double ageMinutes = ageMap.entrySet().stream().mapToDouble(e -> e.getKey() * e.getValue()).sum();
        double totalMinutes = ageMap.values().stream().mapToInt(i -> i).sum();

        return ageMinutes / totalMinutes;
    }
}