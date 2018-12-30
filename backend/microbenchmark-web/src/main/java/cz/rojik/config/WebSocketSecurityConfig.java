package cz.rojik.config;

import cz.rojik.backend.entity.RoleType;
import cz.rojik.constants.MappingURLConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
//                .simpSubscribeDestMatchers(MappingURLConstants.BENCHMARK_WEBSOCKET + "/**").permitAll()
                .simpSubscribeDestMatchers(MappingURLConstants.BENCHMARK_WEBSOCKET + "/**").access("not( hasRole('"+ RoleType.DEMO.getRoleType() + "') )")
                .simpDestMatchers(MappingURLConstants.APP_WEBSOCKET + "/**").access("not( hasRole('"+ RoleType.DEMO.getRoleType() + "') )");
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}