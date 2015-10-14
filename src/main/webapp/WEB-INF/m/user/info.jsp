<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-9-20
  Time: 下午6:55
  To change this template use File | Settings | File Templates.
--%>
<template:basic htmlTitle="${projectName}" bodyTitle="">
    <jsp:attribute name="extraFootScriptConent">
        <!-- REQUIRED: Bootstrap Color Picker -->
        <script src="<c:url value="/resources/js/include/bootstrap-colorpicker.min.js" /> "></script>

	    <!-- REQUIRED: Bootstrap Time Picker -->
        <script src="<c:url value="/resources/js/include/bootstrap-timepicker.min.js" />"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="fluid-container">

            <!-- widget grid -->
            <section id="widget-grid" class="">
                <div class="row-fluid">
                    <article class="span12">
                        <!-- new widget -->
                        <div class="jarviswidget" id="widget-id-0">
                            <header>
                                <h2>我的信息</h2>
                            </header>
                            <!-- wrap div -->
                            <div>

                                <div class="jarviswidget-editbox">
                                    <div>
                                        <label>Title:</label>
                                        <input type="text" />
                                    </div>
                                    <div>
                                        <label>Styles:</label>
                                        <span data-widget-setstyle="purple" class="purple-btn"></span>
                                        <span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
                                        <span data-widget-setstyle="green" class="green-btn"></span>
                                        <span data-widget-setstyle="yellow" class="yellow-btn"></span>
                                        <span data-widget-setstyle="orange" class="orange-btn"></span>
                                        <span data-widget-setstyle="pink" class="pink-btn"></span>
                                        <span data-widget-setstyle="red" class="red-btn"></span>
                                        <span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
                                        <span data-widget-setstyle="black" class="black-btn"></span>
                                    </div>
                                </div>

                                <div class="inner-spacer">
                                    <!-- content goes here -->
                                    <form:form method="post" class="form-horizontal themed" modelAttribute="user">
                                        <fieldset>
                                            <div class="control-group">
                                                <label class="control-label" for="username_input">用户名</label>
                                                <div class="controls">
                                                    <form:input path="username" readonly="true" type="text" class="span12" id="username_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="real_input">姓名</label>
                                                <div class="controls">
                                                    <form:input path="realName" type="text" class="span12" id="real_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="email_input">邮箱</label>
                                                <div class="controls">
                                                    <form:input path="email" type="text" class="span12" id="email_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="phone_input">手机号</label>
                                                <div class="controls">
                                                    <form:input path="phone" type="text" class="span12" id="phone_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="qq_input">QQ</label>
                                                <div class="controls">
                                                    <form:input path="qq" type="text" class="span12" id="qq_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="idNumber_input">身份证号</label>
                                                <div class="controls">
                                                    <form:input path="idNumber" type="text" class="span12" id="idNumber_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="cooperation_input">公司</label>
                                                <div class="controls">
                                                    <form:input path="cooperation" type="text" class="span12" id="cooperation_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="school_input">学校</label>
                                                <div class="controls">
                                                    <form:input path="school" type="text" class="span12" id="school_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="department_input">院系</label>
                                                <div class="controls">
                                                    <form:input path="department" type="text" class="span12" id="department_input" />
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">性别</label>
                                                <div class="controls">
                                                    <label class="radio">
                                                        <input type="radio" name="sex" id="optionsRadios1" value="0" <c:if test="${user.sex==0}">checked</c:if> />
                                                        男
                                                    </label>
                                                    <label class="radio">
                                                        <input type="radio" name="sex" id="optionsRadios2" value="1" <c:if test="${user.sex==1}">checked</c:if> />
                                                        女
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">生日</label>
                                                <div class="controls">
                                                    <div class="input-append date" id="datepicker-js" data-date="2000-01-01" data-date-format="yyyy-mm-dd">
                                                        <form:input path="birthday" class="datepicker-input" size="16" type="text" placeholder="选择日期" />
                                                        <span class="add-on"><i class="cus-calendar-2"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-actions">
                                                <button type="submit" class="btn medium btn-primary">
                                                    保存
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form:form>
                                </div>
                                <!-- end content-->
                            </div>
                            <!-- end wrap div -->
                        </div>
                        <!-- end widget -->
                    </article>
                </div>

                <!-- end row-fluid -->
            </section>
        </div>
    </jsp:body>

</template:basic>

