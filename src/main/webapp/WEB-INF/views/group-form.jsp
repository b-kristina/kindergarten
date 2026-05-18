<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kindergarten.model.Group,kindergarten.web.AppContext,kindergarten.web.ViewUtil" %>
<% String pageTitle = "Группы"; %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%
    AppContext appContext = (AppContext) application.getAttribute(AppContext.ATTRIBUTE_NAME);
    Throwable startupError = (Throwable) application.getAttribute(AppContext.ATTRIBUTE_NAME + ".error");
    String errorMessage = request.getParameter("error");
    String rawId = request.getParameter("id");
    String currentName = request.getParameter("name");
    String currentNumber = request.getParameter("number");
    Group group = null;
    boolean editMode = rawId != null && !rawId.isBlank();

    if (startupError == null && appContext != null && editMode) {
        try {
            group = appContext.getGroupService().getGroupById(Integer.parseInt(rawId));
            if (currentName == null) {
                currentName = group.getName();
            }
            if (currentNumber == null) {
                currentNumber = String.valueOf(group.getNumber());
            }
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
            editMode = false;
        }
    }

    if (currentName == null) {
        currentName = "";
    }
    if (currentNumber == null) {
        currentNumber = "";
    }
%>
<section class="card">
    <h1><%= editMode ? "Редактирование группы" : "Новая группа" %></h1>

    <% if (startupError != null) { %>
        <div class="error"><%= ViewUtil.escapeHtml(startupError.getMessage()) %></div>
    <% } else if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error"><%= ViewUtil.escapeHtml(errorMessage) %></div>
    <% } %>

    <% if (startupError == null) { %>
        <form method="post" action="<%= request.getContextPath() %>/groups/save">
            <% if (editMode && group != null) { %>
                <input type="hidden" name="id" value="<%= group.getId() %>">
            <% } %>

            <label>Название</label>
            <input name="name" value="<%= ViewUtil.escapeHtml(currentName) %>" required>

            <label>Номер</label>
            <input name="number" type="number" min="1" value="<%= ViewUtil.escapeHtml(currentNumber) %>" required>

            <div class="actions" style="margin-top:18px">
                <button type="submit"><%= editMode ? "Сохранить" : "Создать" %></button>
                <a class="button secondary" href="<%= request.getContextPath() %>/groups">Назад</a>
            </div>
        </form>
    <% } %>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>