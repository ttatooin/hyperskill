package encryptdecrypt;

public class Shift implements CryptoMethod {

    @Override
    public String encrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); ++i) {

            if (text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                builder.append((char)('a' + (text.charAt(i) - 'a' + key) % 26));
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {
                builder.append((char)('A' + (text.charAt(i) - 'A' + key) % 26));
            } else {
                builder.append(text.charAt(i));
            }
        }

        return builder.toString();
    }

    @Override
    public String decrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); ++i) {

            if (text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                builder.append((char)('a' + Math.floorMod(text.charAt(i) - 'a' - key, 26)));
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {
                builder.append((char)('A' + Math.floorMod(text.charAt(i) - 'A' - key, 26)));
            } else {
                builder.append(text.charAt(i));
            }
        }

        return builder.toString();
    }
}
