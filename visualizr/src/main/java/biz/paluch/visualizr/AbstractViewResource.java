package biz.paluch.visualizr;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import biz.paluch.visualizr.spi.DataSourceProvider;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 11:26
 */
@Path("view")
public abstract class AbstractViewResource {

    private VelocityEngine velocityEngine;

    @PostConstruct
    public void postConstruct() throws Exception {
        Properties p = new Properties();
        p.load(getClass().getResourceAsStream("/velocity.properties"));
        velocityEngine = new VelocityEngine(p);
        velocityEngine.init();
    }

    @GET
    @Path("single.html")
    @Produces(MediaType.TEXT_HTML)
    public String getView() throws Exception {

        if (velocityEngine == null) {
            postConstruct();
        }

        Template template = velocityEngine.getTemplate("templates/view.html.vm");
        Context ctx = new VelocityContext();

        populateContext(ctx);
        ctx.put("dataSources", getDataSourceProvider().getDataSources());

        StringWriter writer = new StringWriter();
        template.merge(ctx, writer);

        return writer.toString();
    }

    public void populateContext(Context ctx) {
        ctx.put("renderDatePicker", true);
        ctx.put("renderDataSourcePicker", true);

        boolean localFrameworkResources = isLocalHostedJsFrameworksAvailable();
        ctx.put("localFrameworkResources", localFrameworkResources);
    }

    private boolean isLocalHostedJsFrameworksAvailable() {
        try (InputStream is = getClass().getResourceAsStream("/resources/js/jquery.min.js")) {
            if (is == null) {
                return false;
            }
        } catch (IOException e) {
            // ignore
        }

        return true;
    }

    public abstract DataSourceProvider getDataSourceProvider();
}
