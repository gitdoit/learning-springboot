# Spring Security 核心过滤器概述

​     首先看一下Spring帮我们自动配置了下面这些过滤器，我们只挑其中比较重要的来讲

| Alias                        | Filter Class                                        | Namespace Element or Attribute         |
| ---------------------------- | --------------------------------------------------- | -------------------------------------- |
| CHANNEL_FILTER               | ChannelProcessingFilter                             | http/intercept-url@requires-channel    |
| SECURITY_CONTEXT_FILTER      | SecurityContextPersistenceFilter                    | http                                   |
| CONCURRENT_SESSION_FILTER    | ConcurrentSessionFilter                             | session-management/concurrency-control |
| HEADERS_FILTER               | HeaderWriterFilter                                  | http/headers                           |
| CSRF_FILTER                  | CsrfFilter                                          | http/csrf                              |
| LOGOUT_FILTER                | LogoutFilter                                        | http/logout                            |
| X509_FILTER                  | X509AuthenticationFilter                            | http/x509                              |
| PRE_AUTH_FILTER              | AstractPreAuthenticatedProcessingFilter  Subclasses | N/A                                    |
| CAS_FILTER                   | CasAuthenticationFilter                             | N/A                                    |
| FORM_LOGIN_FILTER            | UsernamePasswordAuthenticationFilter                | http/form-login                        |
| BASIC_AUTH_FILTER            | BasicAuthenticationFilter                           | http/http-basic                        |
| SERVLET_API_SUPPORT_FILTER   | SecurityContextHolderAwareRequestFilter             | http/@servlet-api-provision            |
| JAAS_API_SUPPORT_FILTER      | JaasApiIntegrationFilter                            | http/@jaas-api-provision               |
| REMEMBER_ME_FILTER           | RememberMeAuthenticationFilter                      | http/remember-me                       |
| ANONYMOUS_FILTER             | AnonymousAuthenticationFilter                       | http/anonymous                         |
| SESSION_MANAGEMENT_FILTER    | SessionManagementFilter                             | session-management                     |
| EXCEPTION_TRANSLATION_FILTER | ExceptionTranslationFilter                          | http                                   |
| FILTER_SECURITY_INTERCEPTOR  | FilterSecurityInterceptor                           | http                                   |
| SWITCH_USER_FILTER           | SwitchUserFilter                                    | N/A                                    |

### 1. SecurityContextPersistenceFilter

>​	主要职责又两个：当请求来临时，创建SecurityContext安全上下文，并绑定到线程中，请求结束时清空它。
>
>​	如果不适用Spring Security保存用户信息，大部分都是使用Session，在 Spring Security中也是如此，微服务的一个设计理念就是通讯无状态，而Http协议中的无状态意味着不允许存在Session，所以可以通过setAllowSessionCreation(false)实现，这并不意味着这个过滤器就无用了，它还负责清除用户信息，在Spring Security中虽然安全上下文信息被存储在Session中，但我们在实际使用中不应该直接操作Session，而是使用SecurityContextHolder。

```java
//	安全上下文仓库
public SecurityContextPersistenceFilter(SecurityContextRepository repo) {
		this.repo = repo;
	}

public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 确定一个请求只创建一个安全上下文
		if (request.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(request, response);
			return;
		}
		request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		// 执行过滤器前会话是否可用，默认false
		if (forceEagerSessionCreation) {
			HttpSession session = request.getSession();
		}
		// 包装Request Response
		HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request,
				response);
    	// 获取安全上下文
		SecurityContext contextBeforeChainExecution = repo.loadContext(holder);

		try {
            // 请求开始时设置安全上下文，这样避免了用户直接从Session中取
			SecurityContextHolder.setContext(contextBeforeChainExecution);
			chain.doFilter(holder.getRequest(), holder.getResponse());
		}
		finally {
            // 请求结束后清空上下文
			SecurityContext contextAfterChainExecution = SecurityContextHolder
					.getContext();
			SecurityContextHolder.clearContext();
			repo.saveContext(contextAfterChainExecution, holder.getRequest(),
					holder.getResponse());
			request.removeAttribute(FILTER_APPLIED);
		}
	}
```



### 2.UsernamePasswordAuthenticationFilter

>​	表单认证是最常见的认证方式，最常见的场景就是允许用户在表单中输入账号密码进行登陆。前面说过它是通过调用Http.formLogin()方法产生的。它的职责就是根据路径拦截登陆的表单提交请求，调用AuthenticationManager进行登陆认证。那么这里看一下它的调用逻辑。

