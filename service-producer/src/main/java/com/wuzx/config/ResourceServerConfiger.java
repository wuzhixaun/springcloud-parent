package com.wuzx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**
 *
 */
@Configuration
@EnableResourceServer // 开启资源服务器功能
@EnableWebSecurity // 开启web访问安全
public class ResourceServerConfiger extends ResourceServerConfigurerAdapter {

    public ResourceServerConfiger() {
        super();
    }

    /**
     * jwt令牌改造
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("autodeliver")
                .tokenStore(tokenStore())
                .stateless(true) // 无状态设置
        ;
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



    /**
     * 该方法用于定义资源服务器向远程认证服务器发起请求，进行token校验等事宜
     *
     * @param resources
     * @throws Exception
     */
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        // 设置当前资源服务的资源id
//        resources.resourceId("autodeliver");
//
//        // 定义token服务对象(token校验就应该靠token服务对象)
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//
//        // 校验端点/接口设置
//        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/oauth/check_token");
//
//        remoteTokenServices.setClientId("client_lagou");
//        remoteTokenServices.setClientSecret("abcxyz");
//        // 别忘了这一步
//        resources.tokenServices(remoteTokenServices);
//    }



    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests().antMatchers("/resumer/**").authenticated()
                .antMatchers("/demo/**").authenticated()
                .anyRequest().permitAll(); // 其他请求方向
    }
}
