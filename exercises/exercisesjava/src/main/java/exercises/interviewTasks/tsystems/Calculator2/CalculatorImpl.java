package exercises.interviewTasks.tsystems.Calculator2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Андрей on 11.05.2015.
 */
public class CalculatorImpl implements Calculator {
    @Override
    public String evaluate(String statement) {

        Deque<Double> operandStack = new ArrayDeque<Double>();
        Deque<Operator> operatorStack = new ArrayDeque<Operator>();
        WaitingStatus status = WaitingStatus.WAITING_OPERAND;

        try {
            LexicalAnalyzer analyzer = new LexicalAnalyzer(statement);
            for (Token token : analyzer) {
                switch (status) {
                    case WAITING_OPERAND:
                        processWaitingOperand(token, operandStack, operatorStack);
                        break;

                    case WAITING_OPERATOR:
                        processWaitingOperator(token, operandStack, operatorStack);
                        break;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void processWaitingOperand(Token token, Deque<Double> operandStack,
                                       Deque<Operator> operatorStack) {
        switch (token.tokenType) {
            case NUMBER:
                operandStack.addLast(((OperandToken) token).getValue());
                break;
            case SUBTRACT:
// унарный минус
            case LEFT_PARENTHESISE:
                operatorStack.addLast(((OperatorToken) token).getValue());
                break;
            default:
                throw new ParseException("Illegal character");
        }
    }

    private void processWaitingOperator(Token token, Deque<Double> operandStack,
                                        Deque<Operator> operatorStack) {

        switch (token.tokenType) {
            case RIGHT_PARENTHESISE:
            case NUMBER:
                throw new ParseException("Illegal character");
            default:
                // работа с приоритетами
        }
    }

    private class LexicalAnalyzer implements Iterator<Token>, Iterable<Token> {
        private Reader reader;
        private Token nextToken;

        LexicalAnalyzer(String statement) {
            reader = new BufferedReader(new StringReader(statement));
            setNextTokenRead();
        }

        @Override
        public Iterator<Token> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            // ensureTokenRead();
            return nextToken.tokenType != TokenType.END_OF_EXPRESSION;
        }

        // что если возникнут пробемы при конструировании следующего элемента
// например когда следующий элемент буква, получается
// мы готовый вернуть текущий элемент с ним все ок, но со следующим проблема и мы,
// выход изза ошибки на следующем шаге не выглядит ли это странно?
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

        private void closeAnalyzer() {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ParseException(e);
            }
        }

        private Token nextInternal() throws IOException {
            skipWhiteSpaces();
            int character = reader.read();
            if (character == -1) {

                return new Token(TokenType.END_OF_EXPRESSION);
            }
            char characterCode = (char) character;
            switch (characterCode) {
                case ('('):
                    return new OperatorToken(TokenType.LEFT_PARENTHESISE, Operator.LEFT_PARENTHESISE);
                case (')'):
                    return new OperatorToken(TokenType.RIGHT_PARENTHESISE, Operator.RIGHT_PARENTHESISE);
                case ('+'):
                    return new OperatorToken(TokenType.ADD, Operator.ADD);
                case ('-'):
                    return new OperatorToken(TokenType.SUBTRACT, Operator.SUBTRACT);
                case ('*'):
                    return new OperatorToken(TokenType.MULTIPLY, Operator.MULTIPLY);
                case ('/'):
                    return new OperatorToken(TokenType.DIVIDE, Operator.DIVIDE);
                default:
                    if (Character.isDigit(characterCode)) {
                        double number = constructNumber(characterCode);
                        return new OperandToken(TokenType.NUMBER, number);
                    } else {
                        throw new ParseException("Illegal character at statement");
                    }
            }
        }

        // учитываем ли 2.  в качестве допустимого числа ?
        private double constructNumber(char characterCode) throws IOException {
            StringBuilder builder = new StringBuilder();
            int character;
            do {
                builder.append(characterCode);
                reader.mark(1);
                character = reader.read();
                characterCode = (char) character;
            } while ((character != -1) && (Character.isDigit(character) || characterCode == '.'));
            reader.reset();
            return Double.parseDouble(builder.toString());
        }

        private void skipWhiteSpaces() throws IOException {
            int character;
            do {
                reader.mark(1);
                character = reader.read();
            } while ((character != -1) && Character.isWhitespace(character));
            reader.reset();
        }
    }

    private class Token {
        public TokenType tokenType;

        Token(TokenType tokenType) {
            this.tokenType = tokenType;
        }
    }

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
        WAITING_OPERATOR;

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
        UNARY_MINUS(3),
        MULTIPLY(2),
        DIVIDE(2),
        ADD(1),
        SUBTRACT(1),
        LEFT_PARENTHESISE(0),
        RIGHT_PARENTHESISE(0);
        private final int precedence;

        Operator(int precedence) {

            this.precedence = precedence;
        }
    }
}


