package kindergarten.web;

import org.owasp.encoder.Encode;

public final class ViewUtil {

    private ViewUtil() {
    }

    public static String escapeHtml(String value) {
        return Encode.forHtml(value != null ? value : "");
    }

    public static String escapeHtmlAttr(String value) {
        return Encode.forHtmlAttribute(value != null ? value : "");
    }

    public static String escapeJs(String value) {
        return Encode.forJavaScript(value != null ? value : "");
    }
}