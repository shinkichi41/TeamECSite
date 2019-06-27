package com.internousdev.green.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.green.dao.MCategoryDAO;
import com.internousdev.green.dto.MCategoryDTO;
import com.internousdev.green.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport implements SessionAware{

	private Map<String,Object> session;

	public String execute(){

		//未ログイン、仮ユーザーIDを保持していない場合、仮ユーザーIDを与える
		if(!session.containsKey("tempUserId") && !session.containsKey("loginFlg")){
			CommonUtility cu = new CommonUtility();
			session.put("tempUserId", cu.getRamdomValue());
		}

		//カテゴリーリストを取得
		if(!session.containsKey("mCategory")){
			List<MCategoryDTO> mCategory = new ArrayList<MCategoryDTO>();
			MCategoryDAO dao = new MCategoryDAO();
		try{
			mCategory = dao.getCategoryInfo();
		}catch(NullPointerException e){
			mCategory = null;
		}
		session.put("mCategory", mCategory);
		}
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