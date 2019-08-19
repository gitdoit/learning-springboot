## 授权码模式

1. 客户端将用户重定向到服务器，申请访问码

   访问URL：http://localhost/oauth/authorize?response_type=code&client_id=client1&redirect_uri=http://baidu.com

   1. 服务器需要对 /auth/**路径进行权限验证
   2. 如果用户没有在服务器登陆，则需要登陆验证，之后重定向到刚才想要访问的那个路径
   3. 服务端需要有参数中client_id指定的客户端的信息，比如权限，注册的重定向URL，并且存储客户端URL必须和这个请求中的redirect_url一样。
   4. 客户端信息校验完毕后，会显示一个是否授权该客户端[权限1，权限2...]的提示，点击确定，则用户被重定向到redirect_url并且追加一个code=xxxx一般这个参数就是客户端的回调地址
   5. 客户端在接收到回调之后，拿到code，使用post请求带上code\redirect_url\grant_type\client_id等去请i去localhost/oauth/token这个接口，就可以换到token了，然后客户端每次请求都带着这个token就可以访问这个用户的资源了

2. http://localhost/users/me?access_token=5211434c-f32f-481b-a73c-2a6e8532bc55















## 客户端模式

1. **令牌申请**

   请求：

   http://localhost/oauth/token?grant_type=client_credentials&scope=delete&client_id=client&client_secret=123

   响应：

   ```json
   {
   "access_token": "043c5ab6-065b-4fea-85e1-a26e23d804f4",
   "token_type": "bearer",
   "expires_in": 43109,
   "scope": "delete"
   }
   ```

2. 资源访问

   ​	http://localhost/users/me?access_token=043c5ab6-065b-4fea-85e1-a26e23d8



### 令牌申请细节

1. 请求路径/oauth/token 被过滤器 ClientCredentialsTokenEndpointFilter所拦截，这个过滤器跟之前的UsernamePasswordAuthenticationFilter都是继承的一个抽象父类。

   >​	这个Filter的作用就是在**请求到达认证端点之前**，使用请求中的客户端id，和密码从你配置的ProviderManager(跟Spring security的不是同一个)进行认证，认证成功后把填充后的认证信息放到SecurityContextHolder里面，认证失败抛出没有用户名异常或者密码错误异常。

   **ClientCredentialsTokenEndpointFilter**：拦截请求：/oauth/token

```java
		throws AuthenticationException, IOException, ServletException {

		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");

		// If the request is already authenticated we can assume that this
		// filter is not needed
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication;
		}

		clientId = clientId.trim();
         // 这个用的AuthenticationToken类型跟表单认证用户的是一样的
         // 不过这里把客户端id当成用户名填进去，客户端secret填到password
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,
				clientSecret);
		// 也是调用AuthenticationManager，注意Oauth模块跟spring securit主模块用的不是同一个
        // 不然的话就跑到用户认证那里了
		return this.getAuthenticationManager().authenticate(authRequest);
```

2. **TokenEndpoint**处理请求

   > 请求在经过Filter校验客户端ID和密码校验成功之后，会放行到Token处理端点TokenEndpoint即: /oauth/token

```java
@RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
	public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
	Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
		//...省略
        // 再查一次
		String clientId = getClientId(principal);
		ClientDetails authenticatedClient = 	         					getClientDetailsService().loadClientByClientId(clientId);
		// 创建TokenRequest
		TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
		
        //... 省略一些校验判断
        
        //创建一个令牌
		OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
		// 返回令牌
		return getResponse(token);

	}
```

3. **TokenGranter**创建令牌

   >​            在端点进行一些判断之后会调用**CompositeTokenGranter**这个类创建Token，看名字就知道它组合了TokenGranter，实际的Token创建工作就是交给这些TokenGranter实现类去创建的，大体流程就是判断客户端使用哪种模式(密码、授权码、简单模式等)，这几种模式有对应的TokenGranter实现，令牌的创建方式也不同。

```java
public abstract class AbstractTokenGranter implements TokenGranter {
    // token相关service
	private final AuthorizationServerTokenServices tokenServices;
    // clientDetailsService
	private final ClientDetailsService clientDetailsService;
	// 创建OauthRequest工厂
	private final OAuth2RequestFactory requestFactory;
	// 授权类型
	private final String grantType;

    //
	public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        // ...
		String clientId = tokenRequest.getClientId();
		ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
		validateGrantType(grantType, client);
		return getAccessToken(client, tokenRequest);
	}

	protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
		return tokenServices.createAccessToken(getOAuth2Authentication(client, tokenRequest));
	}

	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		OAuth2Request storedOAuth2Request = requestFactory.createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, null);
	}
}
```



### 资源访问细节

> ​          上面的令牌申请请求已经经过了，此时可以使用得到的令牌访问受保护的资源。资源访问请求首先会到达**OAuth2AuthenticationProcessingFilter**，**它的作用就是从请求中提取令牌，使用令牌查询客户端的Authentication**然后放入安全上下文中，供后面的权限校验使用。
>
> 这个Filter的描述如下：
>
> ​	OAuth2保护资源的预先认证过滤器。如果与OAuth2AuthenticationManager结合使用，则会从到来的请求之中提取一个OAuth2 token，之后使用OAuth2Authentication来填充Spring Security上下文。



1. 根据Token获取客户端信息

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		final boolean debug = logger.isDebugEnabled();
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		try {
			// 就是根据请求中的Token参数，new一个PreAuthenticatedAuthenticationToken
            // 跟UsernamePasswordAuthenticationToken差不多的东西，然后下面对这个东西填充
			Authentication authentication = tokenExtractor.extract(request);
			if (authentication == null) {
				if (stateless && isAuthenticated()) {
					SecurityContextHolder.clearContext();
				}
			}
			else {
                // 设置一个Principal
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
				if (authentication instanceof AbstractAuthenticationToken) {
					AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken) authentication;
			needsDetails.setDetails(authenticationDetailsSource.buildDetails(request));
				}
                // 调用OAuth2AuthenticationManager进行认证,步骤2详解
				Authentication authResult = authenticationManager.authenticate(authentication);
                // 发布认证成功事件
				eventPublisher.publishAuthenticationSuccess(authResult);
                // 上下文中设置认证信息
				SecurityContextHolder.getContext().setAuthentication(authResult);
			}
		}
		catch (OAuth2Exception failed) {
            // 清空上下文
			SecurityContextHolder.clearContext();
            // 发布认证失败事件
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));
			// 进入认证失败接入点
			authenticationEntryPoint.commence(request, response,
					new InsufficientAuthenticationException(failed.getMessage(), failed));
			return;
		}
        // 执行其他Filter
		chain.doFilter(request, response);
	}
```

2. 根据客户端信息校验权限

   >这里的认证器使用的是**OAuth2Authentication**，它的作用使用令牌查出这个令牌对应的客户端信息。
   >
   >而之前的**ProviderManager**则是在**登陆**的时候根据用户名密码校验身份。他俩的区别就很明显，一个用于登陆，一个用户在携带token访问资源的时候根据token查用户信息。
   >
   >

   ```java
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
   		// 获取请求中携带的Token
   		String token = (String) authentication.getPrincipal();
       	// 根据Token查询认证信息
   		OAuth2Authentication auth = tokenServices.loadAuthentication(token);
   		Collection<String> resourceIds = auth.getOAuth2Request().getResourceIds();
   		
       	//...省略根据资源ID鉴定该客户端是否有权限访问
       
       	// 查询客户端信息，再判断它申请的权限是否合法
   		checkClientDetails(auth);
   		// 填充
   		auth.setDetails(authentication.getDetails());
       	// 已通过
   		auth.setAuthenticated(true);
   		return auth;
   	}
   ```
