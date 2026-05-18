package kindergarten.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {
            servletContext.setAttribute(AppContext.ATTRIBUTE_NAME, AppContext.create());
        } catch (RuntimeException e) {
            servletContext.setAttribute(AppContext.ATTRIBUTE_NAME + ".error", e);
        }
    }
}