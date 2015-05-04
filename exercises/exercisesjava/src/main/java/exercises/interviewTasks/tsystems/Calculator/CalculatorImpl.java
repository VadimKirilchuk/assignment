package exercises.interviewTasks.tsystems.Calculator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Андрей on 03.05.2015.
 */
public class CalculatorImpl implements Calculator {
    public static void main(String[] args) {

        CalculatorImpl calc = new CalculatorImpl();
        System.out.println(calc.evaluate("3+(2+4*2)+1"));
    }

    private enum Operator {
        UNARYMINUS('#', 'u', 3), MULTIPLY('*', 'b', 2), DIVIDE('/', 'b', 2),
        ADD('+', 'b', 1), SUBTRACT('-', 'b', 1);
        private final char operatorSymbol;
        private final int precedence;
        private char operationStyle;

        Operator(char operatorSymbol, char operationStyle, int precedence) {
            this.operatorSymbol = operatorSymbol;
            this.operationStyle = operationStyle;
            this.precedence = precedence;
        }

        private static Operator getOperator(char token) {
            Operator[] arrayOfOperators = values();
            for (int i = 0; i < arrayOfOperators.length; i++) {
                if (token == arrayOfOperators[i].operatorSymbol) {
                    return arrayOfOperators[i];
                }
            }
            return null;
        }
    }

