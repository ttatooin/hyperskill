package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static final String STR_INVALID_ASSIGNMENT = "Invalid assignment";
    public static final String STR_INVALID_IDENTIFIER = "Invalid identifier";
    public static final String STR_INVALID_EXPRESSION = "Invalid expression";
    public static final String STR_UNKNOWN_VARIABLE = "Unknown variable";

    public static final String OP_ADDITION = "+";
    public static final String OP_DIFFERENCE = "-";
    public static final String OP_MULTIPLICATION = "*";
    public static final String OP_DIVISION = "/";
    public static final String OP_POWER = "^";

    private Map<String, BigInteger> variables;

    public Calculator() {
        variables = new HashMap<>();
    }

    public String process(String input) {
        String error = checkForCorrectness(input);
        if (error.equals("Correct")) {
            if (input.contains("=")) {
                String variableName = input.split("=")[0].trim();
                String variableValue = input.split("=")[1].trim();
                variables.put(variableName, new BigInteger(processArithmeticStatement(variableValue)));
                return null;
            } else {
                return processArithmeticStatement(input);
            }
        } else {
            return error;
        }
    }

    private String normalize(String input) {
        String result = input.replaceAll("\\s+", "");
        while (result.matches(".*[\\+\\-]{2,}.*")) {
            result = result.replaceAll("\\+\\+|\\-\\-", "+");
            result = result.replaceAll("\\+\\-|\\-\\+", "-");
        }
        return result;
    }

    private String checkForCorrectness(String input) {
        input = normalize(input);
        if (input.matches("[^\\p{Alpha}\\d\\(\\)\\+\\-\\*\\/\\^]")) {
            return STR_INVALID_EXPRESSION;
        }
        if (input.contains("=")) {
            if (input.matches(".*=.*=.*")) {
                return STR_INVALID_ASSIGNMENT;
            } else if (!input.split("=")[0].matches("\\p{Alpha}+")) {
                return STR_INVALID_IDENTIFIER;
            } else {
                input = input.split("=")[1];
            }
        }
        if (input.matches("^[\\*\\/\\^]|[\\+\\-\\*\\/\\^]$]")) {
            return STR_INVALID_EXPRESSION;
        }
        if (input.matches(".*[\\+\\-\\*\\/\\^]{2,}.*") ||
            input.matches(".*(\\d\\p{Alpha}|\\p{Alpha}\\d).*") ||
            input.matches(".*(\\(\\)|\\)\\().*") ||
            input.matches(".*([\\d\\p{Alpha}]\\(|\\)[\\d\\p{Alpha}]).*") ||
            input.matches(".*(\\([\\*\\/\\^]|[\\+\\-\\*\\/\\^]\\)).*")) {
            return STR_INVALID_EXPRESSION;
        }
        int brackets = 0;
        for (int k = 0; k < input.length(); ++k) {
            if (input.charAt(k) == '(') {
                brackets += 1;
            }
            if (input.charAt(k) == ')') {
                brackets -= 1;
            }
            if (brackets < 0) {
                return STR_INVALID_EXPRESSION;
            }
        }
        if (brackets > 0) {
            return STR_INVALID_EXPRESSION;
        }
        Matcher matcher = Pattern.compile("\\p{Alpha}+").matcher(input);
        while (matcher.find()) {
            if (!variables.containsKey(matcher.group())) {
                return STR_UNKNOWN_VARIABLE + matcher.group();
            }
        }
        return "Correct";
    }

    private String processArithmeticStatement(String input) {
        LinkedList<String> tokenList = StringToTokenList(normalize(input));
        LinkedList<String> postfixList = convertToPostfix(tokenList);
        return processArithmetic(postfixList).toString();
    }

    private LinkedList<String> StringToTokenList(String input) {
        LinkedList<String> result = new LinkedList<>();
        Matcher matcher = Pattern.compile("\\d+|\\p{Alpha}+|\\(|\\)|\\+|\\-|\\*|\\/|\\^").matcher(input);
        while (matcher.find()) {
            String token = matcher.group();
            if (token.equals("+") || token.equals("-")) {
                if (result.isEmpty() || result.getLast().equals("(")) {
                    matcher.find();
                    token += matcher.group();
                }
            }
            result.addLast(token);
        }
        return result;
    }

    private LinkedList<String> convertToPostfix(LinkedList<String> infixNotation) {
        LinkedList<String> resultList = new LinkedList<>();
        LinkedList<String> operatorsStack = new LinkedList<>();
        while (!infixNotation.isEmpty()) {
            String token = infixNotation.removeFirst();
            if (token.matches("[\\+\\-]?(\\d+|\\p{Alpha}+)")) {
                resultList.addLast(token);
            } else if (token.matches("\\+|\\-|\\*|\\/|\\^")) {
                if (!operatorsStack.isEmpty() &&
                    !operatorsStack.peek().equals("(") &&
                    getOperatorPriority(token) <= getOperatorPriority(operatorsStack.peek())) {
                    while (!operatorsStack.isEmpty() && !operatorsStack.peek().equals("(")) {
                        resultList.addLast(operatorsStack.pop());
                    }
                }
                operatorsStack.push(token);
            } else if (token.matches("\\)")) {
                while (!operatorsStack.isEmpty() && !operatorsStack.peek().equals("(")) {
                    resultList.addLast(operatorsStack.pop());
                }
                if (!operatorsStack.isEmpty()) {
                    operatorsStack.pop();
                }
            } else if (token.matches("\\(")) {
                operatorsStack.push(token);
            }
        }
        while (!operatorsStack.isEmpty()) {
            resultList.addLast(operatorsStack.pop());
        }
        return resultList;
    }

    private int getOperatorPriority(String operator) {
        switch (operator) {
            case "^":
                return 3;
            case "*":
                return 2;
            case "/":
                return 2;
            case "+":
                return 1;
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    private BigInteger processArithmetic(LinkedList<String> postfixNotation) {
        LinkedList<BigInteger> stack = new LinkedList<>();
        for (String token : postfixNotation) {
            if (token.matches("[\\+\\-]?\\d+")) {
                stack.push(new BigInteger(token));
            } else if (token.matches("[\\+\\-]?\\p{Alpha}+")) {
                stack.push(variables.get(token));
            } else {
                BigInteger var2 = stack.pop();
                BigInteger var1 = stack.pop();
                BigInteger result;
                switch (token) {
                    case "+":
                        result = var1.add(var2);
                        break;
                    case "-":
                        result = var1.subtract(var2);
                        break;
                    case "*":
                        result = var1.multiply(var2);
                        break;
                    case "/":
                        result = var1.divide(var2);
                        break;
                    case "^":
                        result = var1.pow(var2.intValue());
                        break;
                    default:
                        result = new BigInteger("0");
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }

    /*
    private int processArithmetic(LinkedList<String> postfixNotation) {
        LinkedList<Integer> stack = new LinkedList<>();
        for (String token : postfixNotation) {
            if (token.matches("[\\+\\-]?\\d+")) {
                stack.push(Integer.parseInt(token));
            } else if (token.matches("[\\+\\-]?\\p{Alpha}+")) {
                stack.push(variables.get(token));
            } else {
                int var2 = stack.pop();
                int var1 = stack.pop();
                int result;
                switch (token) {
                    case "+":
                        result = var1 + var2;
                        break;
                    case "-":
                        result = var1 - var2;
                        break;
                    case "*":
                        result = var1 * var2;
                        break;
                    case "/":
                        result = var1 / var2;
                        break;
                    case "^":
                        result = (int)Math.pow(var1,  var2);
                        break;
                    default:
                        result = 0;
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
     */
}