package net.augustana.puffinradio;

public class HighScore implements Comparable<HighScore> {
    private String key;
    private String callSign;
    private int score;

    public HighScore(String key, String callSign, int score) {
        this.key = key;
        this.callSign = callSign;
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public String getCallSign() {
        return callSign;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(HighScore other) {
        return other.score - score;
    }
}
