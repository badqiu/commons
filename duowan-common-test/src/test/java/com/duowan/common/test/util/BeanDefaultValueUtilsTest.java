package com.duowan.common.test.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.time.DateFormatUtils;

import com.duowan.common.test.util.testbean.Bean1;
import com.duowan.common.test.util.testbean.SexEnumBean;

public class BeanDefaultValueUtilsTest extends TestCase {
	 
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        BeanAssert.assertPropertiesNull(bean,"char1","int1");
        
        //为 bean所有属性设置默认值，相当于 test 所有 set方法
        BeanDefaultValueUtils.setBeanProperties(bean);
        
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getInteger1(),new Integer(1));
        assertEquals(bean.getLong1(),new Long(1));
        assertEquals(DateFormatUtils.format(bean.getSqldate1(),"yyyyMMddHHmmss"),DateFormatUtils.format(new java.sql.Date(System.currentTimeMillis()),"yyyyMMddHHmmss"));
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getSex(),SexEnumBean.F);
        assertEquals(bean.getChar1(),'1');
        assertNotNull(bean.getArrayList());
        assertNotNull(bean.getListLong());
        assertNotNull(bean.getMap());
        assertNotNull(bean.getHashMap());
        assertNotNull(bean.getTestBean());
        assertNotNull(bean.getSet());
        assertNotNull(bean.getHashSet());
        assertNotNull(bean.getCollection());
        assertNotNull(bean.getStack());
        
        //检查 bean所有属性不为空，相当于 test 所有 get方法
        BeanAssert.assertPropertiesNotNull(bean);
    }
    
    public void test_not_empty() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        BeanAssert.assertPropertiesEmpty(bean,"char1","int1");
        BeanDefaultValueUtils.setBeanProperties(bean);
        BeanAssert.assertPropertiesNotEmpty(bean,"arrayList","set","collection","hashMap","hashSet","map","listLong","stack");

        
    }
    
    public void testConstructor() {
        ConstructorParentBean parent = new ConstructorParentBean();
        BeanDefaultValueUtils.setBeanProperties(parent);
        assertEquals(parent.getChild().getAge(),1);
        assertEquals(parent.getChild().getSex(),new Integer(1));
        assertEquals(parent.getChild().getName(),"1");
//        assertEquals(parent.getChild().getNikenames().get(0),"1"); // FIXME 增加对 List<>的默认参数设置
        
        assertEquals(parent.getPrivateChild(),null);
    }

    public static class ConstructorParentBean {
        ConstructorChildBean child;
        ConstructorPrivateChildBean privateChild;
        public ConstructorChildBean getChild() {
            return child;
        }

        public void setChild(ConstructorChildBean child) {
            this.child = child;
        }

        public ConstructorPrivateChildBean getPrivateChild() {
            return privateChild;
        }

        public void setPrivateChild(ConstructorPrivateChildBean privateChild) {
            this.privateChild = privateChild;
        }
    }
    
    public static class ConstructorChildBean {
        private int age = 0;
        private Integer sex = null;
        private String name = "";
        private List<String> nikenames;
        
        public ConstructorChildBean(int age, Integer sex, String name) {
            super();
            this.age = age;
            this.sex = sex;
            this.name = name;
        }

        public ConstructorChildBean(int age, Integer sex) {
            super();
            this.age = age;
            this.sex = sex;
        }
        
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

		public List<String> getNikenames() {
			return nikenames;
		}

		public void setNikenames(List<String> nikenames) {
			this.nikenames = nikenames;
		}
        
    }
    
    public static class ConstructorPrivateChildBean {
        private int age = 0;
        private Integer sex = null;
        private String name = "";
        
        private ConstructorPrivateChildBean(int age, Integer sex, String name) {
            super();
            this.age = age;
            this.sex = sex;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
    }
    
}
