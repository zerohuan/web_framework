<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-9-25
  Time: 下午2:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
  <title>
    tomcat 连接池
  </title>
</head>
<body bgcolor="#ffffff">
<h3>
  test
  <br>
  连接池:
</h3>
<%try {

  Context initCtx = new InitialContext();
  Context envCtx = (Context) initCtx.lookup("java:comp/env");
  DataSource ds = (DataSource) envCtx.lookup("jdbc/cg");


  Connection conn = ds.getConnection();
  Statement stmt = conn.createStatement();
  ResultSet rs = stmt.executeQuery("select * from b_user");
  while (rs.next()) {%>

<br>
<%=rs.getString(2)%>
<%}%>
<c:out value="success<br />" />
<%rs.close();
  stmt.close();
  conn.close();
} catch (Exception e) {
  e.printStackTrace();
}

%>

</body>
</html>
