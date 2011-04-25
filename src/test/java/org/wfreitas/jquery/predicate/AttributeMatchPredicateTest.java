package org.wfreitas.jquery.predicate;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.wfreitas.jjquery.predicate.AttributeMatchPredicate;
import org.wfreitas.jjquery.predicate.BeanAttributePredicate;
import org.wfreitas.jjquery.predicate.PredicateFactory;

public class AttributeMatchPredicateTest {
  
  @Test
  public void nameOnlyAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name]");
    
    Assert.assertTrue(predicate.evaluate("name", "bla"));
    Assert.assertFalse(predicate.evaluate("nome", "bla"));
    
  }
  
  @Test
  public void equalsAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name = 'bla']");
    
    Assert.assertTrue(predicate.evaluate("name", "bla"));
    Assert.assertFalse(predicate.evaluate("name", "blo"));
    Assert.assertFalse(predicate.evaluate("nome", "bla"));
  }

  @Test
  public void numberEqualsAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[value = '3']");
    
    Assert.assertTrue(predicate.evaluate("value", 3));
    Assert.assertTrue(predicate.evaluate("value", new Integer(3)));
    Assert.assertTrue(predicate.evaluate("value", new Long(3)));
    Assert.assertTrue(predicate.evaluate("value", BigInteger.valueOf(3)));
  }

  @Test
  public void containsAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name *= 'Test string']");
    
    Assert.assertTrue(predicate.evaluate("name", "est str"));
    Assert.assertTrue(predicate.evaluate("name", "Test"));
    Assert.assertTrue(predicate.evaluate("name", "string"));
    Assert.assertFalse(predicate.evaluate("name", "bla"));
    Assert.assertFalse(predicate.evaluate("nome", "Test"));
  }
  
  @Test
  public void startsWithAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name ^= 'Test string']");
    
    Assert.assertTrue(predicate.evaluate("name", "Test"));
    Assert.assertTrue(predicate.evaluate("name", "Test str"));
    Assert.assertFalse(predicate.evaluate("name", "est"));
    Assert.assertFalse(predicate.evaluate("nome", "Test"));
  }

  @Test
  public void endsWithAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name $= 'Test string']");
    
    Assert.assertTrue(predicate.evaluate("name", "string"));
    Assert.assertTrue(predicate.evaluate("name", "st string"));
    Assert.assertFalse(predicate.evaluate("name", "string "));
    Assert.assertFalse(predicate.evaluate("nome", "string"));
  }

  @Test
  public void notEqualsAttributeMatch(){
    BeanAttributePredicate predicate = getPredicate("[name != 'Test']");
    
    Assert.assertTrue(predicate.evaluate("name", "bla"));
    Assert.assertFalse(predicate.evaluate("name", "Test"));
  }
  
  private BeanAttributePredicate getPredicate(String query){
    BeanAttributePredicate predicate = (BeanAttributePredicate) PredicateFactory.getPredicate(query);
    Assert.assertTrue(predicate instanceof AttributeMatchPredicate);
    
    return predicate;
  }
}