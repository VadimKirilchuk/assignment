package exercises.interviewTasks.tsystems.Calculator2;

/**
 * Created by Андрей on 03.05.2015.
 */

/**
 * Interface for Calculator task
 */
public interface Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     *
     * @return string value containing result of evaluation or null if statement is invalid
     */
    String evaluate(String statement);

}