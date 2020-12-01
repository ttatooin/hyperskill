package bullscows;

public class Grader {

    private final Character[] secretCode;

    public Grader (Character[] secretCode) {
        this.secretCode = secretCode;
    }

    public GradeAnswer grade(String code) {
        int cows = 0;
        int bulls = 0;
        char[] elements = code.toCharArray();
        for (int k = 0; k < elements.length; ++k) {
            switch (gradeDigit(elements[k], k)) {
                case 1:
                    ++cows;
                    break;
                case 2:
                    ++bulls;
                    break;
            }
        }
        return new GradeAnswer(bulls, cows, bulls == secretCode.length);
    }

    // 0 - none, 1 - cow, 2 - bull
    private int gradeDigit(char symbol, int place) {
        int result = 0;
        for (int k = 0; k < secretCode.length; ++k) {
            if (symbol == secretCode[k]) {
                if (place == k) {
                    result = 2;
                    break;
                } else {
                    result = 1;
                    continue;
                }
            }
        }
        return result;
    }

    private static int[] getDigits(int number) {
        return getDigits(Integer.toString(number));
    }

    private static int[] getDigits(String number) {
        int[] result = new int[number.length()];
        for (int k = 0; k < number.length(); ++k) {
            result[k] = Integer.parseInt(number.substring(k, k + 1));
        }
        return result;
    }

}
