/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 10-Feb-2017
 */
package com.capillary.expectation.exception;

import lombok.Data;

/**
 * @author aditya
 *
 */
public @Data class ExpectationException extends RuntimeException {

    private static final long serialVersionUID = -2599776871100205126L;

    private final int errorCode;
    private final String message;
    private final Throwable t;
}
