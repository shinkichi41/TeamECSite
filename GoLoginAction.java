package com.internousdev.green.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GoLoginAction extends ActionSupport implements SessionAware{

	private Map<String,Object> session;

	public String execute(){

		//セッションタイムアウトした場合
		if(!session.containsKey("userId") && !session.containsKey("tempUserId")){
			return "sessionTimeout";
		}

		session.remove("fromCart");
		return SUCCESS;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	@Override
	public void setSession(Map<String,Object> session){
		this.session = session;
	}

}
