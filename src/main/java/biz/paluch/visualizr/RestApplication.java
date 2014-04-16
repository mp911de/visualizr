package biz.paluch.visualizr;

import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.providers.InputStreamProvider;
import org.jboss.resteasy.plugins.providers.StringTextStar;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:mark.paluch@1und1.de">Mark Paluch</a>
 * @since 13.04.14 10:45
 */
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Sets.newHashSet(ChartDataResource.class, ChartResource.class, ContentResource.class, ViewResource.class,
                InputStreamProvider.class, StringTextStar.class);
    }
}
