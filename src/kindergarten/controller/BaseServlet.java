package kindergarten.controller;

import kindergarten.web.AppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {

    protected static final String PATH_CHILDREN = "/children";
    protected static final String PATH_CHILDREN_NEW = "/children/new";
    protected static final String PATH_CHILDREN_EDIT = "/children/edit";
    protected static final String PATH_CHILDREN_SAVE = "/children/save";
    protected static final String PATH_CHILDREN_DELETE = "/children/delete";

    protected static final String PATH_GROUPS = "/groups";
    protected static final String PATH_GROUPS_NEW = "/groups/new";
    protected static final String PATH_GROUPS_EDIT = "/groups/edit";
    protected static final String PATH_GROUPS_SAVE = "/groups/save";

    protected AppContext appContext(HttpServletRequest request) {
        AppContext ctx = (AppContext) request.getServletContext()
                .getAttribute(AppContext.ATTRIBUTE_NAME);

        if (ctx == null) {
            throw new IllegalStateException("AppContext not initialized");
        }

        return ctx;
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String view)
            throws IOException {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (ServletException e) {
            throw new IOException("Forward failed: " + view, e);
        }
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected void redirectWithParams(HttpServletRequest request, HttpServletResponse response,
                                      String path, Map<String, String> params) throws IOException {

        StringBuilder url = new StringBuilder(request.getContextPath()).append(path);
        boolean hasQuery = path.contains("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (value == null || value.isBlank()) {
                continue;
            }
            url.append(hasQuery ? '&' : '?');
            url.append(entry.getKey()).append('=').append(encodeUrl(value));
            hasQuery = true;
        }

        response.sendRedirect(url.toString());
    }

    protected String encodeUrl(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    protected void putQueryParam(Map<String, String> params, String name, String value) {
        if (value != null && !value.isBlank()) {
            params.put(name, value);
        }
    }

    protected void redirectToFormWithError(HttpServletRequest request, HttpServletResponse response,
                                           String path, String rawId, Map<String, String> params, String error) throws IOException {

        if (rawId != null && !rawId.isBlank()) {
            params.put("id", rawId);
        }
        putQueryParam(params, "error", error);
        redirectWithParams(request, response, path, params);
    }

    protected boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    protected Integer parseInteger(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}