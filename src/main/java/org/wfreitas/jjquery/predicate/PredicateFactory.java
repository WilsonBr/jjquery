package org.wfreitas.jjquery.predicate;

public class PredicateFactory {
  public static Predicate getPredicate(Object query){
    Predicate predicate = null;
    if(query instanceof Class){
      predicate = new TypePredicate((Class) query);
    }else if(query instanceof String){
      if(AttributeMatchPredicate.QUERY_PATTERN.matcher((String)query).find()){
        predicate = new AttributeMatchPredicate((String)query);
      }
    }
      
    return predicate;
  }
}
