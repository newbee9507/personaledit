package com.seb_main_034.SERVER.forTestUtils;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface ForMockTestCustomUser {

    long userId() default 1L;

    String email() default "admin@gmail.com";


}
