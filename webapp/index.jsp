<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  项目根路径（/heritage/）默认命中本页，
  这里直接转发到首页 Action，保证首页的轮播、分类、热门/新品数据都由后台装配后再展示。
--%>
<jsp:forward page="/index.action"/>
