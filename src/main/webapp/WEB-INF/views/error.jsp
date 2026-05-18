<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kindergarten.web.AppContext,kindergarten.web.ViewUtil" %>
<% String pageTitle = "Ошибка"; %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%
    Throwable startupError = (Throwable) application.getAttribute(AppContext.ATTRIBUTE_NAME + ".error");
    String errorMessage = request.getParameter("error");
    if (errorMessage == null || errorMessage.isBlank()) {
        Object servletError = request.getAttribute("javax.servlet.error.message");
        if (servletError != null) {
            errorMessage = servletError.toString();
        }
    }
    if ((errorMessage == null || errorMessage.isBlank()) && startupError != null) {
        errorMessage = startupError.getMessage();
    }
    if (errorMessage == null || errorMessage.isBlank()) {
        errorMessage = "Не удалось выполнить запрос.";
    }
%>
<section class="card">
    <h1>Ошибка</h1>
    <div class="error"><%= ViewUtil.escapeHtml(errorMessage) %></div>
    <p class="muted">Проверьте, что PostgreSQL запущен и доступен на 127.0.0.1:5433.</p>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
