package cz.rojik.config;

import cz.rojik.backend.enums.RoleTypeEnum;
import cz.rojik.constants.MappingURLConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpSubscribeDestMatchers(MappingURLConstants.BENCHMARK_WEBSOCKET + "/**").access("not( hasRole('"+ RoleTypeEnum.DEMO.getRoleType() + "') )")
                .simpDestMatchers(MappingURLConstants.APP_WEBSOCKET + "/**").access("not( hasRole('"+ RoleTypeEnum.DEMO.getRoleType() + "') )");
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}