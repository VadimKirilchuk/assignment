package exercises.interviewTasks.tsystems.Calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 03.05.2015.
 */
public class CalculatorImpl implements Calculator {
    public static void main(String[] args) {

        CalculatorImpl calc = new CalculatorImpl();
        System.out.println(calc.evaluate("-3/2"));
    }

    private enum Operator {
        UNARYMINUS('#', false, 3), MULTIPLY('*', true, 2), DIVIDE('/', true, 2),
        ADD('+', true, 1), SUBTRACT('-', true, 1), LEFT_PARENTHESISE('(', true, 0),
        RIGHT_PARENTHESISE(')', true, 0);
        public static Map<Character, Operator> operatorMap = new HashMap<Character, Operator>();
        private final char operatorSymbol;
        private final int precedence;
        private boolean binary;

        static {
            for (Operator operator : values()) {
                operatorMap.put(operator.operatorSymbol, operator);
            }
        }

        Operator(char operatorSymbol, boolean binary, int precedence) {
            this.operatorSymbol = operatorSymbol;
            this.binary = binary;
            this.precedence = precedence;
        }

        private static Operator getOperator(Character token) {
            return operatorMap.get(token);
        }
    }

    @Override
    public String evaluate(String statement) {

        Deque<Double> operandStack = new ArrayDeque<Double>();
        Deque<Operator> operatorStack = new ArrayDeque<Operator>();
        statement = statement.replaceAll("\\s+", "");

        int statementLength = statement.length();
        Character lastProcessed = null;
        try {
            for (int i = 0; i < statementLength; i++) {
                char token = statement.charAt(i);

                i = analyzeToken(token, lastProcessed, i, statement, operatorStack,
                        operandStack);
                lastProcessed = token;
            }

            processResultExpression(operandStack, operatorStack);
            String result = getStringRoundResult(operandStack);
            return result;
        } catch (Exception e)

        {
            e.printStackTrace();
            return null;
        }
    }

