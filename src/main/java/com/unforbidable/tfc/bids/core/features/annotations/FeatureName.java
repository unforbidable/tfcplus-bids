package com.unforbidable.tfc.bids.core.features.annotations;

import com.unforbidable.tfc.bids.core.features.Feature;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.meta.TypeQualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TypeQualifier(applicableTo = Feature.class)
public @interface FeatureName {
    String value();
}
