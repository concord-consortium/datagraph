package org.concord.datagraph.analysis;

import java.util.Iterator;

import javax.imageio.spi.ServiceRegistry;

public abstract class GraphAnalyzerProvider {
    public static enum Type { ANY, NETLOGO }
    public abstract GraphAnalyzer getAnalyzer(Type preferredType);
    
    @SuppressWarnings("restriction")
    public static GraphAnalyzer findAnalyzer(Type preferredType) {
        // ServiceRegistry actually just wraps sun.misc.Service.providers(), and doesn't require compiler tweaks.
        // If we ever move to 1.6, this should get changed to use java.util.ServiceLoader
        Iterator<?> providers = ServiceRegistry.lookupProviders(GraphAnalyzerProvider.class);
        while (providers.hasNext()) {
            GraphAnalyzerProvider gas = (GraphAnalyzerProvider)providers.next();
            GraphAnalyzer ga = gas.getAnalyzer(Type.ANY);
            if (ga != null) {
                return ga;
            }
        }
        return null;
    }
}
