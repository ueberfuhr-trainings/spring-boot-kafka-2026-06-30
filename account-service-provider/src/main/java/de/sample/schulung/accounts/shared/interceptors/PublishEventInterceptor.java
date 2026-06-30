package de.sample.schulung.accounts.shared.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class PublishEventInterceptor
  extends AbstractBeanFactoryAwareAdvisingPostProcessor
  implements InitializingBean {

  private final MethodInterceptor publishEventAdvice;

  public PublishEventInterceptor(final ApplicationEventPublisher eventPublisher) {
    this.publishEventAdvice = invocation -> {
      // create event object
      Object event = null;
      final var annotation = AnnotationUtils.findAnnotation(invocation.getMethod(), PublishEvent.class);
      if (null != annotation) {
        event = annotation.value()
          .getConstructor(invocation.getMethod().getParameterTypes())
          .newInstance(invocation.getArguments());
      }
      // invoke method
      final var result = invocation.proceed();
      // if successful, fire event
      if (null != event) {
        eventPublisher.publishEvent(event);
      }
      return result;
    };
  }

  @Override
  public void afterPropertiesSet() {
    Pointcut pointcut = new AnnotationMatchingPointcut(null, PublishEvent.class, true);
    this.advisor = new DefaultPointcutAdvisor(pointcut, publishEventAdvice);
  }


}