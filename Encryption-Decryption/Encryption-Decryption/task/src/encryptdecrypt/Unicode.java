package encryptdecrypt;

public class Unicode implements  CryptoMethod {

    @Override
    public String encrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); ++i) {
            builder.append((char) (text.charAt(i) + key));
        }
        return builder.toString();
    }

    @Override
    public String decrypt(String text, int key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); ++i) {
            builder.append((char) (text.charAt(i) - key));
        }

        return builder.toString();
    }
}