    private int analyzeToken(char token, Character lastProcessed, int tokenIndex,
                             String statement, Deque<Operator> operatorStack,
                             Deque<Double> operandStack) {
        if (isOperator(token)) {
            int statementLength = statement.length();
            processOperatorToken(token, lastProcessed, tokenIndex, statementLength,
                    operatorStack, operandStack);
            return tokenIndex;
        } else {
            if (isOperand(token)) {
                return processOperand(lastProcessed, operandStack, statement, tokenIndex);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private void processOperatorToken(char token, Character lastProcessed,
                                      int tokenIndex, int statementLength,
                                      Deque<Operator> operatorStack,
                                      Deque<Double> operandStack) {
        switch (token) {
            case '(':
                processLeftParenthesis(token, lastProcessed, operatorStack);
                break;
            case ')':
                processRightParenthesis(lastProcessed, operatorStack, operandStack);
                break;
            default:
                processOperator(token, lastProcessed, operandStack, operatorStack,
                        tokenIndex, statementLength);
        }
    }

    private String getStringRoundResult(Deque<Double> operandStack) {
        Double result = operandStack.pollLast();
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(4);
        return numberFormat.format(result);
    }

    private void processResultExpression(Deque<Double> operandStack,
                                         Deque<Operator> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peekLast() != Operator.LEFT_PARENTHESISE) {
            execute(operatorStack, operandStack);
        }
        if (!operatorStack.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidOperand(Character lastProcessed) {
        return Operator.getOperator(lastProcessed) != Operator.RIGHT_PARENTHESISE;
    }

    private int processOperand(Character lastProcessed, Deque<Double> operandStack,
                               String statement, int tokenIndex) {

        if (isValidOperand(lastProcessed)) {

            StringBuilder stringBuilder = new StringBuilder();
            int statementLength = statement.length();
            Double number = null;

            //loop for   number, walk across the string and append parts of number
            // is this loop good?

            while (tokenIndex != statementLength && ((isOperand(statement.charAt(tokenIndex))
                    || statement.charAt(tokenIndex) == '.'))) {

                stringBuilder.append(statement.charAt(tokenIndex));
                tokenIndex++;
            }
            String stringNumber = stringBuilder.toString();
            //convert to double

            number = Double.valueOf(stringNumber);

            operandStack.addLast(number);
        } else {

            throw new IllegalArgumentException();
        }
        return (tokenIndex - 1);
    }

    //todo:сравниваем через char
    private boolean isValidUnaryOperator(Operator operator, Character lastProcessed) {
        return (operator == Operator.SUBTRACT) && (lastProcessed == null
                || lastProcessed == Operator.LEFT_PARENTHESISE.operatorSymbol);
    }

    private boolean isValidBinaryOperator(Character lastProcessed,
                                          int tokenIndex, int statementLength) {
        return (isOperand(lastProcessed) || (lastProcessed != null
                && (lastProcessed == Operator.RIGHT_PARENTHESISE.operatorSymbol)))
                && (tokenIndex != statementLength - 1);
    }

    private void processOperator(char token, Character lastProcessed,
                                 Deque<Double> operandStack,
                                 Deque<Operator> operatorStack,
                                 int tokenIndex, int statementLength) {
        Operator operator = Operator.getOperator(token);

        // if "-" is unary ,"-1+2","(-1+4)"
        //here checking that lastProcessed ==null is necessary, it is OK
        if (isValidUnaryOperator(operator, lastProcessed)) {
            operatorStack.addLast(Operator.UNARYMINUS);
        } else {

            //if we have binary operation
            //OK - "2+3", "...)+12"
            //NOT OK - "+2"," /2+3 ", "2+3+", "3++5"
            if (isValidBinaryOperator(lastProcessed, tokenIndex, statementLength)) {
                Operator stackOperator = operatorStack.peekLast();
                //if operator precedence <= precedence of last operation at stack
                // we execute operation till precedence become > than operation from stack or till we find "("
                //&& stackOperator != Operator.LEFT_PARENTHESISE
                while (stackOperator != null && checkPrecedence(operator, stackOperator)) {
                    execute(operatorStack, operandStack);
                    stackOperator = operatorStack.peekLast();
                }
                //add operator to stack
                operatorStack.addLast(operator);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isValidLeftParenthesis(Character lastProcessed) {
        return !isOperand(lastProcessed)
                && (Operator.getOperator(lastProcessed) != Operator.RIGHT_PARENTHESISE);
    }

    //here we process case where our symbol is left parenthesis
    private void processLeftParenthesis(char token, Character lastProcessed,
                                        Deque<Operator> operatorStack) {
        // we work only with operators, "(",")" and numbers, so on previous step symbol could be number,
        // operator or"(", because when we work with ")" we execute expression,  and "(" and operator is good, so
        // we can put to stack "(" only if previous symbol in statement was not number-"(1+3...","1+(..", "((..."
        // not "2(..."

        if (isValidLeftParenthesis(lastProcessed)) {
            Operator operator = Operator.getOperator(token);
            operatorStack.addLast(operator);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidRightParenthesis(Character lastProcessed) {
        return isOperand(lastProcessed)
                || Operator.getOperator(lastProcessed) == Operator.RIGHT_PARENTHESISE;
    }

    //here we process case where our symbol is left parenthesis
    private void processRightParenthesis(Character lastProcessed,
                                         Deque<Operator> operatorStack,
                                         Deque<Double> operandStack) {
        //we can use ")" only if last symbol was number or "(", good situation-"..8)","...))"
        //not good "..+)"

        if (isValidRightParenthesis(lastProcessed)) {

            //while stack is not empty we should search "(", and make some work with one operator
            // and one or two numbers, after execute expression we should put result to operand stack
            while (!operatorStack.isEmpty() && operatorStack.peekLast() != Operator.LEFT_PARENTHESISE) {
                execute(operatorStack, operandStack);
            }
            // if we didnt find "(" we report about error, because we haven't pair for ")"
            if (!operatorStack.isEmpty()) {
                operatorStack.pollLast();
            } else {
                // if everything is great we remove "(" from stack

                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void execute(Deque<Operator> operatorStack,
                         Deque<Double> operandStack) {
        Operator operator = operatorStack.pollLast();

        Double result = null;
        if (!operator.binary) {
            Double operand = operandStack.pollLast();
            result = executeOperation(operator, operand, null);
        } else {
            Double firstOperand = operandStack.pollLast();
            Double secondOperand = operandStack.pollLast();
            result = executeOperation(operator, firstOperand, secondOperand);
        }
        operandStack.addLast(result);
    }

    private Double executeOperation(Operator operator, Double firstOperand,
                                    Double secondOperand) {
        switch (operator) {
            case UNARYMINUS:
                return (-firstOperand);
            case MULTIPLY:
                return secondOperand * firstOperand;
            case DIVIDE:
                return secondOperand / firstOperand;
            case ADD:
                return secondOperand + firstOperand;
            case SUBTRACT:
                return secondOperand - firstOperand;
            default:
                return null;
        }
    }

    private boolean checkPrecedence(Operator token, Operator stackOperator) {
        return token.precedence <= stackOperator.precedence;
    }

    private boolean isOperand(char token) {

        return token >= '0' && token <= '9';
    }

    private boolean isOperand(Character token) {
        return token != null && isOperand(token.charValue());
    }

    private boolean isOperator(char token) {
        Operator operator = Operator.getOperator(token);
        //any operator is good for us except unary, because unary is our shadowside operator,
        // we analyze binary operator and if it satisfy our condition then we use unary version
        return (operator != null) && operator.binary;
    }

    private boolean isOperator(Character token) {
        return token != null && isOperator(token.charValue());
    }
}