    @Override
    public String evaluate(String statement) {

        Deque<Double> operandStack = new ArrayDeque<Double>();
        Deque<Character> operatorStack = new ArrayDeque<Character>();
        statement = statement.replaceAll("\\s+", "");

        int lengthOfStatement = statement.length();
        Character lastProcessed = null;
        try {
            for (int i = 0; i < lengthOfStatement; i++) {
                char token = statement.charAt(i);

                if (token == '(') {
                    processLeftParenthesis(token, lastProcessed, operatorStack);
                    lastProcessed = token;
                } else {
                    if (token == ')') {
                        processRightParenthesis(lastProcessed, operatorStack, operandStack);
                        lastProcessed = token;
                    } else {
                        if (isOperator(token)) {
                            processOperator(token, lastProcessed, operandStack, operatorStack, i,
                                    lengthOfStatement);
                            lastProcessed = token;
                        } else {
                            if (isOperand(token)) {
                                i = processOperand(lastProcessed, operandStack, statement,
                                        i, lengthOfStatement);
                                lastProcessed = token;
                            } else {
                                return null;
                            }
                        }
                    }
                }
            }
            processFinalExpression(operandStack, operatorStack);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        double result = operandStack.pollLast();
        return BigDecimal.valueOf(result).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
    }

    private void processFinalExpression(Deque<Double> operandStack,
                                        Deque<Character> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peekLast() != '(') {
            execute(operatorStack, operandStack);
        }
        if (!operatorStack.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private int processOperand(Character lastProcessed, Deque<Double> operandStack,
                               String statement, int tokenIndex, int statementLength) {

        //todo:here is my problem, i must make extra checking last symbol on null, because lastProcessed is object,
        //we can have case where lastProcessed ==null, so operand is first symbol,
        // and if we will make lastProcessed ==")" we will get error
        if (lastProcessed == null || lastProcessed != ')') {

            StringBuilder stringBuilder = new StringBuilder();
            //loop for   number, walk across the string and append parts of number
            // is this loop good?
            while (tokenIndex != statementLength && (isOperand(statement.charAt(tokenIndex))
                    || statement.charAt(tokenIndex) == '.')) {
                stringBuilder.append(statement.charAt(tokenIndex));
                tokenIndex++;
            }
            String number = stringBuilder.toString();
            //convert to double
            Double doubleNumber = Double.valueOf(number);
            operandStack.addLast(doubleNumber);
        } else {

            throw new IllegalArgumentException();
        }
        return (tokenIndex - 1);
    }

    private void processOperator(char token, Character lastProcessed,
                                 Deque<Double> operandStack,
                                 Deque<Character> operatorStack,
                                 int tokenIndex, int statementLength) {
        // todo: don't like this mechanism, to analyze operator we should convert token to enum member
        Operator operator = Operator.getOperator(token);

        // if "-" is unary ,"-1+2","(-1+4)"
        //here checking that lastProcessed ==null is necessary, it is OK
        if ((operator == Operator.SUBTRACT) && (lastProcessed == null || lastProcessed == '(')) {
            operatorStack.addLast(Operator.UNARYMINUS.operatorSymbol);
        } else {

            //if we have binary operation
            //OK - "2+3", "...)+12"
            //NOT OK - "+2"," /2+3 ", "2+3+", "3++5"
            ///todo:here is my problem, i must make extra checking last symbol on null, because lastProcessed is object
            if (lastProcessed != null && (isOperand(lastProcessed) || lastProcessed == ')')
                    && (tokenIndex != statementLength - 1)) {
                Character stackOperator = operatorStack.peekLast();
                //if operator precedence <= precedence of last operation at stack
                // we execute operation till precedence become > than operation from stack or till we find "("
                while (stackOperator != null && stackOperator != '('
                        && checkPrecedence(token, stackOperator)) {
                    execute(operatorStack, operandStack);
                    stackOperator = operatorStack.peekLast();
                }
                //add operator to stack
                operatorStack.addLast(token);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
//here we process case where our symbol is left parenthesis
    private void processLeftParenthesis(char token, Character lastProcessed,
                                        Deque<Character> operatorStack) {
        // we work only with operators, "(",")" and numbers, so on previous step symbol could be number,
        // operator or"(", because when we work with ")" we execute expression,  and "(" and operator is good, so
        // we can put to stack "(" only if previous symbol in statement was not number-"(1+3...","1+(..", "((..."
        // not "2(..."

        //todo:here is my problem, i must make extra checking last symbol on null, because lastProcessed is object,
        //and we can't check null at isOperand(char symb)
        if (lastProcessed == null || !isOperand(lastProcessed)) {
            operatorStack.addLast(token);
        } else {
            throw new IllegalArgumentException();
        }
    }
    //here we process case where our symbol is left parenthesis
    private void processRightParenthesis(Character lastProcessed,
                                         Deque<Character> operatorStack,
                                         Deque<Double> operandStack) {
        //we can use ")" only if last symbol was number or "(", good situation-"..8)","...))"
        //not good "..+)"

        //todo:here is my problem, i must make extra checking last symbol on null, because lastProcessed is object,
        if (lastProcessed != null && isOperand(lastProcessed)) {

            //while stack is not empty we should search "(", and make some work with one operator
            // and one or two numbers, after execute expression we should put result to operand stack
            while (!operatorStack.isEmpty() && operatorStack.peekLast() != '(') {
                execute(operatorStack, operandStack);
            }
            // if we didnt find "(" we report about error, because we haven't pair for ")"
            if (operatorStack.isEmpty()) {
                throw new IllegalArgumentException();
            } else {
                // if everything is great we remove "(" from stack
                operatorStack.pollLast();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void execute(Deque<Character> operatorStack,
                         Deque<Double> operandStack) {
        Character stackOperator = operatorStack.pollLast();
        Operator operator = Operator.getOperator(stackOperator);
        Double result = null;
        if (operator.operationStyle == 'u') {
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


    private boolean checkPrecedence(char token, char stackOperator) {
        Operator operatorForToken = Operator.getOperator(token);
        Operator operatorForStackOperator = Operator.getOperator(stackOperator);
        return operatorForToken.precedence <= operatorForStackOperator.precedence;
    }

    private boolean isOperand(char token) {

        return (token >= '0' && token <= '9');
    }

    private boolean isOperator(char token) {
        Operator operator = Operator.getOperator(token);
        //any operator is good for us except unary, because unary is our shadowside operator,
        // we analyze binary operator and if it satisfy our condition then we use unary version
        return (operator != null) && (operator.operationStyle != 'u');
    }
}
