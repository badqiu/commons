package com.github.rapid.common.ana;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface PackageLevelAnnotation
{
  // stuff as required.
}