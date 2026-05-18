<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kindergarten.model.Group,kindergarten.web.AppContext,kindergarten.web.ViewUtil,java.util.Collections,java.util.HashMap,java.util.List,java.util.Map" %>
<% String pageTitle = "Группы"; %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%
    AppContext appContext = (AppContext) application.getAttribute(AppContext.ATTRIBUTE_NAME);
    Throwable startupError = (Throwable) application.getAttribute(AppContext.ATTRIBUTE_NAME + ".error");
    String errorMessage = request.getParameter("error");
    List<Group> groups = Collections.emptyList();
    Map<Integer, Integer> childCountByGroupId = new HashMap<>();

    if (startupError == null && appContext != null) {
        groups = appContext.getGroupService().getAllGroups();
        for (Group group : groups) {
            childCountByGroupId.put(group.getId(), appContext.getChildService().getChildrenByGroupId(group.getId()).size());
        }
    }
%>
<section class="card">
    <div class="actions">
        <h1 style="margin:0;flex:1">Группы</h1>
        <a class="button" href="<%= request.getContextPath() %>/groups/new">Новая группа</a>
    </div>
</section>

<section class="card">
    <% if (startupError != null) { %>
        <div class="error"><%= ViewUtil.escapeHtml(startupError.getMessage()) %></div>
    <% } else if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error"><%= ViewUtil.escapeHtml(errorMessage) %></div>
    <% } %>

    <% if (groups.isEmpty()) { %>
        <p class="muted">Групп пока нет.</p>
    <% } else { %>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Номер</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <% for (Group group : groups) { %>
                <tr>
                    <td><%= group.getId() %></td>
                    <td><%= ViewUtil.escapeHtml(group.getName()) %></td>
                    <td><%= group.getNumber() %></td>
                    <td>
                        <div class="actions">
                            <a class="button secondary" href="<%= request.getContextPath() %>/groups/edit?id=<%= group.getId() %>">Редактировать</a>
                            <form class="inline" method="post" action="<%= request.getContextPath() %>/groups/delete">
                                <input type="hidden" name="id" value="<%= group.getId() %>">
                                <button type="submit">Удалить</button>
                            </form>
                            <span class="muted">Детей: <%= childCountByGroupId.getOrDefault(group.getId(), 0) %></span>
                        </div>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
</section>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>