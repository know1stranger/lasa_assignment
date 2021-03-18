<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<t:layout title="Home" context="${pageContext.servletContext.contextPath}">
    <jsp:body>
        <div class="row">
            <div class="col-md-12" style="text-align: center">
                <h4>Welcome <c:out value="${username}"/>!<h4>
            </div>
        </div>
    </jsp:body>
</t:layout>