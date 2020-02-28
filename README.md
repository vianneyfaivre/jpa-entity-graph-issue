# JPA Issue with @Lock + @EntityGraph + @ElementCollection

When trying to execute a JPQL Query that uses `@Lock` on a table created by an `@ElementCollection` annotation, the method `QueryLoader#applyLocks` throws a `NullPointerException`.

## How to reproduce

Tested with Spring Boot 2.2.4.RELEASE and Hibernate 5.4.10

1. run `mvnw.cmd spring-boot:run`
2. send the http request `GET http://localhost:8080/customers/1`
3. Check the stacktrace in the logs


## Possible solution

In QueryLoader.jave, the call `#getQueryable` to return `null`, see https://github.com/hibernate/hibernate-orm/blob/5.4.10/hibernate-core/src/main/java/org/hibernate/loader/hql/QueryLoader.java#L368

In that case, because we are using `@ElementCollection`, I think the method `#getQueryableCollection` should be called instead of `#getQueryable`.

## Stacktrace

```
java.lang.NullPointerException: null
	at org.hibernate.loader.hql.QueryLoader.applyLocks(QueryLoader.java:369) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.preprocessSQL(Loader.java:257) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.executeQueryStatement(Loader.java:2032) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.executeQueryStatement(Loader.java:2012) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.doQuery(Loader.java:953) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.doQueryAndInitializeNonLazyCollections(Loader.java:354) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.doList(Loader.java:2815) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.doList(Loader.java:2797) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.listIgnoreQueryCache(Loader.java:2629) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.Loader.list(Loader.java:2624) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.loader.hql.QueryLoader.list(QueryLoader.java:506) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.hql.internal.ast.QueryTranslatorImpl.list(QueryTranslatorImpl.java:396) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.engine.query.spi.HQLQueryPlan.performList(HQLQueryPlan.java:219) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.internal.SessionImpl.list(SessionImpl.java:1396) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.query.internal.AbstractProducedQuery.doList(AbstractProducedQuery.java:1558) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.query.internal.AbstractProducedQuery.list(AbstractProducedQuery.java:1526) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.hibernate.query.internal.AbstractProducedQuery.getSingleResult(AbstractProducedQuery.java:1574) ~[hibernate-core-5.4.10.Final.jar:5.4.10.Final]
	at org.springframework.data.jpa.repository.query.JpaQueryExecution$SingleEntityExecution.doExecute(JpaQueryExecution.java:196) ~[spring-data-jpa-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.data.jpa.repository.query.JpaQueryExecution.execute(JpaQueryExecution.java:88) ~[spring-data-jpa-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.data.jpa.repository.query.AbstractJpaQuery.doExecute(AbstractJpaQuery.java:154) ~[spring-data-jpa-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.data.jpa.repository.query.AbstractJpaQuery.execute(AbstractJpaQuery.java:142) ~[spring-data-jpa-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.data.repository.core.support.RepositoryFactorySupport$QueryExecutorMethodInterceptor.doInvoke(RepositoryFactorySupport.java:618) ~[spring-data-commons-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.data.repository.core.support.RepositoryFactorySupport$QueryExecutorMethodInterceptor.invoke(RepositoryFactorySupport.java:605) ~[spring-data-commons-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:366) ~[spring-tx-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:99) ~[spring-tx-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139) ~[spring-tx-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:149) ~[spring-data-jpa-2.2.4.RELEASE.jar:2.2.4.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:212) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at com.sun.proxy.$Proxy100.findByIdLocked(Unknown Source) ~[na:na]
	at com.example.demo.CustomerController.getCustomerById(CustomerController.java:20) ~[classes/:na]
	at com.example.demo.CustomerController$$FastClassBySpringCGLIB$$fb2a9f34.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:366) ~[spring-tx-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:99) ~[spring-tx-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689) ~[spring-aop-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at com.example.demo.CustomerController$$EnhancerBySpringCGLIB$$70cb39ad.getCustomerById(<generated>) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_102]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_102]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_102]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_102]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:888) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:793) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:634) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.3.RELEASE.jar:5.2.3.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:367) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:860) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1598) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_102]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_102]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_102]

```