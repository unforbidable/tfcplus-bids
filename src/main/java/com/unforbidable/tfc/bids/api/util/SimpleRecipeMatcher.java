package com.unforbidable.tfc.bids.api.util;

public interface SimpleRecipeMatcher<T> {

    boolean matches(T ingredient);

}
