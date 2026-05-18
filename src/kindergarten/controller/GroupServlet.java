package kindergarten.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GroupServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        appContext(request);
        String servletPath = request.getServletPath();
        if ("/groups".equals(servletPath)) {
            forward(request, response, "/WEB-INF/views/groups.jsp");
            return;
        }

        if ("/groups/new".equals(servletPath) || "/groups/edit".equals(servletPath)) {
            forward(request, response, "/WEB-INF/views/group-form.jsp");
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("/groups/save".equals(request.getServletPath())) {
            saveGroup(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void saveGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        String name = request.getParameter("name");
        String rawNumber = request.getParameter("number");
        Integer id = parseInteger(rawId);
        Integer number = parseInteger(rawNumber);
        boolean editMode = rawId != null && !rawId.isBlank();

        String validationError = validateGroupForm(editMode, id, name, number);
        if (validationError != null) {
            Map<String, String> params = new LinkedHashMap<>();
            putQueryParam(params, "name", name);
            putQueryParam(params, "number", rawNumber);
            redirectToFormWithError(request, response, editMode ? "/groups/edit" : "/groups/new", rawId, params, validationError);
            return;
        }

        if (editMode) {
            appContext(request).getGroupService().updateGroup(id, name, number);
        } else {
            appContext(request).getGroupService().createGroup(name, number);
        }

        redirect(request, response, "/groups");
    }

    private String validateGroupForm(boolean editMode, Integer id, String name, Integer number) {
        return firstValidationError(List.of(
                () -> editMode && id == null ? "Неверные данные формы" : null,
                () -> isBlank(name) ? "Название группы не может быть пустым" : null,
                () -> number == null ? "Неверный номер группы" : null
        ));
    }

}