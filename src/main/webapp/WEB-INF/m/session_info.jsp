<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  BUser: yjh
  Date: 2015/9/7
  Time: 0:13
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>会话信息页面</title>
</head>
<body>
  <h1>会话信息列表：</h1>
  <p><b>会话ID：</b><span><%= session.getId() %></span></p>
  <p><b>会话开始时间：</b><span><%= DateUtil.dateToStr(new Date(session.getCreationTime()), "yyyy-MM-dd HH:mm:ss") %></span></p>
  <p><b>会话最近更新时间：</b><span><%= DateUtil.dateToStr(new Date(session.getLastAccessedTime()), "yyyy-MM-dd HH:mm:ss") %></span></p>
  <p><b>会话最大有效时间：</b><span><%= session.getMaxInactiveInterval() + "s" %></span></p>
  <p><b>会话是否为新创建：</b><span><%= session.isNew() %></span></p>
  <br />
  <h1>请求相关信息：</h1>
  <p><b>请求地址：</b><span><%= request.getRequestURL() + "/" + StringUtil.emptyIfBlank(request.getQueryString()) %></span></p>
  <p><b>请求方法：</b><span><%= request.getMethod() %></span></p>
  <p><b>请求MIME类型：</b><span><%= request.getContentType() %></span></p>
  <p><b>请求字符集：</b><span><%= request.getCharacterEncoding() %></span></p>
  <p><b>请求正文长度：</b><span><%= request.getContentLength() %></span></p>
</body>
</html>
