package pl.aliberadzki.bpmnagents.knowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aliberadzki on 09.05.17.
 */
public class Knowledge {
    //TODO: use beliefs here
    private Map<String, Object> facts = new HashMap<>();

    public void recognizeFact(String key, Object fact)
    {
        this.facts.put(key, fact);
    }

    public boolean evaluateExpression(Expression expression)
    {
        String[] symbols = new String[] {"==", ">=", "<=", "!=", ">", "<"};

        Pattern pattern = Pattern.compile("(.*)("+ String.join("|", symbols) +")(.*)");
        Matcher matcher = pattern.matcher(expression.getExpressionContent());
        matcher.find();
        Comparable left = matcher.group(1);
        String symbol = matcher.group(2);
        Comparable right = matcher.group(3);

        left = facts.containsKey(left) ? (Comparable) facts.get(left) : Integer.valueOf((String)left);
        right = facts.containsKey(right) ? (Comparable) facts.get(right) : Integer.valueOf((String)right);

        return Expression.evaluate(symbol, left, right);
    }

    public Object factValue(String expressionString) {
        //TODO: it can be not only variable, but also 2+2..
        return facts.get(expressionString);
    }
}
