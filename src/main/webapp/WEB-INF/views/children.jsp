<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kindergarten.model.Child,kindergarten.model.Group,kindergarten.web.AppContext,kindergarten.web.ViewUtil,java.util.Collections,java.util.HashMap,java.util.List,java.util.Map" %>
<% String pageTitle = "Дети"; %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%
    AppContext appContext = (AppContext) application.getAttribute(AppContext.ATTRIBUTE_NAME);
    Throwable startupError = (Throwable) application.getAttribute(AppContext.ATTRIBUTE_NAME + ".error");
    String errorMessage = request.getParameter("error");
    List<Child> children = Collections.emptyList();
    List<Group> groups = Collections.emptyList();
    Map<Integer, Group> groupById = new HashMap<>();

    if (startupError == null && appContext != null) {
        children = appContext.getChildService().getAllChildren();
        groups = appContext.getGroupService().getAllGroups();
        for (Group group : groups) {
            groupById.put(group.getId(), group);
        }
    }
%>
<section class="card">
    <div class="actions">
        <h1 style="margin:0;flex:1">Дети</h1>
        <a class="button" href="<%= request.getContextPath() %>/children/new">Новый ребенок</a>
    </div>
</section>

<section class="card">
    <% if (startupError != null) { %>
        <div class="error"><%= ViewUtil.escapeHtml(startupError.getMessage()) %></div>
    <% } else if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error"><%= ViewUtil.escapeHtml(errorMessage) %></div>
    <% } %>

    <% if (children.isEmpty()) { %>
        <p class="muted">Детей пока нет.</p>
    <% } else { %>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>ФИО</th>
                <th>Пол</th>
                <th>Возраст</th>
                <th>Группа</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <% for (Child child : children) { %>
                <tr>
                    <td><%= child.getId() %></td>
                    <td><%= ViewUtil.escapeHtml(child.getFullName()) %></td>
                    <td><%= ViewUtil.escapeHtml(child.getGender()) %></td>
                    <td><%= child.getAge() %></td>
                    <td>
                        <% if (child.getGroupId() == null) { %>
                            Без группы
                        <% } else if (groupById.get(child.getGroupId()) != null) { %>
                            <%= ViewUtil.escapeHtml(groupById.get(child.getGroupId()).getName()) %> №<%= groupById.get(child.getGroupId()).getNumber() %>
                        <% } else { %>
                            Группа удалена
                        <% } %>
                    </td>
                    <td>
                        <div class="actions">
                            <a class="button secondary" href="<%= request.getContextPath() %>/children/edit?id=<%= child.getId() %>">Редактировать</a>
                            <form class="inline" method="post" action="<%= request.getContextPath() %>/children/delete">
                                <input type="hidden" name="id" value="<%= child.getId() %>">
                                <button type="submit">Удалить</button>
                            </form>
                        </div>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>