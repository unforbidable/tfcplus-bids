package com.unforbidable.tfc.bids.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.meta.TypeQualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@TypeQualifier(applicableTo = String.class)
public @interface BlockId {
    int value();
}
