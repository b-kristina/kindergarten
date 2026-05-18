package kindergarten.controller;

import kindergarten.service.GroupService;
import kindergarten.web.AppContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GroupServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();

        switch (path) {
            case PATH_GROUPS:
                listGroups(request, response);
                break;
            case PATH_GROUPS_NEW:
                showNewForm(request, response);
                break;
            case PATH_GROUPS_EDIT:
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
            case PATH_GROUPS_SAVE:
                save(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listGroups(HttpServletRequest request, HttpServletResponse response) throws IOException {
        forward(request, response, "/WEB-INF/views/groups.jsp");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        forward(request, response, "/WEB-INF/views/group-form.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            redirect(request, response, PATH_GROUPS);
            return;
        }
        forward(request, response, "/WEB-INF/views/group-form.jsp");
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        String name = request.getParameter("name");
        String rawNumber = request.getParameter("number");

        boolean isEdit = rawId != null && !rawId.isBlank();

        try {
            GroupService service = appContext(request).getGroupService();

            if (isEdit) {
                service.updateGroup(
                        parseInteger(rawId), name, parseInteger(rawNumber)
                );
            } else {
                service.createGroup(name, parseInteger(rawNumber));
            }

            redirect(request, response, PATH_GROUPS);

        } catch (RuntimeException e) {
            Map<String, String> params = new LinkedHashMap<>();
            params.put("name", name);
            params.put("number", rawNumber);

            String formPath = isEdit ? PATH_GROUPS_EDIT : PATH_GROUPS_NEW;
            redirectToFormWithError(request, response, formPath, rawId, params, e.getMessage());
        }
    }
}