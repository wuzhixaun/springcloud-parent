package com.wuzx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer // 开启认证服务器功能
public class OauthServerConfiger extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * 认证服务器最终是以api接口的方式对外提供服务(校验合法性并生成令牌、校验令牌等)
     * 那么，以api接口方式对外的话，就涉及到接口的访问权限，我们需要在这里进行必要的配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        // 允许客户端的表单认证
        security.allowFormAuthenticationForClients()
                // 开启端口 /oauth/token_key的访问权限(允许)
                .tokenKeyAccess("permitAll()")
                // 开启端口 /oauth/check_token的访问权限(允许)
                .checkTokenAccess("permitAll()");
    }

//    /**
//     * 从内存客户端详情配置，
//     *
//     * @param clients
//     * @throws Exception
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        super.configure(clients);
//        clients.inMemory() // 客户端信息存储在什么地方，可以在内存中，可以在数据库里
//                .withClient("client_lagou") // 添加一个client配置,指定id
//                .secret("abcxyz") // 指定客户端的密码/安全码
//                .resourceIds("autodeliver") // 指定客户端 所能访问资源id清单，此处的资源id是需要在具体的资源服务器上也配置一样
//                .authorizedGrantTypes("password", "refresh_token") // 认证类型/令牌颁发模式，可以配置多个在这里，但是不一定 都用，具体使用哪种方式颁发token，需要客户端调用的时候传递参数指定
//                .scopes("all"); // 客户端的权限范围，此处配置为all全部即可
//
//    }


    /**
     * 从数据库
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);

        clients.withClientDetails(clientDetailsService());
    }



    @Autowired
    private DataSource dataSource;


    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 认证服务器是玩转token的，那么这里配置token令牌管理相关(token此时就是一个字符串，当下的token需要在服务器端存储，
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);

        endpoints.tokenStore(tokenStore())// 指定token的存储方法
                .tokenServices(authorizationServerTokenServices()) // token服 务的一个描述，可以认为是token生成细节的描述，比如有效时间多少等
                .authenticationManager(authenticationManager) // 指定认证管理器，随后注入一个到当前类使用即可
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 该方法用于创建tokenStore对象(令牌存储对象)
     * @return
     */
    public TokenStore tokenStore(){
//        return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    private String sign_key = "wuzx";
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(sign_key); // 签名秘钥
        converter.setVerifier(new MacSigner(sign_key)); // 验证时使用的秘钥，和签名秘钥保持一致

        return converter;
    }


    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true); // 是 否开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore());


        // 针对jwt令牌添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        // 设置令牌有效时间(一般设置为2个小时)
        defaultTokenServices.setAccessTokenValiditySeconds(2000); // access_token就是我们请求资源需要携带的令牌
        // 设置刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200); // 3天
        return defaultTokenServices;
    }

}
