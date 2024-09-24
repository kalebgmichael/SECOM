//package com.productcnit.config;
////
////import com.sun.security.auth.UserPrincipal;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.http.server.ServerHttpRequest;
////import org.springframework.web.socket.WebSocketHandler;
////import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
////
////import java.security.Principal;
////import java.util.Map;
////import java.util.UUID;
////
////public class UserHandshakeHandler extends DefaultHandshakeHandler {
////    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);
////
////    @Override
////    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
////        final String randomId = UUID.randomUUID().toString();
////        LOG.info("User with ID '{}' opened the page", randomId);
////
////        return new UserPrincipal(randomId);
////    }
////}
//import org.keycloak.KeycloakPrincipal;
//import org.keycloak.KeycloakSecurityContext;
//import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//
//public class UserHandshakeHandler extends DefaultHandshakeHandler {
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof KeycloakPrincipal) {
//            KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
//            String userId = keycloakPrincipal.getKeycloakSecurityContext().getToken().getSubject();
//            return new KeycloakPrincipal<>(userId, keycloakPrincipal.getKeycloakSecurityContext());
//        } else {
//            // Handle other authentication mechanisms or return null if not authenticated
//            return null;
//        }
//    }
//}
//
