package org.wfreitas.jjquery.predicate;

public interface BeanAttributePredicate extends Predicate {
  boolean evaluate(String name, Object value);
}
