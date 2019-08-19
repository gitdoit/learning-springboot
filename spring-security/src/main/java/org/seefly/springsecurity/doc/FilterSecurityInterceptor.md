### FilterSecurityInterceptor  extends AbstractSecurityInterceptor implements Filter

> ​	整个认证流程走通后，开始要对用户想要访问的资源进行鉴权了。主要就是FilterSecurityInterceptor从SecurityContextHolder中获取Authentication对象，然后比对用户拥有的权限和资源所需的权限。前者可以通过Authentication对象直接获得，而后者则需要引入我们之前一直未提到过的两个类：SecurityMetadataSource，AccessDecisionManager。理解清楚决策管理器的整个创建流程和SecurityMetadataSource的作用需要花很大一笔功夫，这里，暂时只介绍其大概的作用。



#### void invoke

> 到达FilterSecurityInterceptor过滤器先进入这个方法，传参是请求+响应+过滤器链的结合体

```java
public void invoke(FilterInvocation fi) throws IOException, ServletException {
	// 判断是否需要多次校验	
    if ((fi.getRequest() != null)
				&& (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
				&& observeOncePerRequest) {
			// filter already applied to this request and user wants us to observe
			// once-per-request handling, so don't re-do security checking
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}
		else {
			// 第一次请求，则准备安全校验
			if (fi.getRequest() != null && observeOncePerRequest) {
                // 置状态位
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}
            // 调用父类的beforeInvocation
			InterceptorStatusToken token = super.beforeInvocation(fi);
            // 执行其他的过滤器，按理说这个过滤器在过滤器链的末端了，再执行就不是安全模块的了
			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			}
			finally {
				super.finallyInvocation(token);
			}
			super.afterInvocation(token, null);
		}
	}
```



#### protected InterceptorStatusToken beforeInvocation(Object object)

> ​	请访问者请求的资源进行权限校验，一般来说，请求到了这一层，再往下就是受保护的资源了。而这一层所做的工作就是对来访者进行身份验证，而来访者的身份信息是放在安全上下文中的，而安全上下文则是依靠Session来保存认证信息。所以这一层所做的工作就是从上下文中拿出认证信息，和来访者所请求的资源所需权限进行验证。
>
> 关键步骤
>
> // 通过请求的资源路径，获取访问该资源所需的权限。
>
> 1、obtainSecurityMetadataSource().getAttributes(object);
>
> //  根据来访者身份，该资源安全配置进行决策，是否允许访问。
>
> 2、accessDecisionManager.decide(authenticated, object, attributes);

```java
protected InterceptorStatusToken beforeInvocation(Object object) {
    
		// ....
    	
    	// 根据请求的资源路径，获取在该资源上配置的安全策略，如:需要登陆、需要XX权限、拒绝所有等
		Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource()
				.getAttributes(object);
		// 如果请求的资源没有安全配置，而且配置了拒绝公开访问，就抛出异常
		if (attributes == null || attributes.isEmpty()) {
			if (rejectPublicInvocations) {
				throw new IllegalArgumentException(XXX);
			}
			publishEvent(new PublicInvocationEvent(object));
			return null; // no further work post-invocation
		}
		
    	// 从安全上下文中拿出认证信息，到这里
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			credentialsNotFound(messages.getMessage(
					"AbstractSecurityInterceptor.authenticationNotFound",
					"An Authentication object was not found in the SecurityContext"),
					object, attributes);
		}
		// 判断是否需要认证(若设置了每次请求都要认证，则会调用认证管理器认证一下)
    	// 否则直接从安全上下文中获取
		Authentication authenticated = authenticateIfRequired();

		// 进入决策管理，判断请求者和访问的路径
		try {
			this.accessDecisionManager.decide(authenticated, object, attributes);
		}
		catch (AccessDeniedException accessDeniedException) {
			publishEvent(new AuthorizationFailureEvent(object, attributes, a						uthenticated,accessDeniedException));
			throw accessDeniedException;
		}
		// 发布认证成功信息
		if (publishAuthorizationSuccess) {
			publishEvent(new AuthorizedEvent(object, attributes, authenticated));
		}
		// Attempt to run as a different user
		Authentication runAs = this.runAsManager.buildRunAs(authenticated, object,
				attributes);
		
		if (runAs == null) {
			if (debug) {
				logger.debug("RunAsManager did not change Authentication object");
			}

			// no further work post-invocation
			return new InterceptorStatusToken(SecurityContextHolder.getContext(), false,
					attributes, object);
		}
		else {
			SecurityContext origCtx = SecurityContextHolder.getContext();
		SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
			SecurityContextHolder.getContext().setAuthentication(runAs);
			// need to revert to token.Authenticated post-invocation
			return new InterceptorStatusToken(origCtx, true, attributes, object);
		}
	}
```

