package kindergarten.controller;

import kindergarten.web.AppContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {
    protected AppContext appContext(HttpServletRequest request) {
        javax.servlet.ServletContext servletContext = request.getServletContext();
        Object attribute = servletContext.getAttribute(AppContext.ATTRIBUTE_NAME);
        if (attribute instanceof AppContext appContext) {
            return appContext;
        }

        synchronized (servletContext) {
            attribute = servletContext.getAttribute(AppContext.ATTRIBUTE_NAME);
            if (attribute instanceof AppContext appContext) {
                return appContext;
            }

            try {
                AppContext appContext = AppContext.create();
                servletContext.setAttribute(AppContext.ATTRIBUTE_NAME, appContext);
                servletContext.removeAttribute(AppContext.ATTRIBUTE_NAME + ".error");
                return appContext;
            } catch (RuntimeException e) {
                servletContext.setAttribute(AppContext.ATTRIBUTE_NAME + ".error", e);
                throw e;
            }
        }
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String view) throws IOException {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected void redirectWithParams(HttpServletRequest request, HttpServletResponse response, String path, Map<String, String> params) throws IOException {
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

    protected void redirectToFormWithError(HttpServletRequest request, HttpServletResponse response, String path, String rawId, Map<String, String> params, String error) throws IOException {
        if (rawId != null && !rawId.isBlank()) {
            params.put("id", rawId);
        }

        putQueryParam(params, "error", error);
        redirectWithParams(request, response, path, params);
    }

    protected String firstValidationError(List<Supplier<String>> validationRules) {
        for (Supplier<String> validationRule : validationRules) {
            String error = validationRule.get();
            if (error != null) {
                return error;
            }
        }

        return null;
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