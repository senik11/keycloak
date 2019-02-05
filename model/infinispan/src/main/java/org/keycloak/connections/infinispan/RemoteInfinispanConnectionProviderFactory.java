package org.keycloak.connections.infinispan;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import javax.naming.InitialContext;

/**
 * @author Arseny Krasenkov {@literal <akrasenkov@at-consulting.ru>}
 */
public class RemoteInfinispanConnectionProviderFactory implements InfinispanConnectionProviderFactory {

    private static final String ID = "remote-infinispan";

    private static final Logger logger = Logger.getLogger(RemoteInfinispanConnectionProviderFactory.class);

    private RemoteCacheManager cacheManager;
    private Config.Scope providerConfig;
    
    @Override
    public InfinispanConnectionProvider create(KeycloakSession session) {
        lazyInit();
        // TODO: Return real provider
        return null;
    }

    private void lazyInit() {
        if (cacheManager == null) {
            synchronized (this) {
                if (cacheManager == null) {
                    String cacheContainer = providerConfig.get("cacheContainer");
                    initContainerManaged(cacheContainer);
                    //TODO: Add support for "embedded" config
                }
            }
        }
    }

    private void initContainerManaged(String lookup) {
        try {
            cacheManager = (RemoteCacheManager) new InitialContext().lookup(lookup);
            logger.debugv("Using container-managed remote Infinispan cache contaienr, lookup={0}", lookup);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve cache container", e);
        }
    }

    @Override
    public void close() {
        cacheManager = null;
    }

    @Override
    public void init(Config.Scope config) {
        this.providerConfig = config;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

}
