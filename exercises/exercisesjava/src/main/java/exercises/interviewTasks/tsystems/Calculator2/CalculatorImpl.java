package exercises.interviewTasks.tsystems.Calculator2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Андрей on 11.05.2015.
 */
public class CalculatorImpl implements Calculator {
    public static void main(String[] args) {
        CalculatorImpl calc = new CalculatorImpl();
        // ошибка ли это("9+(-10   .0)/(-2)")
        System.out.println(calc.evaluate("09+(-00010 )/(-2)"));
    }

    @Override
    public String evaluate(String statement) {

        Deque<Double> operandStack = new ArrayDeque<Double>();
        Deque<Operator> operatorStack = new ArrayDeque<Operator>();
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        Status status = new Status();

        try {
            try {
// не лучше ли использовать итерабл для навигации по токенам
                Iterator<Token> iterator = analyzer.parse(statement);
                while (iterator.hasNext()) {
                    Token token = iterator.next();
                    switch (status.getStatus()) {
                        case WAITING_OPERAND:
                            processWaitingOperand(token, operandStack, operatorStack, status);
                            break;
                        case WAITING_OPERATOR:
                            processWaitingOperator(token, operandStack, operatorStack, status);
                            break;
                        case WAITING_OPERAND_NON_UNARY_OPERATOR:
                            processWaitingOperandNonUnaryOperator(token, operandStack, operatorStack, status);
                            break;
                    }
                }
                if (status.getStatus() == WaitingStatus.WAITING_OPERATOR) {
                    processResultExpression(operandStack, operatorStack);
                    String result = getStringRoundResult(operandStack);
                    return result;
                } else {
                    throw new ParseException("Illegal state");
                }
            } finally {
                analyzer.closeAnalyzer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processResultExpression(Deque<Double> operandStack,
                                         Deque<Operator> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peekLast() != Operator.LEFT_PARENTHESISE) {
            execute(operatorStack.pollLast(), operandStack);
        }
        if (!operatorStack.isEmpty()) {
            throw new ParseException("Illegal character");
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

    private void processWaitingOperandNonUnaryOperator(Token token, Deque<Double> operandStack,
                                                       Deque<Operator> operatorStack, Status status) {
        switch (token.tokenType) {
            case NUMBER:
                String number = token.getValue();
                operandStack.addLast(Double.valueOf(number));
                status.setStatus(WaitingStatus.WAITING_OPERATOR);
                break;
            case LEFT_PARENTHESISE:
                operatorStack.addLast(Operator.LEFT_PARENTHESISE);
                status.setStatus(WaitingStatus.WAITING_OPERAND);
                break;
            default:
                throw new ParseException("Illegal character");
        }
    }

    private void processWaitingOperand(Token token, Deque<Double> operandStack,
                                       Deque<Operator> operatorStack, Status status) {
        switch (token.tokenType) {
            case NUMBER:
                String number = token.getValue();
                operandStack.addLast(Double.valueOf(number));
                status.setStatus(WaitingStatus.WAITING_OPERATOR);
                break;
            case SUBTRACT:
                Operator operator = Operator.UNARY_MINUS;
                // нужно ли, думаю нет
                while (!operatorStack.isEmpty() && operatorStack.peekLast().precedence >= operator.precedence) {
                    execute(operatorStack.pollLast(), operandStack);
                }
                operatorStack.addLast(operator);
                status.setStatus(WaitingStatus.WAITING_OPERAND_NON_UNARY_OPERATOR);
                break;
            case LEFT_PARENTHESISE:
                operatorStack.addLast(Operator.LEFT_PARENTHESISE);
                break;
            default:
                throw new ParseException("Illegal character");
        }
    }

    private void processWaitingOperator(Token token, Deque<Double> operandStack,
                                        Deque<Operator> operatorStack, Status status) {
        switch (token.tokenType) {
            case RIGHT_PARENTHESISE:
                while (!operatorStack.isEmpty() && operatorStack.peekLast() != Operator.LEFT_PARENTHESISE) {
                    execute(operatorStack.pollLast(), operandStack);
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pollLast();
                } else {
                    throw new ParseException("Illegal character");
                }
                break;
            case LEFT_PARENTHESISE:
            case NUMBER:
                throw new ParseException("Illegal character");
            default:
                String value = token.getValue();
                //не очень красиво
                char operatorChar = value.charAt(0);
                Operator operator = Operator.getOperator(operatorChar);
                while (!operatorStack.isEmpty() && operatorStack.peekLast().precedence >= operator.precedence) {
                    execute(operatorStack.pollLast(), operandStack);
                }
                operatorStack.addLast(operator);
                status.setStatus(WaitingStatus.WAITING_OPERAND_NON_UNARY_OPERATOR);
                break;
        }
    }

    private void execute(Operator operator,
                         Deque<Double> operandStack) {

        Double result = null;
        if (!operator.isBinary) {
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
            case UNARY_MINUS:
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

    private class Status {
        private WaitingStatus waitingStatus = WaitingStatus.WAITING_OPERAND;

        public WaitingStatus getStatus() {
            return waitingStatus;
        }

        public void setStatus(WaitingStatus waitingStatus) {
            this.waitingStatus = waitingStatus;
        }
    }

    private class LexicalAnalyzer {
        private Reader reader;

        public Iterator<Token> parse(String statement) {
            reader = new BufferedReader(new StringReader(statement));
            return new LexIterator();
        }

        /*
                @Override
                public Iterator<Token> iterator () {
                    BufferedReader reader=new BufferedReader(new StringReader(statement));
                }
                */
//какое исключение кинуть
        private void closeAnalyzer() {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ParseException(e);
            }
        }

        private class LexIterator implements Iterator<Token> {
            // private BufferedReader reader;
            private Token nextToken;

            LexIterator() {
                //  this.reader = reader;
                setNextTokenRead();
            }

            @Override
            public boolean hasNext() {
                // ensureTokenRead();
                return nextToken.tokenType != TokenType.END_OF_EXPRESSION;
            }

            @Override
            public Token next() {
                if (nextToken.tokenType != TokenType.END_OF_EXPRESSION) {

                    Token token = nextToken;
                    setNextTokenRead();
                    return token;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported");
            }

            private void setNextTokenRead() {
                try {
                    nextToken = nextInternal();
                } catch (IOException e) {

                    throw new ParseException(e);
                }
            }

            private Token nextInternal() throws IOException {
                skipWhiteSpaces();
                int character = reader.read();
                if (character == -1) {
                    return new Token(TokenType.END_OF_EXPRESSION, null);
                }
                char characterCode = (char) character;
                switch (characterCode) {
                    case ('('):
                        return new Token(TokenType.LEFT_PARENTHESISE, String.valueOf(characterCode));
                    case (')'):
                        return new Token(TokenType.RIGHT_PARENTHESISE, String.valueOf(characterCode));
                    case ('+'):
                        return new Token(TokenType.ADD, String.valueOf(characterCode));
                    case ('-'):
                        return new Token(TokenType.SUBTRACT, String.valueOf(characterCode));
                    case ('*'):
                        return new Token(TokenType.MULTIPLY, String.valueOf(characterCode));
                    case ('/'):
                        return new Token(TokenType.DIVIDE, String.valueOf(characterCode));
                    default:
                        if (Character.isDigit(characterCode)) {
                            String number = constructNumber(characterCode);
                            return new Token(TokenType.NUMBER, number);
                        } else {
                            throw new ParseException("Illegal character at statement");
                        }
                }
            }

            // учитываем ли 2.  в качестве допустимого числа ?
            private String constructNumber(char characterCode) throws IOException {
                StringBuilder builder = new StringBuilder();
                int character;
                do {
                    builder.append(characterCode);
                    reader.mark(1);
                    character = reader.read();
                    characterCode = (char) character;
                } while ((character != -1) && (Character.isDigit(character) || characterCode == '.'));
                reader.reset();
                return builder.toString();
            }

            private void skipWhiteSpaces() throws IOException {
                int character;
                do {
                    reader.mark(1);
                    character = reader.read();
                } while ((character != -1) && Character.isWhitespace((char) character));
                reader.reset();
            }
        }
    }

    private class Token {
        public TokenType tokenType;
        public String value;

        Token(TokenType tokenType, String value) {
            this.tokenType = tokenType;
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /*
        private class OperandToken extends Token {
            double value;

            OperandToken(TokenType tokenType, double value) {
                super(tokenType);
                this.value = value;
            }

            public double getValue() {
                return value;
            }
        }

        private class OperatorToken extends Token {
            Operator value;

            OperatorToken(TokenType tokenType, Operator value) {
                super(tokenType);
                this.value = value;
            }

            public Operator getValue() {
                return value;
            }
        }

    */
    private class ParseException extends RuntimeException {
        private static final long serialVersionUID = -8526479705887018360L;

        public ParseException(String message) {
            super(message);
        }

        public ParseException(Throwable cause) {
            super(cause);
        }
    }

    private enum WaitingStatus {
        WAITING_OPERAND,
        WAITING_OPERATOR,
        WAITING_OPERAND_NON_UNARY_OPERATOR;

    }

    private enum TokenType {
        END_OF_EXPRESSION,
        NUMBER,
        LEFT_PARENTHESISE,
        RIGHT_PARENTHESISE,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE;

    }

    public enum Operator {
        UNARY_MINUS('#', 3, false),
        MULTIPLY('*', 2, true),
        DIVIDE('/', 2, true),
        ADD('+', 1, true),
        SUBTRACT('-', 1, true),
        LEFT_PARENTHESISE('(', 0, true),
        RIGHT_PARENTHESISE(')', 0, true);
        public static Map<Character, Operator> operatorMap = new HashMap<Character, Operator>();
        private final int precedence;
        private final char operatorCharacter;
        private final boolean isBinary;

        // нужна ли мапа здесь?
        static {
            for (Operator operator : values()) {
                operatorMap.put(operator.operatorCharacter, operator);
            }
        }

        Operator(char operatorCharacter, int precedence, boolean isBinary) {
            this.operatorCharacter = operatorCharacter;
            this.precedence = precedence;
            this.isBinary = isBinary;
        }

        private static Operator getOperator(char token) {
            return operatorMap.get(token);
        }
    }
}


//итератор стэйтфул стейтлес название метода parse? закрытие потока ввода
// проверка на приоритет при обработке унарного минуса
// получение оператора processOperator