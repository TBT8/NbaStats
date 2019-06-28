import java.util.Objects;

class NbaTeamKey {
    private final int year;
    private final String teamName;

    NbaTeamKey(int year, String teamName) {
        this.year = year;
        this.teamName = Objects.requireNonNull(teamName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NbaTeamKey that = (NbaTeamKey) o;
        return year == that.year &&
                teamName.equals(that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, teamName);
    }

    @Override
    public String toString() {
        return "" + teamName + " - " + year;
    }
}
