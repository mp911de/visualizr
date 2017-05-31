package biz.paluch.visualizr;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * Proxy for bundled resources.
 *
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 10:50
 */
@Path("view")
public class ContentResource {

    @GET
    @Path("{name:.*}.js")
    @Produces("text/javascript")
    public InputStream getJs(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".js");
    }

    @GET
    @Path("{name:.*}.eot")
    @Produces("application/vnd.ms-fontobject")
    public InputStream getEot(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".eot");
    }

    @GET
    @Path("{name:.*}.ttf")
    @Produces("application/octet-stream")
    public InputStream getTtf(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".ttf");
    }

    @GET
    @Path("{name:.*}.woff")
    @Produces("application/x-font-woff")
    public InputStream getWoff(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".woff");
    }

    @GET
    @Path("{name:.*}.png")
    @Produces("image/png")
    public InputStream getPng(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".png");
    }

    @GET
    @Path("{name:.*}.gif")
    @Produces("image/gif")
    public InputStream getGif(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".gif");
    }

    @GET
    @Path("{name:.*}.css")
    @Produces("text/css")
    public InputStream getCss(@PathParam("name") String name) {
        return getResource("/resources/" + name + ".css");
    }

    private static InputStream getResource(String name) {

        InputStream is = ContentResource.class.getResourceAsStream(name);

        if (is == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        return is;
    }

}
