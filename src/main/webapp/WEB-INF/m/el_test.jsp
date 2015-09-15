<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 2015/9/8
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.yjh.base.model.BookTest,java.time.DayOfWeek,java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.yjh.base.util.CollectionUtil,java.util.Arrays" %>

<jsp:useBean id="day" class="com.yjh.base.model.TestBean" scope="page" />

<%
    //EL表达式可以直接访问day1,day2变量
    request.setAttribute("day1", DayOfWeek.SATURDAY);
    application.setAttribute("day2", DayOfWeek.FRIDAY);
    List<BookTest> books = new ArrayList<>();
    books.add(new BookTest("Thinking in Java", 89.9, 3));
    books.add(new BookTest("Java Design Pattern", 99.9, 3));
    books.add(new BookTest("Java Network Programing", 99.9, 3));
    session.setAttribute("books", books);
%>

<%--@elvariable id="day1" type="java.time.DayOfWeek" --%>

<html>
<head>
    <title>EL表达式使用</title>
</head>
<body>
    <h1>保留字</h1>
    <p>true false null instanceof empty div mod and or not eq ne le ge lt gt </p>
    <h1>字面量</h1>
    <p>双引号：${"This is a sentence."}</p>
    <p>单引号：${'This is a sentence.'}</p>
    <%--<p>使用+=运算符：${"This is one " += "xxxx" += ", and this is anotner " += "xxxx."}</p>--%>
    <p>int：${105}</p>
    <p>long：${-132147483648}</p>
    <%--<p>bigInteger：${139223372036854775807}</p>--%>
    <h1>EL函数</h1>
    <p>length函数：${fn:length("xx")}</p>
    <h1>访问静态字段和静态方法</h1>
    <p>Integer.MAX_VALUE：${Integer.MAX_VALUE}</p>
    <h1>枚举</h1>
    <p>枚举变量可以自动转化为字符串：${day2}</p>
    <h1>lambda表达式（匿名函数）</h1>
    <p>lambda表达式1结果：${(a -> a + 5)(4)}</p>
    <p>lambda表达式2结果：${((a, b) -> a + b)(4, 7)}</p>
    <p>lambda表达式3结果：${v = (a, b) -> a + b; v(3, 4)}</p>
    <h1>EL访问作用域变量</h1>
    <p>查看请求参数name：${paramValues["name"] == null ? "无name参数":Arrays.toString(paramValues["name"])}</p>
    <p>查看请求头：${CollectionUtil.printStrsMap(headerValues)}</p>
    <p>请求地址：${pageContext.request.remoteAddr}</p>
    <p>请求URL：${pageContext.request.requestURL}</p>
    <h1>流式操作</h1>
    <p>过滤流：${books.stream().filter(b -> b.author == "Thinking in Java")}</p>
    <p>Distinct函数使用：${[1,2,3,3,4,5,5,6,7,8,9,10].stream().distinct().toList()}</p>
    <p>操作遍历：${books.stream().forEach(b -> b.setCount(1))}</p>
    <p>对流进行排序：${books.stream().sorted((b1, b2) -> b1.price.compareTo(b2.price)).toList()}</p>
    <p>直接调用sorted排序实现了Comparable接口的元素：${Arrays.toString([8,3,19,5,7,-2,0].stream().sorted().toArray())}</p>
    <p>限制流（分页）：${Arrays.toString(books.stream().limit(10).toArray())}</p>
    <p>转换流1：${Arrays.toString(books.stream().map(b -> b.name).toArray())}</p>
    <p>转换流2：${Arrays.toString(books.stream().map(b -> [b.name, b.price]).toArray())}</p>
    <p>转换流2：${Arrays.toString(books.stream().map(b -> {"title":b.name, "price":b.price}).toArray())}</p>
    <p><b>使用终结操作：返回集合和数组</b></p>
    <p>两个重要方法：toArray(), toList()</p>
    <p>使用聚集函数：</p>
    <p>最小值：${books.stream().map(b->b.price).min()}</p>
    <p>最大值：${books.stream().map(b->b.price).max()}</p>
    <p>平均值：${books.stream().map(b->b.price).average()}</p>
    <p>计数：${books.stream().filter(b->b.name=="Thinking in Java").count()}</p>
    <p>求和：${books.stream().map(b->b.price * b.count).sum()}</p>
    <p></p>
</body>
</html>
