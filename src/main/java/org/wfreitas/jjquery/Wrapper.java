package org.wfreitas.jjquery;

import java.util.List;

import org.wfreitas.jjquery.predicate.Predicate;

/**
 * JQuery style wrapper for java lists
 * @author wilson
 */
public interface Wrapper {
  /**
   * Updates the attribute with provided name in all wrapped list elements 
   * @param name Attribute name
   * @param value Attribute value
   * @return The wrapper
   */
  Wrapper attr(String name, Object value);
  
  /**
   * Retrieves the attribute value for the wrapped list first element
   * @param name
   * @return
   */
  Object attr(String name);
  
  Object val();
  
  Wrapper find(String query);
  
  Wrapper find(Class clazz);
  
  Wrapper find(Predicate predicate);
  
  List<Object> asList();
}