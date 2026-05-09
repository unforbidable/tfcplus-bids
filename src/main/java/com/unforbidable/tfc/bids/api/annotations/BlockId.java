package com.unforbidable.tfc.bids.api.annotations;

import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@TypeQualifier(applicableTo = String.class)
public @interface BlockId {
    int value();
}
