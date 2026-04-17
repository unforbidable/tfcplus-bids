package com.unforbidable.tfc.bids.api.Interfaces;

public interface ISimpleRecipeMatcher<T> {

    boolean matches(T ingredient);

}
