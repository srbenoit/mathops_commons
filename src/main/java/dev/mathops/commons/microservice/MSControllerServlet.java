package dev.mathops.commons.microservice;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.installation.Installation;
import dev.mathops.commons.log.Log;
import dev.mathops.commons.log.LogBase;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.Serial;

public final class MSControllerServlet extends HttpServlet {

    /** Version number for serialization. */
    @Serial
    private static final long serialVersionUID = 2807128847440271961L;

    /** The servlet configuration. */
    private ServletConfig servletConfig = null;

    /** The servlet context. */
    private ServletContext servletContext = null;

    /** The installation. */
    private Installation installation = null;

    /** The service directory. */
    private File serviceDir = null;

    /**
     * Constructs a new {@code MSControllerServlet}.
     */
    public MSControllerServlet() {

        super();
    }

    /**
     * Initializes the servlet.
     *
     * @param config the servlet context in which the servlet is being initialized
     */
    @Override
    public void init(final ServletConfig config) {

        this.servletConfig = config;
        this.servletContext = config.getServletContext();

        this.installation =
                (Installation) this.servletContext.getAttribute(MSContextListener.INSTALLATION_KEY);
        this.serviceDir = (File) this.servletContext.getAttribute(MSContextListener.SERVICE_DIR_KEY);

        final String serverInfo = this.servletContext.getServerInfo();
        Log.info("Microservices Controller Servlet Starting in ", serverInfo);

        // TODO:

        Log.info("Microservices Controller Servlet Initialized");
    }

    /**
     * Gets the service directory.
     *
     * @return the service directory
     */
    public File getServiceDir() {

        return this.serviceDir;
    }

    /**
     * Gets the servlet configuration.
     *
     * @return the servlet configuration
     */
    @Override
    public ServletConfig getServletConfig() {

        return this.servletConfig;
    }

    /**
     * Gets the servlet context under which this servlet was initialized.
     *
     * @return the servlet context
     */
    @Override
    public ServletContext getServletContext() {

        return this.servletContext;
    }

    /**
     * Gets the installation.
     *
     * @return the installation
     */
    private Installation getInstallation() {

        return this.installation;
    }

    /**
     * Called when the servlet container unloads the servlet.
     * <p>
     * This method reduces the reference count on the installation - when that count reaches zero, the installation's
     * JMX server is allowed to stop
     */
    @Override
    public void destroy() {

        final Installation installation = getInstallation();
        final File dir = new File(installation.baseDir, "sessions");

        Log.info("Microservices Controller Servlet Terminated");
        Log.fine(CoreConstants.EMPTY);
    }

    /**
     * Gets the servlet information string.
     *
     * @return the information string
     */
    @Override
    public String getServletInfo() {

        return "Microservices Controller Servlet";
    }

    /**
     * Processes a request. The first part of the request path (between the first and second '/') is used to determine
     * the site, then if the site is valid, the request is dispatched to the site processor.
     *
     * @param req  the request
     * @param resp the response
     * @throws IOException      if there is an error writing the response
     * @throws ServletException if there is an exception processing the request
     */
    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException,
            ServletException {

        req.setCharacterEncoding("UTF-8");

        final String remote = req.getRemoteAddr();

        try {
            final String requestHost = ServletUtils.getHost(req);
            final String requestPath = ServletUtils.getPath(req);
            LogBase.setHostPath(requestHost, requestPath, remote);

            try {
                if (req.isSecure()) {
                    serviceSecure(requestPath, req, resp);
                } else {
                    final String reqScheme = req.getScheme();
                    if ("http".equals(reqScheme)) {
                        serviceInsecure(requestPath, req, resp);
                    } else {
                        Log.warning("Invalid scheme: ", reqScheme);
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            } finally {
                LogBase.setHostPath(null, null, null);
            }
        } catch (final IOException ex) {
            // Make sure unexpected exceptions get logged rather than silently failing
            Log.severe(ex);
            throw ex;
        }
    }

    /**
     * Processes a request when it is known the connection was secured. The first part of the request path is used to
     * determine whether the request is for a public file, or if not, to determine the mid-controller to which to
     * forward the request.
     ** @param requestPath the request path
     * @param req         the HTTP servlet request
     * @param resp        the HTTP servlet response
     * @throws IOException if there is an error writing the response
     */
    private void serviceSecure(final String requestPath, final HttpServletRequest req,
                               final HttpServletResponse resp) throws IOException {

        // TODO:
    }

    /**
     * Processes a request when it is known the connection is not secure. The first part of the request path is used to
     * determine whether the request is for a public file, or if not, to determine the mid-controller to which to
     * forward the request.
     *
     * @param requestPath the request path
     * @param req         the HTTP servlet request
     * @param resp        the HTTP servlet response
     * @throws IOException if there is an error writing the response
     */
    private void serviceInsecure(final String requestPath, final HttpServletRequest req,
                                 final HttpServletResponse resp) throws IOException {

        // TODO:
    }
}