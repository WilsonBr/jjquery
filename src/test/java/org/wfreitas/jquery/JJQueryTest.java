package org.wfreitas.jquery;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.wfreitas.jjquery.JJQuery;
import org.wfreitas.jjquery.Wrapper;
import org.wfreitas.jjquery.predicate.BeanAttributePredicate;
import org.wfreitas.jjquery.predicate.ObjectPredicate;

public class JJQueryTest {

  @Test
  public void simpleAttrSet(){
    TestBean bean = new TestBean("Test");
    String testValue = "Test value";
    Wrapper wrapper = JJQuery.newInstance(bean).attr("name", testValue);
    Assert.assertEquals(testValue, wrapper.attr("name"));
    Assert.assertEquals(testValue, bean.getName());
  }

  @Test
  public void listAttrSet(){
    List<TestBean> testBeans = getTestBeanList();
    
    Wrapper wrapper = JJQuery.newInstance(testBeans);
    String testValue = "Test value";
    wrapper.attr("name", testValue);
    
    for(TestBean bean : testBeans){
      Assert.assertEquals(testValue, bean.getName());
    }
    
    Assert.assertEquals(testValue, wrapper.attr("name"));
  }

  private List<TestBean> getTestBeanList() {
    List<TestBean> testBeans = new ArrayList<TestBean>();
    testBeans.add(new TestBean("bean1", 1));
    testBeans.add(new TestBean("bean2", 2));
    testBeans.add(new TestBean("bean3", 3));
    return testBeans;
  }
  
  @Test
  public void findByType(){
    List<TestBean> testBeans = getTestBeanList();
    Wrapper wrapper = JJQuery.newInstance(testBeans);
    
    for(Object item : wrapper.find(String.class).asList()){
      Assert.assertTrue(item instanceof String);
    }
    
    Assert.assertTrue(wrapper.find(TestBean.class).val() instanceof TestBean);
    Assert.assertTrue(wrapper.find(TestBean.TestInnerBean.class).val() instanceof TestBean.TestInnerBean);
  }

  @Test
  public void findByQuery() throws Exception{
    List<TestBean> testBeans = getTestBeanList();
    Wrapper wrapper = JJQuery.newInstance(testBeans);
    
    List<Object> queryResult = wrapper.find("[name]").asList();
    Assert.assertTrue(!queryResult.isEmpty());
    
    for(Object item : queryResult){
      Assert.assertNotNull(PropertyUtils.getPropertyDescriptor(item, "name"));
    }
    
    Assert.assertTrue(wrapper.find("[bla]").asList().isEmpty());
  }
  
  @Test
  public void findByCustomObjectPredicate() throws Exception{
    List<TestBean> testBeans = getTestBeanList();
    Wrapper wrapper = JJQuery.newInstance(testBeans);
    
    List result = wrapper.find(Integer.class).find(new ObjectPredicate() {
      @Override
      public boolean evaluate(Object value) {
        return ((Integer)value) > 1;
      }
    }).asList();
    
    for(Object item : result){
      Assert.assertTrue(item instanceof Integer);
      Assert.assertTrue(((Integer)item) > 1);
    }
  }  
  
  @Test
  public void findByCustomBeanAttrPredicate() throws Exception{
    List<TestBean> testBeans = getTestBeanList();
    Wrapper wrapper = JJQuery.newInstance(testBeans);
    
    List result = wrapper.find(TestBean.class).find(new BeanAttributePredicate() {
      
      @Override
      public boolean evaluate(String name, Object value) {
        return ((TestBean)value).getBeanId() > 1;
      }
    }).asList();
    
    for(Object item : result){
      Assert.assertTrue(item instanceof TestBean);
      Assert.assertTrue(((TestBean)item).getBeanId() > 1);
    }
  }  
}