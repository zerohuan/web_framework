<c:if test="${param.containsKey('loginFailed')}">
	<b class="errors">登录失败</b><br />
</c:if><c:if test="${param.containsKey('loggedOut')}">
	<i>已注销</i><br /><br />
</c:if>
<br /><br />
<form:form method="post" modelAttribute="loginForm" autocomplete="off">
	<form:label path="username">手机号</form:label><br />
	<form:input path="username" autocomplete="off" /><br />
	<form:label path="password">密码</form:label><br />
	<form:password path="password" autocomplete="off" /><br />
	<input type="submit" value="登录" />
</form:form>