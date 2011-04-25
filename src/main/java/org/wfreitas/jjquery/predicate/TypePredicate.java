package org.wfreitas.jjquery.predicate;

public class TypePredicate implements ObjectPredicate {

  private Class typeToCheck;
  
  public TypePredicate(Class clazz){
    typeToCheck = clazz;
  }
  
  @Override
  public boolean evaluate(Object value) {
    return typeToCheck.isAssignableFrom(value.getClass());
  }
  
}
