package kindergarten.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        try {
            AppContext appContext = AppContext.create();
            servletContext.setAttribute(AppContext.ATTRIBUTE_NAME, appContext);
            System.out.println("AppContext initialized successfully");
        } catch (RuntimeException e) {
            servletContext.setAttribute(AppContext.ATTRIBUTE_NAME + ".error", e);
            System.err.println("Failed to initialize AppContext: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        servletContext.removeAttribute(AppContext.ATTRIBUTE_NAME);
        servletContext.removeAttribute(AppContext.ATTRIBUTE_NAME + ".error");

        System.out.println("AppContext destroyed");
    }
}