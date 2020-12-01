package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BattleshipGame game = new BattleshipGame(scanner);
        game.start();
    }

}