## springSecurity源码笔记

1. 继承**WebSecurityConfigurerAdapter**类，并在配置类上添加**@EnableWebSecurity**注解
2. 该注解引入了**WebSecurityConfiguration**类



### WebSecurityConfiguration类

> ​     使用WebSecurity创建FilterChainProxy，为Spring Security执行基于Web的安全性。, 然后它导出必要的bean。, 可以通过扩展WebSecurityConfigurerAdapter并将其公开为配置或实现WebSecurityConfigurer并将其作为配置公开来对WebSecurity进行自定义。, 使用EnableWebSecurity时会导入此配置

1. **setFilterChainProxySecurityConfigurer** 方法，在它的参数上可以看到

![1544776914711](https://i.imgur.com/GFhBK3i.png)

它使用@Value+SpEL表达式引入了一个参数，作用就是获得所有实现了**WebSecurityConfigurer**接口的bean，最终的获取的逻辑如下。

![1544777009448](https://i.imgur.com/UxKJz43.png)

​        获取到这些实现类之后，会通过他们的Order对他们进行排序，并校验这些实现类的Order优先级不能相	同。

​	然后将这些已经排序的**WebSecurityConfigurer**接口实现类通过**WebSecurity**的**apply**方法保存到自身WebSecurity



2. **springSecurityFilterChain**方法

   > ​        主要就是判断上一步有没有获取到webSecurityConfigurers ，如果没有则自己创建一个，并放入WebSecurity中。最后都会执行webSecurity.build()方法，这个方法最终会产生一个**springSecurityFilterChain**它是整个spring sercurity的核心

   ![](https://i.imgur.com/YgSdx3K.png)

------



## WebSecurity

1. 继承关系

![](https://i.imgur.com/vMqFASY.png)

2. ### webSercurity.build()

   > ​      方法最终调用到的是它继承的父类AbstractConfiguredSecurityBuilder.doBuild()方法
   >
   > 这个方法执行了三个重要的方法如下：1、init() 2、configure()  3、performBuild()。

   1. **AbstractConfiguredSecurityBuilder.init()**

      > 它所做的就是，将之前获取到的SecurityConfigurer，循环调用它们的init方法。

      ```java
      private void init() throws Exception {
      		Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();
      		for (SecurityConfigurer<O, B> configurer : configurers) {
                  // 实际调用WebSecurityConfigurerAdapter.init()
      			configurer.init((B) this);
      		}
      		for (SecurityConfigurer<O, B> configurer : configurersAddedInInitializing) {
      			configurer.init((B) this);
      		}
      	}
      ```

      1. WebSecurityConfigurerAdapter.init()

         > 这个方法由SecurityConfigurer接口提供，并在WebSecurityConfigurerAdapter抽象类中实现；

      ```java
      public void init(final WebSecurity web) throws Exception {
          	// 这个方法下面详细解读
      		final HttpSecurity http = getHttp();
          	// 将httpSecurity实例放入webSecurity的securityFilterChainBuilders属性中
          	// 这个属性后面会用来生成拦截器链
      		web.addSecurityFilterChainBuilder(http).postBuildAction(new Runnable() {
      			public void run() {
                      //在build都完成后从http中拿出FilterSecurityInterceptor对象并赋值给					   //WebSecurity		
      				FilterSecurityInterceptor securityInterceptor = http
      						.getSharedObject(FilterSecurityInterceptor.class);
      				web.securityInterceptor(securityInterceptor);
      			}
      		});
      	}
      ```

      2. WebSecurityConfigurerAdapter.getHttp()

      ``` java
      protected final HttpSecurity getHttp() throws Exception {
      		if (http != null) {
      			return http;
      		}
          	// 异常相关
      		DefaultAuthenticationEventPublisher eventPublisher = objectPostProcessor
      				.postProcess(new DefaultAuthenticationEventPublisher());
      localConfigureAuthenticationBldr.authenticationEventPublisher(eventPublisher);
          	// 如果覆盖了configure(AuthenticationManagerBuilder auth)，这里被调用
      		AuthenticationManager authenticationManager = authenticationManager();
      		authenticationBuilder.parentAuthenticationManager(authenticationManager);
      		authenticationBuilder.authenticationEventPublisher(eventPublisher);
          	//创建共享对象
      		Map<Class<? extends Object>, Object> sharedObjects = createSharedObjects();
      		http = new HttpSecurity(objectPostProcessor, authenticationBuilder,
      				sharedObjects);
          	//默认情况下开启默认配置
      		if (!disableDefaults) {
      			http
                      // 启用csrf
      				.csrf().and()
                // 将Security上下文与Spring Web中用于处理异步请求映射的 WebAsyncManager 进行集成。
      				.addFilter(new WebAsyncManagerIntegrationFilter())
                      //添加默认的各种异常状态处理，如无权访问资源异常等
      				.exceptionHandling().and()
                      // 在响应中添加一些安全标头
      				.headers().and()
                      // 单点登陆
      				.sessionManagement().and()
                      // 设置security上下文管理
      				.securityContext().and()
                      // 未登录前访问受限资源被引导到登陆页，登陆完之后自动回到之前想要访问的页面
      				.requestCache().and()
                      // 启用匿名用户角色
      				.anonymous().and()
                      // 为servlet集成更丰富的API
      				.servletApi().and()
                      //生成一个默认登陆页
      				.apply(new DefaultLoginPageConfigurer<>()).and()
      				.logout();
                  // 加载一些配置信息，默认没有
      			ClassLoader classLoader = this.context.getClassLoader();
      			List<AbstractHttpConfigurer> defaultHttpConfigurers =
      			SpringFactoriesLoader.loadFactories(AbstractHttpConfigurer.class, 				classLoader);
      			for (AbstractHttpConfigurer configurer : defaultHttpConfigurers) {
      				http.apply(configurer);
      			}
      		}
          	// 通常情况下我们会覆盖这个方法，这里被调用
      		configure(http);
      		return http;
      	}
      ```

   2. **AbstractConfiguredSecurityBuilder.configure()**

      > 向HttpSecutiry中添加下面这些过滤器，

      ```java
      private void configure() throws Exception {
      		Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();
      		for (SecurityConfigurer<O, B> configurer : configurers) {
                  // 主要是将下面这过滤策略放到http的filters属性中，后面会用到
      			configurer.configure((B) this);
      		}
      	}
      ```

      ![](https://i.imgur.com/gG5cCgs.png)

   3. **AbstractConfiguredSecurityBuilder.performBuild()**

      > 实际上调用的是WebSecutity.performBuild()、HttpSecutity.performBuild()方法;
      >
      > 最终会生成一个拦截器链

      1. WebSecurity.performBuild()方法

   ```java
   	@Override
   	protected Filter performBuild() throws Exception {
   		int chainSize = ignoredRequests.size() + securityFilterChainBuilders.size();
           //拦截器链
   		List<SecurityFilterChain> securityFilterChains = new ArrayList<>(
   				chainSize);
   		for (RequestMatcher ignoredRequest : ignoredRequests) {
   			securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
   		}
           // 遍历WebSecurity.securityFilterChainBuilders中的那些HttpSecutiry
   		for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : securityFilterChainBuilders) {
               // 调用HttpSecutiry的build()方法，这个方法又回到了上面的
               // AbstractConfiguredSecurityBuilder.doBuild()
               // 但是不同的是最终在调用performBuild()的时候调用的是HttpSecutity.performBuild()
               // HttpSecutity.performBuild()返回一个DefaultSecurityFilterChain
   			securityFilterChains.add(securityFilterChainBuilder.build());
   		}
           // 最终将那些HttpSecurity返回的DefaultSecurityFilterChain组装成一个过滤链代理
   		FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
   		if (httpFirewall != null) {
   			filterChainProxy.setFirewall(httpFirewall);
   		}
   		filterChainProxy.afterPropertiesSet();
   		Filter result = filterChainProxy;
           //执行的就是之前在WebSecurityConfigurerAdapter.init()中的那个Runner
   		postBuildAction.run();
           //返回个过滤链代理
   		return result;
   	}
   ```

   2.  Http.performBuild()

      ```java
      @Override
      	protected DefaultSecurityFilterChain performBuild() throws Exception {
      		// 将上面添加到Http的filters属性中的 filter排序
      		Collections.sort(filters, comparator);
      		// 生成一个DefaultSecurityFilterChain返回
               // 这个类的作用很简单，一个匹配规则(requestMatcher)和一个在满足这个匹配规则后
               // 需要执行的那些过滤器(filters)
      		return new DefaultSecurityFilterChain(requestMatcher, filters);
      	}
      ```

------

## FilterChainProxy

>上面已经看到，做了那么多工作之后只产生了一个FilterChainProxy，这个东西里面包含了一个个HttpSecurity定义的规则和规则命中后执行的过滤器列表，下面看一下FilterChainProxy具体的执行步骤

```java
@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;
		if (clearContext) {
			try {
				request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                  // 开始实际执行
				doFilterInternal(request, response, chain);
			}
			finally {
				SecurityContextHolder.clearContext();
				request.removeAttribute(FILTER_APPLIED);
			}
		}
		else {
			doFilterInternal(request, response, chain);
		}
	}
```

```java
private void doFilterInternal(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 包装请求、响应
		FirewalledRequest fwRequest = firewall
				.getFirewalledRequest((HttpServletRequest) request);
		HttpServletResponse fwResponse = firewall
				.getFirewalledResponse((HttpServletResponse) response);
		// 根据之前在HttpSecurity中写的那些规则，获取到对应的Filters
		List<Filter> filters = getFilters(fwRequest);
		// 如果所有规则都没有命中，则Spring Security不需要过滤拦截
		if (filters == null || filters.size() == 0) {
             // 重置过滤状态
			fwRequest.reset();
             // 执行其他的Filter（即不是Spring Security的那些）
			chain.doFilter(fwRequest, fwResponse);
			return;
		}
		// 规则命中，使用虚拟过滤器链来包装那些在HttpSecurity中的filters
		VirtualFilterChain vfc = new VirtualFilterChain(fwRequest, chain, filters);
    	// 开始执行这些过滤器链
		vfc.doFilter(fwRequest, fwResponse);
	}
```



 **private static class VirtualFilterChain implements FilterChain**中的doFilter方法

```java
		// 原始过滤器链，包含了FilterChainProxy以及其他的那些节点
		private final FilterChain originalChain;
		// HttpSecurity中的Fitlers
		private final List<Filter> additionalFilters;
		// 包装的请求
		private final FirewalledRequest firewalledRequest;
		// filters大小
		private final int size;
		// 当前执行到additionalFilters中filter的位置
		private int currentPosition = 0;
		
		//...省略构造方法

		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
            // 这些filters都执行完毕了，并且全都通过
			if (currentPosition == size) {
				// 退出当前执行器链
				this.firewalledRequest.reset();
                 // 执行FilterChainProxy后面的过滤器，也就相当于spring Security
                 // 的校验全都通过了
				originalChain.doFilter(request, response);
			}
			else {
                 // 否则继续遍历虚拟的filters，游标+1
				currentPosition++;
				// 获取当前需要执行的filter
				Filter nextFilter = additionalFilters.get(currentPosition - 1);
				// 开始执行这个filters，并将当前这个虚拟过滤器链传入，下面使用
                 // LogoutFilter来演示一下
				nextFilter.doFilter(request, response, this);
			}
		}
	}
```



### LogoutFilter

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
    	// 判断是不是登出请求
		if (requiresLogout(request, response)) {
             //是登出请求，拿出上下文中的认证信息
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             // handler是一个组合了多个登出请求处理的类，他会执行所有被它组合的登出请求处理器
			this.handler.logout(request, response, auth);
             // 登出成功的处理方法
			logoutSuccessHandler.onLogoutSuccess(request, response, auth);
             // HttpSecurity中的filters调用链在这里被终止
			return;
		}
    	// 不是登出请求，则继续调用VirtualFilterChain.doFilter重复上述步骤
    	// 直到过滤器链执行完毕，或者其中某个过滤器 return;
		chain.doFilter(request, response);
	}
```







## 总结

> **AbstractConfiguredSecurityBuilder**抽象类有两个最重要的子类 WebSecurity  HttpSecurity
>
> WebSecurity依赖HttpSecurity，并且会调用不管是系统添加的WebSecurityConfigurer实例还是自己自定义的配置类他们的 init() 和 configure()方法。比较坑爹的是两个体系都有这俩方法，比较容易混

1. **webSercurity.build()**

   > ​     他会调用父类AbstractConfiguredSecurityBuilder.doBuild()方法，完成对WebSecurityConfigurer的初始化，以及将所有HttpSecurity封装成一个**FilterChainProxy**

   1. **AbstractConfiguredSecurityBuilder.doBuild()**

   > 该方法调用自身的两个已经实现的方法一个是init() 一个是configure()，而另一个performBuild()没有提供实现，交由子类(WebSecurity HttpSecurity)实现。

   1. **init()** 

      > ​      它会调用**configurers**属性中保存的那些WebSecurityConfigurer实例的**init()**方法，我们自定义的spring Security的配置类也都是这个接口的实现，这里使用**WebSecurityConfigurerAdapter**的init()方法做演示。这两个init不要混了

      1.  **WebSecurityConfigurerAdapter.init(WebSecurity)**

         > ​      初始化HttpSecurity，并将这个HttpSecurity放入WebSecurity的**securityFilterChainBuilders**属性中

   2.  **configure()**

      > ​     取出自身的**configurers**中保存的WebSecurityConfigurer列表，并调用它的们的configure(WebSecurity)方法，这个方法大部分都是空的。

   3.  **performBuild()**

      > ​      这一步将WebSecurity的**securityFilterChainBuilders**属性中保存的那些HttpSecurity取出来，并调用他们的build()方法，这就有意思了，还记得WebSecurity和HttpSecurity这俩都是AbstractConfiguredSecurityBuilder子类吗，一调用这个方法又回到上面那些步骤了。下面



------

 2. **HttpSecurity.buid()**

    >​      但是这次不同的是父类中实现的init() configure()方法他们操作的对象不同了，而且最后的HttpSecurity.performBuild()方法将会生成一个DefaultSecurityFilterChain,返回给WebSecurity，他将这些DefaultSecurityFilterChain组装成**FilterChainProxy**，最后生成一个叫做springSecurityFilterChain的Filter

    1. **init()**

       >​	上面当子类是WebSecurity时调用init()和configure()方法是对那些配置类起作用。而现在则是对
       >
       > **SecurityConfigurerAdapter**的实例们起作用.
       >
       >​	这些**SecurityConfigurerAdapter**是这样产生的例如: httpSecurity.formLogin()时，产生一个FormLoginConfigurer,它就是一个SecurityConfigurerAdapter实例。然后将它放到**configurers**属性中。
       >
       >​	上面知道了HttpSecurit的**configurers**属性中保存的是SecurityConfigurerAdapter。而这里则是调用他们的init()方法，现在用FormLoginConfigurer的init方法演示。

       1. **FormLoginConfigurer.init(HttpSecurity)**

          > ​	初始化一些默认值，例如登陆页面地址，登陆失败页面，登陆的时候用户名参数叫什么  密码参数叫什么，表单提交到哪。最终要的是它会产生一个UsernamePasswordAuthenticationFilter，这个后面会说。

    2. **configure()**

       > ​	跟上一步类似，不过这次是调用**configurers**参数中那些**SecurityConfigurerAdapter**实例的**configure(HttpSecurity)**方法了。这里还是拿**FormLoginConfigurer**举例

       1. **FormLoginConfigurer.configure(HttpSecurity)**

          >初始化它内部的Filter的一些参数，这个类用的是**UsernamePasswordAuthenticationFilter**
          >
          >然后把这个Filter放到HttpSecurity的**filters**属性中

    3. **performBuild()**

       > 这里做的就很简单了，把刚才放进filters属性中的过滤器排个序，然后
       >
       > **return new DefaultSecurityFilterChain(requestMatcher, filters);**