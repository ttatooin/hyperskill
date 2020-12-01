package bullscows;

public class GradeAnswer {

    public final int bulls;
    public final int cows;
    public final boolean isGuessed;

    public GradeAnswer(int bulls, int cows, boolean isGuessed) {
        this.bulls = bulls;
        this.cows = cows;
        this.isGuessed = isGuessed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Grade: ");
        if (bulls == 0 && cows == 0) {
            builder.append("None");
        }
        if (bulls > 0) {
            builder.append(bulls);
            builder.append(" bull");
            if (bulls > 1) {
                builder.append("s");
            }
        }
        if (bulls > 0 && cows > 0) {
            builder.append(" and ");
        }
        if (cows > 0) {
            builder.append(cows);
            builder.append(" cow");
            if (cows > 1) {
                builder.append("s");
            }
        }
        return builder.toString();
    }

}
