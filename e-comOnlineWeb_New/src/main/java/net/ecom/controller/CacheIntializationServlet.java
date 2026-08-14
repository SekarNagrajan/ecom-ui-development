package net.ecom.controller;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

public class CacheIntializationServlet extends HttpServlet {

    ServletContext context = null;

    @Override
    public void init(ServletConfig config) {
        try {
            System.out.println("[ECOM-WAR] CacheIntializationServlet ====CacheIntializationServlet>>");
            context = config.getServletContext();
            initializeCache();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        initializeCache();
        response.getWriter().print("SUCCESS");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void initializeCache() {
        try {
            System.out.println("[ECOM-WAR] CacheIntializationServlet ====cache init servlet load====");
            EcomCacheLoader loader = new EcomCacheLoader();
            loader.cacheLoader(context);
            loader.cacheLoaderWS(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
