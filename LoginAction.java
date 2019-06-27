package com.internousdev.green.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.green.dao.CartInfoDAO;
import com.internousdev.green.dao.UserInfoDAO;
import com.internousdev.green.dto.CartInfoDTO;
import com.internousdev.green.dto.UserInfoDTO;
import com.internousdev.green.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{

	private String userId;
	private String password;
	private boolean userIdCheck;
	private Map<String,Object> session;
	private String userErrorMessage;
	private List<String> userIdError;
	private List<String> passwordError;
	private List<CartInfoDTO> cartInfoList;
	private String totalPrice;

	public String execute() {

		//セッションタイムアウトした場合
		if(!session.containsKey("userId") && !session.containsKey("tempUserId")){
			return "sessionTimeout";
		}

		//ユーザー登録画面から遷移した場合にユーザーIDとパスワードがsessionに入ってるため削除
		session.remove("userIdForCreateUser");
		session.remove("password");

		String result = ERROR;

		//ID保存チェックの有無により入力済みかを決定
		if(userIdCheck){
			session.put("userIdCheck", true);
			session.put("checkedUserId", userId);
		}else{
			session.remove("userIdCheck");
			session.remove("checkedUserId");
		}

		InputChecker ic = new InputChecker();
		userIdError = ic.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false, false);
		passwordError = ic.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false, false);

		if(userIdError.size() > 0 || passwordError.size() > 0){
			return result;
		}

		//ログイン認証
		UserInfoDAO userDao = new UserInfoDAO();
		if(userDao.isExistsUserInfo(userId, password)
			&& userDao.login(userId, password) > 0){
				int logined = userDao.login(userId, password);

			//カートの情報を紐付ける
			String tempUserId = session.get("tempUserId").toString();
			CartInfoDAO cartDao = new CartInfoDAO();
			List<CartInfoDTO> listForTemp = cartDao.getCartInfoList(tempUserId);

			//カート情報を保持している場合
			if(!listForTemp.isEmpty()){
				boolean cartResult = changeCart(listForTemp, tempUserId);

				//紐づけに失敗した場合
				if(!cartResult || logined <= 0){
					return "DBError";
				}

			//カート情報を保持していない場合
			}else{
				cartInfoList = cartDao.getCartInfoList(userId);
				int intTotalPrice = 0;
				for(int n=0; n<cartInfoList.size(); n++) {
					intTotalPrice += Integer.parseInt(cartInfoList.get(n).getProductPrice());
				}
				totalPrice = String.valueOf(intTotalPrice);
			}

			//遷移先を決定
			if(session.containsKey("fromCart")){
				session.remove("fromCart");
				result = "cart";
			}else{
				result = SUCCESS;
			}

			//ユーザー情報をsessionに登録
			UserInfoDTO userDto = userDao.getUserInfo(userId, password);
			session.put("userId", userDto.getUserId());
			session.put("loginFlg", 1);

		}else{
			setUserErrorMessage("ユーザーIDまたはパスワードが異なります。");
			session.put("userId", userId);
		}
		return result;
	}

	//DBのカート情報を更新・作成
	private boolean changeCart(List<CartInfoDTO> listForTemp, String tempUserId) {
		int count = 0;
		CartInfoDAO cartDao = new CartInfoDAO();
		boolean result = false;

		for(CartInfoDTO cartDto : listForTemp){
			//session(画面に表示されている)カート情報と同じ商品情報がDBに存在するかチェック
			if(cartDao.isExistsCartInfo(userId, cartDto.getProductId())){
				//存在する場合、購入個数を更新し仮ユーザーIDのデータを削除
				count += cartDao.updateProductCount(userId, cartDto.getProductId(), cartDto.getProductCount());
				cartDao.deleteTempUserCartInfo(tempUserId, String.valueOf(cartDto.getProductId()));
			}else{
				//存在しない場合、tempUserIdをuserIdに変更
				count += cartDao.updateCartInfoUserId(userId, tempUserId, cartDto.getProductId());
			}
		}
		if(count == listForTemp.size()){
			cartInfoList = cartDao.getCartInfoList(userId);
			int intTotalPrice = 0;
			for(int n=0; n<cartInfoList.size(); n++) {
				intTotalPrice += Integer.parseInt(cartInfoList.get(n).getProductPrice());
			}
			totalPrice = String.valueOf(intTotalPrice);
			result = true;
		}
		return result;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public boolean getUserIdCheck(){
		return userIdCheck;
	}

	public void setUserIdCheck(boolean userIdCheck){
		this.userIdCheck = userIdCheck;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	@Override
	public void setSession(Map<String,Object> session){
		this.session = session;
	}

	public String getUserErrorMessage(){
		return userErrorMessage;
	}

	public void setUserErrorMessage(String userErrorMessage){
		this.userErrorMessage = userErrorMessage;
	}

	public List<String> getUserIdError(){
		return userIdError;
	}

	public void setUserIdError(List<String> userIdError){
		this.userIdError = userIdError;
	}

	public List<String> getPasswordError(){
		return passwordError;
	}

	public void setPasswordError(List<String> passwordError){
		this.passwordError = passwordError;
	}

	public List<CartInfoDTO> getCartInfoList(){
		return cartInfoList;
	}

	public void setCartInfoList(List<CartInfoDTO> cartInfoList){
		this.cartInfoList = cartInfoList;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

}
