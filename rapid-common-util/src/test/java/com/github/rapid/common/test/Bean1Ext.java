package com.github.rapid.common.test;

public interface Bean1Ext {
	
	default Bean1 self() {
		return (Bean1) this;
	}
	
    default String getFullName() {
    	Bean1 self = self();
        return self.getS1() + " " + self.getLong1();
    }
    
}
