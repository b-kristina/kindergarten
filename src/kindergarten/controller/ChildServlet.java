package kindergarten.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChildServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        appContext(request);
        String servletPath = request.getServletPath();
        if ("/children".equals(servletPath)) {
            forward(request, response, "/WEB-INF/views/children.jsp");
            return;
        }

        if ("/children/new".equals(servletPath) || "/children/edit".equals(servletPath)) {
            forward(request, response, "/WEB-INF/views/child-form.jsp");
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("/children/save".equals(request.getServletPath())) {
            saveChild(request, response);
            return;
        }

        if ("/children/delete".equals(request.getServletPath())) {
            deleteChild(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void saveChild(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String rawAge = request.getParameter("age");
        String rawGroupId = request.getParameter("groupId");
        Integer age = parseInteger(request.getParameter("age"));
        Integer groupId = parseInteger(rawGroupId);
        Integer id = parseInteger(rawId);
        boolean editMode = rawId != null && !rawId.isBlank();

        String validationError = validateChildForm(editMode, id, fullName, gender, age, groupId);
        if (validationError != null) {
            Map<String, String> params = new LinkedHashMap<>();
            putQueryParam(params, "fullName", fullName);
            putQueryParam(params, "gender", gender);
            putQueryParam(params, "age", rawAge);
            putQueryParam(params, "groupId", rawGroupId);
            redirectToFormWithError(request, response, editMode ? "/children/edit" : "/children/new", rawId, params, validationError);
            return;
        }

        if (editMode) {
            appContext(request).getChildService().updateChild(id, fullName, gender, age, groupId);
        } else {
            appContext(request).getChildService().createChild(fullName, gender, age, groupId);
        }

        redirect(request, response, "/children");
    }

    private String validateChildForm(boolean editMode, Integer id, String fullName, String gender, Integer age, Integer groupId) {
        return firstValidationError(List.of(
                () -> editMode && id == null ? "Неверные данные формы" : null,
                () -> isBlank(fullName) ? "ФИО ребенка не может быть пустым" : null,
                () -> isInvalidGender(gender) ? "Пол должен быть М или Ж" : null,
                () -> age == null ? "Неверный возраст" : null,
                () -> !editMode && groupId == null ? "Группа не может быть пустой" : null
        ));
    }

    private boolean isInvalidGender(String gender) {
        return !"М".equals(gender) && !"Ж".equals(gender);
    }

    private void deleteChild(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID ребенка");
            return;
        }

        appContext(request).getChildService().deleteChild(id);
        redirect(request, response, "/children");
    }

}