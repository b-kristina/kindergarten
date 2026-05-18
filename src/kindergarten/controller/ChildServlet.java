package kindergarten.controller;

import kindergarten.service.ChildService;
import kindergarten.web.AppContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChildServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();

        switch (path) {
            case PATH_CHILDREN:
                listChildren(request, response);
                break;
            case PATH_CHILDREN_NEW:
                showNewForm(request, response);
                break;
            case PATH_CHILDREN_EDIT:
                showEditForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();

        switch (path) {
            case PATH_CHILDREN_SAVE:
                save(request, response);
                break;
            case PATH_CHILDREN_DELETE:
                delete(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listChildren(HttpServletRequest request, HttpServletResponse response) throws IOException {
        forward(request, response, "/WEB-INF/views/children.jsp");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        forward(request, response, "/WEB-INF/views/child-form.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            redirect(request, response, PATH_CHILDREN);
            return;
        }
        forward(request, response, "/WEB-INF/views/child-form.jsp");
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String rawAge = request.getParameter("age");
        String rawGroupId = request.getParameter("groupId");

        boolean isEdit = rawId != null && !rawId.isBlank();

        try {
            ChildService service = appContext(request).getChildService();

            if (isEdit) {
                service.updateChild(
                        parseInteger(rawId), fullName, gender,
                        parseInteger(rawAge), parseInteger(rawGroupId)
                );
            } else {
                service.createChild(fullName, gender, parseInteger(rawAge), parseInteger(rawGroupId));
            }

            redirect(request, response, PATH_CHILDREN);

        } catch (RuntimeException e) {
            Map<String, String> params = new LinkedHashMap<>();
            params.put("fullName", fullName);
            params.put("gender", gender);
            params.put("age", rawAge);
            params.put("groupId", rawGroupId);

            String formPath = isEdit ? PATH_CHILDREN_EDIT : PATH_CHILDREN_NEW;
            redirectToFormWithError(request, response, formPath, rawId, params, e.getMessage());
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");

        if (rawId == null || rawId.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id = parseInteger(rawId);
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        appContext(request).getChildService().deleteChild(id);
        redirect(request, response, PATH_CHILDREN);
    }
}