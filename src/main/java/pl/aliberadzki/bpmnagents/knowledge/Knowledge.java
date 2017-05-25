package pl.aliberadzki.bpmnagents.knowledge;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by aliberadzki on 09.05.17.
 */
public class Knowledge {
    private Map<String, Belief> beliefs = new HashMap<>();

    public void recognizeFact(String key, Object fact)
    {
        this.recognizeFact(key, fact, "unknown", false);
    }

    public void recognizeFact(String key, Object fact, String type, boolean isList)
    {
        Belief belief = new Belief(key, type, fact, isList);
        this.beliefs.put(key, belief);
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

        //TODO this part makes me sick
        left = beliefs.containsKey(left) ? Integer.valueOf((String)beliefs.get(left).getValue()) : Integer.valueOf((String)left);
        right = beliefs.containsKey(right) ? (Comparable) beliefs.get(right).getValue() : Integer.valueOf((String)right);

        return Expression.evaluate(symbol, left, right);
    }

    public Object factValue(String expressionString) {
        //TODO: it can be not only variable, but also 2+2..
        return beliefs.get(expressionString).getValue();
    }

    public static String stringifyBeliefEntry(Map.Entry<String, Belief> stringBeliefEntry) {
        return stringBeliefEntry.getKey() + "=" + stringBeliefEntry.getValue().getValue();
    }

    public static Collection<Belief> parseBeliefs(String parsedBeliefs) {
        return Arrays.stream(parsedBeliefs.split(","))
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    String[] res = s.split("=");
                    return new Belief(res[0], null, res[1], false);
                })
                .collect(Collectors.toList());
    }
}
