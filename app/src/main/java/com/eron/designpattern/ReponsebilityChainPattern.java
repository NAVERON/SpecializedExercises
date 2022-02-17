package com.eron.designpattern;


/**
 * 责任链模式
 * @author eron
 */
public class ReponsebilityChainPattern {
    
    // 接口或者虚类, 实现同一方法, 设置下一个接口实现
    public static abstract class Handler {
	
	private int handleLevel = 0;
	private Handler nextHandler = null;
	
	public int getLevel() {
	    return this.handleLevel;
	}
	
	public void setNexthandle (Handler handle) {
	    this.nextHandler = handle;
	}
	
	public void preCheck(String request, int level) {
	    // 检查层级
	    if( level == this.getLevel()) {
		this.doHandle(request);
	    }else {
		if (nextHandler != null) {
		    nextHandler.preCheck(request, level + 1);
		}else {
		    new Exception("Error");
		}
	    }
	}
	
	public void doHandle (String request) {
	    // 具体的操作一些动作
	}
    }
    
    public static class ConcretHandler extends Handler {

	@Override
	public void preCheck(String request, int level) {
	    // TODO Auto-generated method stub
	    super.preCheck(request, level);
	    this.doHandle(request);
	}

	@Override
	public void doHandle(String request) {
	    // TODO Auto-generated method stub
	    super.doHandle(request);
	}
    }
}














