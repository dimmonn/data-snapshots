package com.luxosft.shapshot.config;

import com.luxosft.shapshot.mapping.SnapshotApiVersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
@Component
public class CustomMvcRegistrations implements WebMvcRegistrations {
  public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
    return new SnapshotApiVersionRequestMappingHandlerMapping("v");
  }
}
