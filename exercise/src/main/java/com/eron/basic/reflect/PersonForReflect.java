package com.eron.basic.reflect;

import com.eron.basic.annotation.TestUsage;

public class PersonForReflect implements MiniAPI {
	
	private Long id;
	private String name;
	public Integer count;
	protected Float score;
	
	public PersonForReflect() {}
	
	public PersonForReflect(String name) {
		this.name = name;
	}
	
	@TestUsage(age = 0, name = "?") 
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	private Integer getCount() {
		return count;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	private void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "PersonForReflect [id=" + id + ", name=" + name + ", count=" + count + ", score=" + score + "]";
	}
	
}











