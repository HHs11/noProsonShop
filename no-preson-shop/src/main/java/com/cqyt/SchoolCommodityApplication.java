package com.cqyt;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolCommodityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolCommodityApplication.class, args);
    }

//    @Bean
//    public Connector connector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        // 监听的http端口
//        connector.setPort(80);
//        connector.setSecure(false);
//        // 监听到http端口后跳转的https端口
//        connector.setRedirectPort(443);
//        return connector;
//    }
//
//    /**
//     * 拦截所有的请求
//     */
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addMethod("post");
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(connector);
//        return tomcat;
//    }
}
