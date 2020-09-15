package machine;

import java.util.Scanner;

public class CoffeeMachine {

    static final int[][] RECEIPTS = {{250, 0, 16, 4}, {350, 75, 20, 7}, {200, 100, 12, 6}};

    CoffeeMachineState state = CoffeeMachineState.WAITING_FOR_ACTION;

    int waterAmount = 400;
    int milkAmount = 540;
    int coffeeBeansAmount = 120;
    int cupsAmount = 9;
    int moneyAmount = 550;

    public void action(String command) {
        switch (state) {

            case WAITING_FOR_ACTION:
                switch (command) {
                    case "buy":
                        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino");
                        state = CoffeeMachineState.COFFEE_CHOOSE;
                        return;
                    case "fill":
                        System.out.println("Write how many ml of water do you want to add:");
                        state = CoffeeMachineState.FILLING_WATER;
                        return;
                    case "take":
                        System.out.println("I gave you $" + moneyAmount);
                        moneyAmount = 0;
                        return;
                    case "remaining":
                        getStatus();
                        return;
                    case "exit":
                        state = CoffeeMachineState.TERMINATING;
                        return;
                    default:
                        return;
                }

            case COFFEE_CHOOSE:
                switch (command) {
                    case "back":
                        state = CoffeeMachineState.WAITING_FOR_ACTION;
                        System.out.println("Write action (buy, fill, take, remaining, exit):");
                        return;
                    case "1":
                    case "2":
                    case "3":
                        sellItem(Integer.parseInt(command));
                        state = CoffeeMachineState.WAITING_FOR_ACTION;
                        System.out.println("Write action (buy, fill, take, remaining, exit):");
                        return;
                    default:
                        return;
                }

            case FILLING_WATER:
                waterAmount += Integer.parseInt(command);
                System.out.println("Write how many ml of milk do you want to add:");
                state = CoffeeMachineState.FILLING_MILK;
                return;
            case FILLING_MILK:
                milkAmount += Integer.parseInt(command);
                System.out.println("Write how many grams of coffee beans do you want to add:");
                state = CoffeeMachineState.FILLING_COFFEE_BEANS;
                return;
            case FILLING_COFFEE_BEANS:
                coffeeBeansAmount += Integer.parseInt(command);
                System.out.println("Write how many disposable cups do you want to add:");
                state = CoffeeMachineState.FILLING_DISPOSABLE_CUPS;
                return;
            case FILLING_DISPOSABLE_CUPS:
                cupsAmount += Integer.parseInt(command);
                state = CoffeeMachineState.WAITING_FOR_ACTION;
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                return;
            default:
                return;
        }
    }

    public void getStatus() {
        System.out.println("The coffee machine has:");
        System.out.println(waterAmount + " of water");
        System.out.println(milkAmount + " of milk");
        System.out.println(coffeeBeansAmount + " of coffee beans");
        System.out.println(cupsAmount + " of disposable cups");
        System.out.println(moneyAmount + " of money");
    }

    public void sellItem(int item) {
        if (waterAmount < RECEIPTS[item - 1][0]) {
            System.out.println("Sorry, not enough water!");
        } else if (milkAmount < RECEIPTS[item - 1][1]) {
            System.out.println(("Soory, not enough milk!"));
        } else if (coffeeBeansAmount < RECEIPTS[item - 1][2]) {
            System.out.println(("Soory, not enough coffee beans!"));
        } else if (cupsAmount == 0) {
            System.out.println(("Soory, not enough disposable cups!"));
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            waterAmount -= RECEIPTS[item - 1][0];
            milkAmount -= RECEIPTS[item - 1][1];
            coffeeBeansAmount -= RECEIPTS[item - 1][2];
            moneyAmount += RECEIPTS[item - 1][3];
            cupsAmount -= 1;
        }
    }

    public CoffeeMachine() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    public static void main(String[] args) {

        CoffeeMachine coffeeMachine = new CoffeeMachine();

        Scanner scanner = new Scanner(System.in);

        while (coffeeMachine.state != CoffeeMachineState.TERMINATING) {
            coffeeMachine.action(scanner.next());
        }
    }
}

enum CoffeeMachineState {
    WAITING_FOR_ACTION,
    COFFEE_CHOOSE,
    FILLING_WATER,
    FILLING_MILK,
    FILLING_COFFEE_BEANS,
    FILLING_DISPOSABLE_CUPS,
    TERMINATING;
}
