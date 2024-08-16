package dev.mathops.commons.microservice;

import dev.mathops.commons.installation.Installation;
import dev.mathops.commons.installation.Installations;
import dev.mathops.commons.log.Log;
import dev.mathops.commons.log.LoggingSubsystem;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.File;

/**
 * A listener to do installation configuration for the front controller context (rather than within the servlet itself).
 * This class must have "public" access so the servlet container can instantiate.
 */
public final class MSContextListener implements ServletContextListener {

    /** A key under which a context attribute is stored. */
    static final String INSTALLATION_KEY = "Installation";

    /** A key under which a context attribute is stored. */
    static final String SERVICE_DIR_KEY = "ServiceDir";

    /** The installation. */
    private Installation installation = null;

    /**
     * Constructs a new {@code MSContextListener}.  This constructor must have "public" access so the servlet container
     * can instantiate.
     */
    public MSContextListener() {

        // No action
    }

    /**
     * Called when the context is initialized.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        final ServletContext ctx = sce.getServletContext();

        final String baseDirPath = ctx.getInitParameter("mathops-base-dir");
        final String cfgFile = ctx.getInitParameter("mathops-cfg-file");
        final File baseDir = baseDirPath == null ? null : new File(baseDirPath);

        this.installation = Installations.get().getInstallation(baseDir, cfgFile);
        LoggingSubsystem.setInstallation(this.installation);

        final String serviceDirPath = ctx.getInitParameter("service-dir");
        final File serviceDir = serviceDirPath == null ? null : new File(serviceDirPath);

        final String serverInfo = ctx.getServerInfo();
        Log.info("Microservices Context Listener Starting in ", serverInfo);

        Log.config("Installation base directory: ", serviceDirPath);
        Log.config("Installation configuration file:", cfgFile);

        ctx.setAttribute(INSTALLATION_KEY, this.installation);
        ctx.setAttribute(SERVICE_DIR_KEY, serviceDir);

        Log.info("Microservice Context Listener Initialized");
    }

    /**
     * Called when the context is destroyed.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        Log.info("Microservice Context Listener Terminated");
        this.installation = null;
    }
}
