<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 2015/9/10
  Time: 23:08
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.yjh.base.site.model.BookTest,java.time.DayOfWeek,java.util.List" %>
<%@ page import="java.util.ArrayList,java.util.Date" %>

<fmt:setLocale value="zh_CN" />

<%
    List<BookTest> books = new ArrayList<>();
    books.add(new BookTest("Thinking in Java", 89.9, 3));
    books.add(new BookTest("Java Design Pattern", 99.9, 3));
    books.add(new BookTest("Java Network Programing", 99.9, 3));
    session.setAttribute("books", books);
%>

<html>
<head>
    <title>Java标准标签库</title>
</head>
<body>
    <h1>核心标签库</h1>
    <p><c:out value="${'<c:out>示例输出：<xml />'}" /></p>
    <p><c:out value="<c:url>示例" />：
        <a href="<c:url value="/m/el">
                    <c:param name="name" value="name1" />
                    <c:param name="name" value="name2" />
            </c:url>">
            EL演示页
        </a>
    </p>
    <p><c:out value="<c:if>示例" /></p>
    <c:if test="${true}" var="ifTest" scope="request">
        <p>You can see if content.</p>
    </c:if>

    <c:if test="${ifTest}">
        <p>You can see ifTest again.</p>
    </c:if>

    <p><c:out value="<c:forEach>示例" /></p>
    <c:forEach var="i" begin="0" end="10" step="3">
        Line ${i}<br />
    </c:forEach>

    <c:forEach items="${books}" var="book" varStatus="status">
        <p>${book.name}&nbsp;${book.price}&nbsp;${book.count}</p>
        <p>
            迭代状态：<br />
            ${status.step}
            ${status.begin}
            ${status.end}
            ${status.count}
            ${status.count}
            ${status.first}
            ${status.first}
            ${status.index}
            ${status.last}
        </p>
    </c:forEach>
    <fmt:message key="str_username" />
    <%
        request.setAttribute("date1", new Date());
        request.setAttribute("price", 123.647);
    %>
    <br />
    <fmt:formatDate value="${date1}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /><br />
    <fmt:formatNumber value="${price}" type="currency" currencyCode="USD" />
    <p></p>
</body>
</html>
