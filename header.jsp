<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./css/style.css">
	<title>ヘッダー</title>
</head>
<body>
	<div id="all">
		<div id="form">
			<div id="name">
				Green
			</div>
		</div>

		<s:form action="SearchItemAction">
			<div id="form">
				<s:if test="#session.mCategory != null && #session.mCategory.size() > 0">
					<s:select name="categoryId" list="#session.mCategory" listValue="categoryName" listKey="categoryId" class="search"/>
				</s:if>
			</div>
			<div id="form">
				<s:textfield name="keywords" value="%{keywords}" placeholder="検索ワード" id="keyword"/>
			</div>
			<div id="form">
				<s:submit value="商品検索" class="button"/>
			</div>
		</s:form>

		<div id="form">
			<s:if test="#session.loginFlg == 1">
				<s:form action="LogoutAction">
					<s:submit value="ログアウト" class="button"/>
				</s:form>
			</s:if>
		</div>

		<div id="form">
			<s:else>
				<s:form action="GoLoginAction">
					<s:submit value="ログイン" class="button"/>
				</s:form>
			</s:else>
		</div>

		<div id="form">
			<s:form action="CartAction">
				<s:submit value="カート" class="button"/>
			</s:form>
		</div>

		<div id="form">
			<s:form action="ProductListAction">
				<s:submit value="商品一覧" class="button"/>
			</s:form>
		</div>

		<div id="form">
			<s:if test="#session.loginFlg == 1">
				<s:form action="MyPageAction">
					<s:submit value="マイページ" class="button"/>
				</s:form>
			</s:if>
		</div>
	</div>
</body>
</html>