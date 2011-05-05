package org.concord.datagraph.analysis;

import java.util.Iterator;

public abstract class GraphAnalyzerProvider {
    public static enum Type { ANY, NETLOGO }
    public abstract GraphAnalyzer getAnalyzer(Type preferredType);
    
    @SuppressWarnings("restriction")
    public static GraphAnalyzer findAnalyzer(Type preferredType) {
        Iterator<?> providers = sun.misc.Service.providers(GraphAnalyzerProvider.class);
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
