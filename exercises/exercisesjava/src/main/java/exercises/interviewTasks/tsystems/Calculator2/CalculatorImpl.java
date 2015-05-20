package exercises.interviewTasks.tsystems.Calculator2;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Calculator implementation based on the idea of Finite-state machine and
 * Shunting-yard algorithm. This class provides methods for calculation expressions
 * which include following operations(«+», «-», «*», «/»). To calculate expression
 * it is needed to impart the string of expression to the {@link #evaluate(String)} method.
 *
 * @author Korestalev Andrey
 */
public class CalculatorImpl implements Calculator {
    public static void main(String[] args) {

        CalculatorImpl calc = new CalculatorImpl();

        System.out.println(calc.evaluate("-(10) /(-3)"));
    }

    /**
     * Calculates mathematical expression and rounds result to four digits after point,
     * if there  are a lot of digits after point. The logic of this method is based on
     * work of Finite-state machine, the method receives  string statement and transfers
     * it to the {@code LexicalAnalyzer}, which divides the string into tokens of various type.
     * Depending on the state of Finite-state machine, token, operand stack and
     * operators stack are being transferred for processing  to one of the methods
     * ({@link #processWaitingOperand(Token, Deque, Deque, State)},
     * {@link #processWaitingOperandNonUnaryOperator(Token, Deque, Deque, State)},
     * {@link #processWaitingOperator(Token, Deque, Deque, State)}) for further analysis.
     * When statement is completely processed by {@code LexicalAnalyzer}, the final result
     * is being calculated, rounded up and return.
     * If error occurs then return  null.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: {@code "(1 + 38) * 4.5 - 1 / 2.)"}
     * @return string value containing result of evaluation or null if statement is invalid
     */
    @Override
    public String evaluate(String statement) {
        Deque<Double> operandStack = new ArrayDeque<>();
        Deque<Operator> operatorStack = new ArrayDeque<>();
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        State state = new State();

        try (StringReader statementReader = new StringReader(statement)) {
            Iterator<Token> iterator = analyzer.parse(statementReader);
            while (iterator.hasNext()) {
                Token token = iterator.next();
                switch (state.getState()) {
                    case WAITING_OPERAND:
                        processWaitingOperand(token, operandStack, operatorStack, state);
                        break;
                    case WAITING_OPERATOR:
                        processWaitingOperator(token, operandStack, operatorStack, state);
                        break;
                    case WAITING_OPERAND_NON_UNARY_OPERATOR:
                        processWaitingOperandNonUnaryOperator(token, operandStack, operatorStack, state);
                        break;
                }
            }
            if (state.getState() == WaitingState.WAITING_OPERATOR) {
                processResultExpression(operandStack, operatorStack);
                String result = getStringRoundResult(operandStack);
                return result;
            } else {
                throw new ParseException("Illegal state");
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Processes the token and stores it at the stack of operators or stack of operands.
     * Method will be called when WAITING_OPERAND_NON_UNARY_OPERATOR state of
     * Finite-state machine occurs. If token type is not NUMBER or
     * LEFT_PARENTHESISE then exception will be thrown.
     *
     * @param token         token with specific type and value
     * @param operandStack  stack of operands
     * @param operatorStack stack of operators
     * @param state         object stores current state of Finite-state machine
     * @throws ParseException if character is illegal
     */
    private void processWaitingOperandNonUnaryOperator(Token token, Deque<Double> operandStack,
                                                       Deque<Operator> operatorStack, State state) {
        switch (token.getType()) {
            case NUMBER:
                String number = token.getValue();
                operandStack.addLast(Double.valueOf(number));
                state.setState(WaitingState.WAITING_OPERATOR);
                break;
            case LEFT_PARENTHESISE:
                operatorStack.addLast(Operator.LEFT_PARENTHESISE);
                state.setState(WaitingState.WAITING_OPERAND);
                break;
            default:
                throw new ParseException("Illegal character");
        }
    }

    /**
     * Processes the token and stores it at the stack of operators or stack of operands.
     * Method will be called when WAITING_OPERAND state of Finite-state machine occurs.
     * This method also processes unary minus.Allowable token types are as follows:
     * NUMBER,SUBTRACT, LEFT_PARENTHESISE, any other types are
     * undesirable so exception will be thrown.
     *
     * @param token         token with specific type and value
     * @param operandStack  stack of operands
     * @param operatorStack stack of operators
     * @param state         object stores current state of Finite-state machine
     * @throws ParseException if character is illegal
     */
    private void processWaitingOperand(Token token, Deque<Double> operandStack,
                                       Deque<Operator> operatorStack, State state) {
        switch (token.getType()) {
            case NUMBER:
                String number = token.getValue();
                operandStack.addLast(Double.valueOf(number));
                state.setState(WaitingState.WAITING_OPERATOR);
                break;
            case SUBTRACT:
                Operator operator = Operator.UNARY_MINUS;
                operatorStack.addLast(operator);
                state.setState(WaitingState.WAITING_OPERAND_NON_UNARY_OPERATOR);
                break;
            case LEFT_PARENTHESISE:
                operatorStack.addLast(Operator.LEFT_PARENTHESISE);
                break;
            default:
                throw new ParseException("Illegal character");
        }
    }

    /**
     * Processes the token. Method will be called when WAITING_OPERAND state of
     * Finite-state machine occurs. This method can processes tokens which
     * represent operators and make intermediate calculation.
     * Illegal token types: LEFT_PARENTHESISE and NUMBER, so
     * if token has one of these types exception will be thrown.
     *
     * @param token         token with specific type and value
     * @param operandStack  stack of operands
     * @param operatorStack stack of operators
     * @param state         object stores current state of Finite-state machine
     * @throws ParseException if character is illegal
     */
    private void processWaitingOperator(Token token, Deque<Double> operandStack,
                                        Deque<Operator> operatorStack, State state) {
        switch (token.getType()) {
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
                Operator operator = Operator.getOperator(value);
                while (!operatorStack.isEmpty() && operatorStack.peekLast().precedence >= operator.precedence) {
                    execute(operatorStack.pollLast(), operandStack);
                }
                operatorStack.addLast(operator);
                state.setState(WaitingState.WAITING_OPERAND_NON_UNARY_OPERATOR);
                break;
        }
    }

    /**
     * Calculates result of expression, processes all operators at the stack of operators,
     * if left parenthesise occurs then exception will be thrown.
     *
     * @param operandStack  stack of operand
     * @param operatorStack stack of operators
     * @throws ParseException if character is illegal
     */
    private void processResultExpression(Deque<Double> operandStack,
                                         Deque<Operator> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.peekLast() != Operator.LEFT_PARENTHESISE) {
            execute(operatorStack.pollLast(), operandStack);
        }
        if (!operatorStack.isEmpty()) {
            throw new ParseException("Illegal character");
        }
    }

    /**
     * Rounds the result of expression and return it as a string.
     *
     * @param operandStack stack of operand
     * @return the string that represents rounding result of expression
     */
    private String getStringRoundResult(Deque<Double> operandStack) {
        Double result = operandStack.pollLast();
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(4);
        return numberFormat.format(result);
    }

    /**
     * Executes concrete operation. If operation is unary than one operand
     * from operand stack will be taken, otherwise if operation is binary than
     * two operands will be taken from operands stack for execution.
     * Operation execution result will be added to the stack of operands
     *
     * @param operator     the operator which specifies concrete mathematical operation.
     * @param operandStack stack of operands
     */
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

    /**
     * Executes mathematical operation.
     *
     * @param operator      the operator which specifies concrete mathematical operation.
     * @param firstOperand  operand for execution operation
     * @param secondOperand operand for execution operation
     * @return result of operation execution
     */
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

    /**
     * State holder.Instance of this class should be used as state holder,
     * to hold  current state of Finite-state machine.
     */
    private class State {
        private WaitingState waitingState = WaitingState.WAITING_OPERAND;

        public WaitingState getState() {
            return waitingState;
        }

        public void setState(WaitingState waitingState) {
            this.waitingState = waitingState;
        }
    }

    /**
     * Lexical Analyzer
     */
    private class LexicalAnalyzer {
        /**
         * Returns iterator over the tokens of statement accessed by reader,
         * which should be passed to this method.
         *
         * @param reader instance of StringReader, for separate chars access
         * @return iterator over the tokens
         */
        public Iterator<Token> parse(StringReader reader) {
            return new LexIterator(reader);
        }

        private class LexIterator implements Iterator<Token> {
            private Reader reader;
            private Token nextToken;

            LexIterator(StringReader reader) {
                this.reader = reader;
                setNextTokenRead();
            }

            @Override
            public boolean hasNext() {
                return nextToken.getType() != TokenType.END_OF_EXPRESSION;
            }

            @Override
            public Token next() {
                if (nextToken.getType() != TokenType.END_OF_EXPRESSION) {
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

            /**
             * Returns next token or throws exception if character for token is not permitted.
             *
             * @return token with concrete type and value
             * @throws IOException    if any problems during reading characters occurs
             * @throws ParseException if character is illegal
             */
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
                            throw new ParseException("Illegal character");
                        }
                }
            }

            /**
             * Constructs number from character sequence.
             *
             * @param characterCode first character of number
             * @return string representing of number
             * @throws IOException if any problems during reading characters occurs
             */
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
                } while ((character != -1) && Character.isWhitespace(character));
                reader.reset();
            }
        }
    }

    /**
     * Class represents specific lexeme with concrete type.
     */
    private class Token {
        private final TokenType tokenType;
        private final String value;

        Token(TokenType tokenType, String value) {
            this.tokenType = tokenType;
            this.value = value;
        }

        public TokenType getType() {
            return tokenType;
        }

        public String getValue() {
            return value;
        }
    }

    private class ParseException extends RuntimeException {
        /**
         * Constructs a {@code ParseException}, saving a reference to the
         * error message string {@code message} for later retrieval by the
         * {@code getMessage} method.
         *
         * @param message the detail message.
         */
        public ParseException(String message) {
            super(message);
        }

        /**
         * Constructs a new exception with the specified cause and a detail
         * message of {@code cause==null ? null : cause.toString()} (which
         * typically contains the class and detail message of {@code cause}).
         *
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A {@code null} value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         */
        public ParseException(Throwable cause) {

            super(cause);
        }
    }

    /**
     * State of Finite-state machine. A finite state machine can be in
     * only one state at a given point in time.
     */
    private enum WaitingState {
        /**
         * State of Finite-state machine. Assume that next token from
         * {@code LexicalAnalyzer} is operand. Valid token types: NUMBER,
         * LEFT_PARENTHESISE. If token type is SUBTRACT, it means that
         * token represent unary minus.
         */
        WAITING_OPERAND,
        /**
         * State of Finite-state machine. Assume that next token from
         * {@code LexicalAnalyzer} is operator. Not valid token types:
         * LEFT_PARENTHESISE, NUMBER. Any other type is legal.
         */
        WAITING_OPERATOR,
        /**
         * State of Finite-state machine. Assume that next token from
         * {@code LexicalAnalyzer} is operand or operator.
         * Unary operators and RIGHT_PARENTHESISE are illegal.
         */
        WAITING_OPERAND_NON_UNARY_OPERATOR;

    }

    /**
     * Type of token.
     */
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

    /**
     * Operator represents mathematical operation which can be occurred
     * during statement parsing. Ich operator have operation string representing,
     * precedence and binarity sign.
     */
    public enum Operator {
        UNARY_MINUS("#", 3, false),
        MULTIPLY("*", 2, true),
        DIVIDE("/", 2, true),
        ADD("+", 1, true),
        SUBTRACT("-", 1, true),
        LEFT_PARENTHESISE("(", 0, true),
        RIGHT_PARENTHESISE(")", 0, true);
        public static Map<String, Operator> operatorMap = new HashMap<>();
        private final int precedence;
        private final String operatorCharacter;
        private final boolean isBinary;

        static {
            for (Operator operator : values()) {
                operatorMap.put(operator.operatorCharacter, operator);
            }
        }

        Operator(String operatorCharacter, int precedence, boolean isBinary) {
            this.operatorCharacter = operatorCharacter;
            this.precedence = precedence;
            this.isBinary = isBinary;
        }

        private static Operator getOperator(String token) {
            return operatorMap.get(token);
        }
    }
}

