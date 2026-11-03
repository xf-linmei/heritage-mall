<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  公共错误页：
  1) 全局异常映射 java.lang.Exception -> error
  2) 文件上传类型/大小不合法（fileUpload 拦截器）-> input
  都会被转发到本页，页面用 Struts2 标签读取值栈中的 actionError / actionMessage
--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>出错了 · 匠集</title>
    <style>
        body { margin: 0; font-family: "Microsoft YaHei", sans-serif; background: #f6f4ef; color: #3c3c3c; }
        .box { max-width: 640px; margin: 120px auto; background: #fff; border: 1px solid #e6e0d4;
               border-radius: 8px; padding: 40px; text-align: center; }
        h1 { color: #8c2f26; font-size: 26px; letter-spacing: 4px; margin: 0 0 18px; }
        .msg { color: #a94442; background: #fdf2f2; border: 1px solid #f0d4d4;
               border-radius: 4px; padding: 12px; margin: 16px 0; font-size: 14px; }
        .tip { color: #8a8a8a; font-size: 13px; }
        a.btn { display: inline-block; margin-top: 22px; padding: 10px 26px; background: #8c2f26;
                color: #fff; text-decoration: none; border-radius: 4px; }
        a.btn.ghost { background: #fff; color: #8c2f26; border: 1px solid #8c2f26; margin-left: 10px; }
    </style>
</head>
<body>
<div class="box">
    <h1>匠 集 · 出 错 了</h1>

    <%-- Struts2 标签读取 Action 中 addActionError() 存入的信息 --%>
    <s:if test="hasActionErrors()">
        <div class="msg">
            <s:iterator value="actionErrors">
                <div><s:property/></div>
            </s:iterator>
        </div>
    </s:if>

    <%-- 全局异常映射时，容器会把异常信息放在该属性中 --%>
    <s:if test="#request['javax.servlet.error.message'] != null">
        <div class="msg">
            <s:property value="#request['javax.servlet.error.message']"/>
        </div>
    </s:if>

    <p class="tip">请返回首页重试，或联系管理员。（若为图片上传报错，请确认格式为 jpg/png 且小于 5MB）</p>

    <a class="btn" href="${pageContext.request.contextPath}/index.action">返回首页</a>
    <a class="btn ghost" href="${pageContext.request.contextPath}/user_toLogin.action">重新登录</a>
</div>
</body>
</html>
