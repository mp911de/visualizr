package biz.paluch.visualizr;

import com.google.common.collect.Sets;
import org.jboss.resteasy.plugins.providers.InputStreamProvider;
import org.jboss.resteasy.plugins.providers.StringTextStar;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 13.04.14 10:45
 */
@ApplicationPath("visualizr")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Sets.newHashSet(ChartDataResource.class, ChartResource.class, ContentResource.class, ViewResource.class,
                InputStreamProvider.class, StringTextStar.class);
    }
}
