package pl.aliberadzki.bpmnagents.knowledge;

/**
 * Created by aliberadzki on 09.05.17.
 */
public class Expression {
    private String expressionContent;

    public Expression(String expressionContent) {
        this.expressionContent = expressionContent;
    }

    public String getExpressionContent() {
        return expressionContent;
    }

    public void setExpressionContent(String expressionContent) {
        this.expressionContent = expressionContent;
    }

    public static boolean evaluate(String symbol, Comparable left, Comparable right)
    {
        switch (symbol) {
            case "==":
                return left == right;
            case "!=":
                return left != right;
            case "<=":
                return left.compareTo(right) <= 0;
            case ">=":
                return left.compareTo(right) >= 0;
            case ">":
                return left.compareTo(right) > 0;
            case "<":
                return  left.compareTo(right) < 0;
            default:
                throw new RuntimeException("BAD SYMBOL FOR EXPRESSION: " + symbol);
        }
    }
}
