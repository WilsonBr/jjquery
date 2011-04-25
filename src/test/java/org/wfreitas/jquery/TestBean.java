package org.wfreitas.jquery;

public class TestBean {
  private String name;
  private TestInnerBean innerBean;
  private Integer beanId;

  public TestBean(String name) {
    this(name, 0);
  }

  public TestBean(String name, Integer beanId) {
    this.name = name;
    this.innerBean = new TestInnerBean(name);
    this.beanId = beanId;
  }
  
  public TestInnerBean getInnerBean() {
    return innerBean;
  }

  public void setInnerBean(TestInnerBean innerBean) {
    this.innerBean = innerBean;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getBeanId() {
    return beanId;
  }

  public void setBeanId(Integer beanId) {
    this.beanId = beanId;
  }

  public class TestInnerBean{
    private String name;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public TestInnerBean(String nameFromOuter){
      name = "Inner -> " + nameFromOuter;
    }
  }  
}