1. 首先过滤入口在它的父类**AbstractAuthenticationProcessingFilter**中

   ```java
   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
   			throws IOException, ServletException {
   		HttpServletRequest request = (HttpServletRequest) req;
   		HttpServletResponse response = (HttpServletResponse) res;
       	// 判断是不是表单提交的请求，就是你设置的http.formLogin().loginProcessingUrl("xx")
   		if (!requiresAuthentication(request, response)) {
   			chain.doFilter(request, response);
   			return;
   		}
   		Authentication authResult;
   		try {
               // 这里就会调用到子类UsernamePasswordAuthenticationFilter的认证方法了，下面说
   			authResult = attemptAuthentication(request, response);
               // 判断是否认需要继续认证，根据接口的定义认证不成功应该抛出异常！
   			if (authResult == null) {
   				return;
   			}
   			sessionStrategy.onAuthentication(authResult, request, response);
   		}
       	// 内部服务异常,例如认证服务不可用啥的
   		catch (InternalAuthenticationServiceException failed) {
   			unsuccessfulAuthentication(request, response, failed);
   			return;
   		}
       	// 认证异常，如没有该用户，密码错误等异常
   		catch (AuthenticationException failed) {
               // 清空安全上下文，记住我里面的清空Cookie啥的，还有执行failureHandler
   			unsuccessfulAuthentication(request, response, failed);
   			return;
   		}
   		// 认证成功
   		if (continueChainBeforeSuccessfulAuthentication) {
   			chain.doFilter(request, response);
   		}
       	// 设置安全上下文，调用记住我逻辑，发布认证成功事件，执行successHandler
   		successfulAuthentication(request, response, chain, authResult);
   	}
   ```

   2. 执行**UsernamePasswordAuthenticationFilter.attemptAuthentication(request, response)**

      > ​	这里的逻辑就很简单了，根据接口声明，子类应该完成这三件事.
      >
      > 1.为经过身份验证的用户返回已填充的身份验证令牌，表示身份验证成功。
      >
      > 2.返回null，表示身份验证过程仍在进行中。, 在返回之前，实现应该执行完成该过程所需的任何其他工作。
      >
      > 3.如果身份验证过程失败，则抛出AuthenticationException

   ```java
   public Authentication attemptAuthentication(HttpServletRequest request,
   			HttpServletResponse response) throws AuthenticationException {
       	// 获取设置的用户名密码参数名称，默认是 username password
   		String username = obtainUsername(request);
   		String password = obtainPassword(request);
   		username = username.trim();
       	// 创建一个空的AuthenticationToken
   		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
   				username, password);
   		// 初始化，可以自定义
   		setDetails(request, authRequest);
   		// 调用AuthenticationManager开始认证，这里不再继续下去，因为属于另一码事了
       	// 这里做的逻辑就是上面说的那三个。
   		return this.getAuthenticationManager().authenticate(authRequest);
   	}
   ```



### AnonymousAuthenticationFilter

>​	匿名认证过滤器，看这个名字有点奇怪，怎么匿名还需要认证过滤。其实Spring Security就是把没有登陆的匿名用户也当作一个拥有角色、权限的普通用户来看待。这样在处理逻辑上可以贯穿下去，不用特殊处理。这个过滤器在UserNamePasswordAuthenticationFilter、BasecAutchenticationFilter、RememberMeAuthenticationFilter之后。目的就是经过这三个过滤器你都没有身份，那么就给你一个匿名身份。

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// 如果上下文中没有认证信息
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // 创建一个匿名的身份(AnonymousAuthenticationToken)放到上下文中
			SecurityContextHolder.getContext().setAuthentication(
					createAuthentication((HttpServletRequest) req));
		}
		else {
			// log
		}
		chain.doFilter(req, res);
	}
```



### ExceptionTranslationFilter

>​	这个过滤器通常应该在整个过滤链的后面，目的就是将过滤过程中产生的异常翻译成需要向客户端响应的信息。例如登陆验证的时候产生的登陆失败(AuthenticationException)异常，它会交给内部的AuthenticationEntryPoint处理。
>
>Doc：
>
>​	处理在过滤器链中抛出的任何AccessDeniedException和AuthenticationException。,此过滤器是必需的，因为它提供了Java异常和HTTP响应之间的桥梁。 它仅关注维护用户界面。此筛选器不执行任何实际的安全性。
>
>​	如果检测到AuthenticationException，则筛选器将启动authenticationEntryPoint。这允许共同处理源自AbstractSecurityInterceptor的任何子类的身份验证失败。
>
>​	如果检测到AccessDeniedException，则筛选器将确定该用户是否是匿名用户。, 如果他们是匿名用户，则将启动authenticationEntryPoint。,如果他们不是匿名用户，则过滤器将委托给AccessDeniedHandler。默认情况下，过滤器将使用AccessDeniedHandlerImpl。
>
>要使用此过滤器，必须指定以下属性
>
> 1. authenticationEntryPoint 
>
>    ​	指示在检测到AuthenticationException时应开始身份验证过程的处理程序。 请注意，这也可以将当前协议从http切换为https以进行SSL登录。
>
> 2. requestCache 
>
>    ​	确定在身份验证过程中用于保存请求的策略，用户在登陆成功后可以跳回之前访问拒绝的页面。默认实现是HttpSessionRequestCache。

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			chain.doFilter(request, response);
		}catch (Exception ex) {
			// 尝试从异常栈中提取SpringSecurityException
			//...省略
			if (ase != null) {
                 // 处理SpringSecurityException
				handleSpringSecurityException(request, response, chain, ase);
			}
			else {
				// 抛出一个运行时异常
			}
		}
	}
```

```java
private void handleSpringSecurityException(request,response,chain,exception) {
    	// 判断是否是认证异常
		if (exception instanceof AuthenticationException) {
            // 重定向到认证端点
			sendStartAuthentication(request, response, chain, exception);
		}
    	// 是否为访问拒绝
		else if (exception instanceof AccessDeniedException) {
            // 拿到认证信息
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 判断是不是匿名用户,是的话重定向到认证端点
			if (authenticationTrustResolver.isAnonymous(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
				sendStartAuthentication(request,response,chain,
						new InsufficientAuthenticationException(...));
			}
            // 已经登陆，但是没有权限访问
			else {
                 // 执行访问拒绝处理
				accessDeniedHandler.handle(request, response, exception);
			}
		}
	}
```





### FilterSecurityInterceptor

>  	​	整个认证流程走通后，开始要对用户想要访问的资源进行鉴权了。主要就是FilterSecurityInterceptor从SecurityContextHolder中获取Authentication对象，然后比对用户拥有的权限和资源所需的权限。前者可以通过Authentication对象直接获得，而后者则需要引入我们之前一直未提到过的两个类：SecurityMetadataSource，AccessDecisionManager。理解清楚决策管理器的整个创建流程和SecurityMetadataSource的作用需要花很大一笔功夫，这里，暂时只介绍其大概的作用。