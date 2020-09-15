package encryptdecrypt;

public interface CryptoMethod {

    String encrypt(String text, int key);
    String decrypt(String text, int key);
}
