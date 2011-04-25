package org.wfreitas.jjquery.predicate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class AttributeMatchPredicate implements BeanAttributePredicate {

  private String attributeName;
  private String operator;
  private String operand;
  public static final Pattern QUERY_PATTERN = Pattern.compile("\\[(.+)\\]");
  private static final Pattern INNER_ATTRIBUTE_PATTERN = Pattern.compile("(\\w+)\\s*?([\\^\\$\\*!]?=)\\s*'([\\w\\s]+)");
  
  private static final String OPERATOR_EQUALS = "=";
  private static final String OPERATOR_CONTAINS = "*=";
  private static final String OPERATOR_ENDS_WITH = "$=";
  private static final String OPERATOR_STARTS_WITH = "^=";
  private static final String OPERATOR_NOT_EQUAL = "!=";
  
  public AttributeMatchPredicate(String query){
    Matcher matcher = QUERY_PATTERN.matcher(query);
    if(matcher.find()){
      String expression = matcher.group(1);
      Matcher expressionMatcher = INNER_ATTRIBUTE_PATTERN.matcher(expression);
      if(expressionMatcher.find()){
        this.attributeName = expressionMatcher.group(1);
        this.operator = expressionMatcher.group(2);
        this.operand = expressionMatcher.group(3);
      }else{
        this.attributeName = expression;
      }
    }
  }
  
  @Override
  public boolean evaluate(String name, Object value) {
    boolean result = false;
    if(StringUtils.isNotBlank(attributeName) && attributeName.equals(name)){
      if(StringUtils.isNotBlank(operator) && StringUtils.isNotBlank(operand)){
        if(OPERATOR_EQUALS.equals(operator)){
          if(value instanceof Number){
            Number numberOperand = NumberUtils.createNumber(operand);
            result = numberOperand.intValue() == ((Number)value).intValue();
          }else{
            result = operand.equals(value);
          }
          
        }else if(OPERATOR_CONTAINS.equals(operator)){
          checkForStringOperand(value, OPERATOR_CONTAINS);
          result = operand.indexOf((String)value) >= 0;
          
        }else if(OPERATOR_STARTS_WITH.equals(operator)){
          checkForStringOperand(value, OPERATOR_STARTS_WITH);
          result = operand.startsWith((String)value);
          
        }else if(OPERATOR_ENDS_WITH.equals(operator)){
          checkForStringOperand(value, OPERATOR_ENDS_WITH);
          result = operand.endsWith((String)value);
          
        }else if(OPERATOR_NOT_EQUAL.equals(operator)){
          checkForStringOperand(value, OPERATOR_NOT_EQUAL);
          result = !operand.equals((String)value);
        }
      }else{
        result = true;
      }
    }
    return result;
  }

  private void checkForStringOperand(Object value, String operator) {
    if(!(value instanceof String)){
      throw new IllegalArgumentException(
        String.format(
          "Operator %s only supports String type operands. Value %s is invalid"
          ,operator
          ,value != null ? value.toString() : "Null"
        )
      );
    }
  }

}