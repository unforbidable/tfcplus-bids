package com.unforbidable.tfc.bids.features.device.kiln.main;

public class KilnValidationException extends Exception {

    public KilnValidationException() {
    }

    public KilnValidationException(String message) {
        super(message);
    }

    public KilnValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KilnValidationException(Throwable cause) {
        super(cause);
    }

}
