package biz.paluch.visualizr;

import java.io.InputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 10:50
 */
@Path("view")
public class ContentResource {

    @GET
    @Path("{name:.*}.js")
    @Produces("text/javascript")
    public InputStream getJs(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".js");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.eot")
    @Produces("application/vnd.ms-fontobject")
    public InputStream getEot(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".eot");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.ttf")
    @Produces("application/octet-stream")
    public InputStream getTtf(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".ttf");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.woff")
    @Produces("application/x-font-woff")
    public InputStream getWoff(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".woff");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.png")
    @Produces("image/png")
    public InputStream getPng(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".png");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.gif")
    @Produces("image/gif")
    public InputStream getGif(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".gif");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

    @GET
    @Path("{name:.*}.css")
    @Produces("text/css")
    public InputStream getCss(@PathParam("name") String name) {

        InputStream is = getClass().getResourceAsStream("/resources/" + name + ".css");
        if (is == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return is;
    }

}
