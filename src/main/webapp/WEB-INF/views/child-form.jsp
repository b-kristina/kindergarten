<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kindergarten.model.Child,kindergarten.model.Group,kindergarten.web.AppContext,kindergarten.web.ViewUtil,java.util.Collections,java.util.List" %>
<% String pageTitle = "Дети"; %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%
    AppContext appContext = (AppContext) application.getAttribute(AppContext.ATTRIBUTE_NAME);
    Throwable startupError = (Throwable) application.getAttribute(AppContext.ATTRIBUTE_NAME + ".error");
    String errorMessage = request.getParameter("error");
    String rawId = request.getParameter("id");
    String currentFullName = request.getParameter("fullName");
    String currentGender = request.getParameter("gender");
    String currentAge = request.getParameter("age");
    String currentGroupId = request.getParameter("groupId");
    Child child = null;
    boolean editMode = rawId != null && !rawId.isBlank();
    List<Group> groups = Collections.emptyList();

    if (startupError == null && appContext != null) {
        groups = appContext.getGroupService().getAllGroups();
        if (editMode) {
            try {
                child = appContext.getChildService().getChildById(Integer.parseInt(rawId));
                if (currentFullName == null) {
                    currentFullName = child.getFullName();
                }
                if (currentGender == null) {
                    currentGender = child.getGender();
                }
                if (currentAge == null) {
                    currentAge = String.valueOf(child.getAge());
                }
                if (currentGroupId == null && child.getGroupId() != null) {
                    currentGroupId = String.valueOf(child.getGroupId());
                }
            } catch (RuntimeException e) {
                errorMessage = e.getMessage();
                editMode = false;
            }
        }
    }

    if (currentFullName == null) {
        currentFullName = "";
    }
    if (currentGender == null) {
        currentGender = "";
    }
    if (currentAge == null) {
        currentAge = "";
    }
    if (currentGroupId == null) {
        currentGroupId = "";
    }
%>
<section class="card">
    <h1><%= editMode ? "Редактирование ребенка" : "Новый ребенок" %></h1>

    <% if (startupError != null) { %>
        <div class="error"><%= ViewUtil.escapeHtml(startupError.getMessage()) %></div>
    <% } else if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error"><%= ViewUtil.escapeHtml(errorMessage) %></div>
    <% } %>

    <% if (groups.isEmpty()) { %>
        <p class="error">Сначала создайте хотя бы одну группу.</p>
    <% } else if (startupError == null) { %>
        <form method="post" action="<%= request.getContextPath() %>/children/save">
            <% if (editMode && child != null) { %>
                <input type="hidden" name="id" value="<%= child.getId() %>">
            <% } %>

            <label>ФИО ребенка</label>
            <input name="fullName" value="<%= ViewUtil.escapeHtml(currentFullName) %>" required>

            <label>Пол</label>
            <select name="gender" required>
                <option value="М" <%= "М".equals(currentGender) ? "selected" : "" %>>М</option>
                <option value="Ж" <%= "Ж".equals(currentGender) ? "selected" : "" %>>Ж</option>
            </select>

            <label>Возраст</label>
            <input name="age" type="number" min="1" value="<%= ViewUtil.escapeHtml(currentAge) %>" required>

            <label>Группа</label>
            <select name="groupId" required>
                <% if (editMode) { %>
                    <option value="" <%= currentGroupId.isBlank() ? "selected" : "" %>>Без группы</option>
                <% } %>
                <% for (Group group : groups) { %>
                    <option value="<%= group.getId() %>" <%= String.valueOf(group.getId()).equals(currentGroupId) ? "selected" : "" %>><%= ViewUtil.escapeHtml(group.getName()) %> №<%= group.getNumber() %></option>
                <% } %>
            </select>

            <div class="actions" style="margin-top:18px">
                <button type="submit"><%= editMode ? "Сохранить" : "Создать" %></button>
                <a class="button secondary" href="<%= request.getContextPath() %>/children">Назад</a>
            </div>
        </form>
    <% } %>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>