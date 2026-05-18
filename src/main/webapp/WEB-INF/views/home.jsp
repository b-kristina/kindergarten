<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Детский сад"; %>
<%@ page import="kindergarten.web.ViewUtil" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<section class="card">
    <h1>Детский сад</h1>
    <p class="muted">Управление группами и детьми</p>
    <div class="actions">
        <a class="button" href="<%= request.getContextPath() %>/groups">Открыть группы</a>
        <a class="button secondary" href="<%= request.getContextPath() %>/children">Открыть детей</a>
    </div>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>