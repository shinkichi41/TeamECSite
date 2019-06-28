<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ログイン画面</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<h1>ログイン画面</h1>
	<div>
		<s:if test="userIdError != null && userIdError.size() > 0">
			<div class="errorRed">
				<s:iterator value="userIdError" ><s:property/><br></s:iterator>
			</div>
		</s:if>

		<s:if test="passwordError != null && passwordError.size() > 0">
			<div class="errorRed">
				<s:iterator value="passwordError"><s:property/><br></s:iterator>
			</div>
		</s:if>

		<s:if test="userErrorMessage != null">
			<div class="errorRed">
				<s:property value="userErrorMessage"/><br>
			</div>
		</s:if>
	</div>

	<div>
		<s:form action="LoginAction">
			<table class="box">
				<tr>
					<th>ユーザーID</th>
					<s:if test="#session.userIdCheck == true">
						<td><s:textfield name="userId" value="%{#session.checkedUserId}" class="txt" id="txt_login"/></td>
					</s:if>
					<s:else>
						<td><s:textfield name="userId" class="txt" id="txt_login"/></td>
					</s:else>
				</tr>
				<tr>
					<th>パスワード</th>
					<td><s:password name="password" class="txt" id="txt_login"/></td>
				</tr>
			</table>

			<div class="checkbox">
				<s:if test="#session.userIdCheck == true && #session.checkedUserId != null && !#session.checkedUserId.isEmpty()">
					<s:checkbox name="userIdCheck" checked="checked"/>
				</s:if>
				<s:else>
					<s:checkbox name="userIdCheck"/>
				</s:else>
				<span id="checked">ユーザーID保存</span>
			</div>

			<div class="button_pos">
				<s:submit value="ログイン" class="button"/>
			</div>
		</s:form>

		<s:form action="CreateUserAction">
			<div class="button_pos">
				<s:submit value="新規ユーザー登録" class="button"/>
			</div>
		</s:form>

		<s:form action="ResetPasswordAction">
			<div class="button_pos">
				<s:submit value="パスワード再設定" class="button"/>
			</div>
		</s:form>
	</div>
</body>
</html>
