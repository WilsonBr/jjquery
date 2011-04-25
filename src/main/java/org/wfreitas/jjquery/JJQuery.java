package org.wfreitas.jjquery;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfreitas.jjquery.predicate.BeanAttributePredicate;
import org.wfreitas.jjquery.predicate.ObjectPredicate;
import org.wfreitas.jjquery.predicate.Predicate;
import org.wfreitas.jjquery.predicate.PredicateFactory;

public class JJQuery implements Wrapper {
  private Collection<Object> wrappedOjects;
  private static Log LOG = LogFactory.getLog(JJQuery.class);
  
  public static Wrapper newInstance(Object objectGraph){
    return new JJQuery(objectGraph);
  }
  
  private JJQuery(Object objectGraph){
    if(objectGraph instanceof Collection){
      wrappedOjects = (Collection<Object>)objectGraph;
    }else{
      wrappedOjects = new ArrayList<Object>();
      wrappedOjects.add(objectGraph);
    }
  }
  
  public Wrapper attr(String name, Object value) {
    for(Object wo : wrappedOjects){
      try {
        BeanUtils.setProperty(wo, name, value);
      } catch (Exception e) {
        LOG.warn("Invalid property " + name + " for " + wo.getClass());
      }
    }
    return this;
  }

  public Object attr(String name) {
    Object result = null;
    
    if(!wrappedOjects.isEmpty()){
      try {
        result = BeanUtils.getProperty(wrappedOjects.iterator().next(), name);
      } catch (Exception e) {
        LOG.warn("Invalid property " + name + " for " + wrappedOjects.iterator().next().getClass());
      }
    }
    return result;
  }

  @Override
  public Object val() {
    Object result = null;
    if(!wrappedOjects.isEmpty()){
      result = wrappedOjects.iterator().next();
    }
    return result;
  }

  @Override
  public Wrapper find(String query) {
    return JJQuery.newInstance(
      traverseGraphAndApplyPredicate(wrappedOjects, PredicateFactory.getPredicate(query))
    );
  }

  @Override
  public Wrapper find(final Class clazz) {
    return JJQuery.newInstance(
      traverseGraphAndApplyPredicate(wrappedOjects, PredicateFactory.getPredicate(clazz))
    );
  }
  
  @Override
  public Wrapper find(Predicate predicate) {
    return JJQuery.newInstance(
      traverseGraphAndApplyPredicate(wrappedOjects, predicate)
    );
  }

  @Override
  public List<Object> asList() {
    return new ArrayList<Object>(wrappedOjects);
  }

  // PRIVATE
  private List<Object> traverseGraphAndApplyPredicate(Object graph, Predicate predicate){
    List<Object> result = new ArrayList<Object>();
    
    if(graph instanceof Collection){
      for(Object item : (Collection)graph){
        result.addAll(traverseGraphAndApplyPredicate(item, predicate));
      }
      
    // Java bean - Traverse properties
    }else{
      
      if(predicate instanceof ObjectPredicate && ((ObjectPredicate)predicate).evaluate(graph)){
        result.add(graph);
      }else{
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(graph.getClass());
        for(PropertyDescriptor descriptor : props){
          try {
            if(!"class".equals(descriptor.getName())){
              Object value = PropertyUtils.getProperty(graph, descriptor.getName());
              if(predicate instanceof BeanAttributePredicate && ((BeanAttributePredicate)predicate).evaluate(descriptor.getName(), value)){
                result.add(graph);
              }else{
                result.addAll(
                  traverseGraphAndApplyPredicate(value, predicate)
                );
              }
            }
          } catch (Exception e) {
            LOG.warn("Error getting descriptors for class " + graph.getClass());
          }
        }
      }
    }
    return result;
  }

}