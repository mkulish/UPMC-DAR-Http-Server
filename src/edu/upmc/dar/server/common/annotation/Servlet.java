package edu.upmc.dar.server.common.annotation;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Servlet {
    String url();
    RequestMethod method() default RequestMethod.ANY;
    ContentType produces() default ContentType.TEXT_PLAIN;
}
