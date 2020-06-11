package com.luxosft.shapshot.mapping;

import com.luxosft.shapshot.annotations.SnapshotApiVersion;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@AllArgsConstructor
public class SnapshotApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

  private String apiPrefix;

  @Override
  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
    if (info == null) {
      return null;
    }
    Annotation[] classAnnotations = handlerType.getDeclaredAnnotations();
    Annotation[] methodAnnotations = method.getDeclaredAnnotations();
    for (Annotation methodApi : methodAnnotations) {
      if (methodApi.annotationType().equals(SnapshotApiVersion.class)) {
        RequestCondition<?> methodCondition = getCustomMethodCondition(method);
        info = createApiVersionInfo((SnapshotApiVersion) methodApi, methodCondition).combine(info);
      }

    }
    for (Annotation classApi : classAnnotations) {
      if (classApi.annotationType().equals(SnapshotApiVersion.class)) {
        RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
        info = createApiVersionInfo((SnapshotApiVersion) classApi, typeCondition).combine(info);
      }
    }
    return info;
  }

  private RequestMappingInfo createApiVersionInfo(SnapshotApiVersion annotation,
      RequestCondition<?> customCondition) {
    int[] values = annotation.version();
    String[] patterns = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      patterns[i] = apiPrefix + values[i];
    }
    return new RequestMappingInfo(
        new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
            useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
        new RequestMethodsRequestCondition(),
        new ParamsRequestCondition(),
        new HeadersRequestCondition(),
        new ConsumesRequestCondition(),
        new ProducesRequestCondition(),
        customCondition);
  }
}
