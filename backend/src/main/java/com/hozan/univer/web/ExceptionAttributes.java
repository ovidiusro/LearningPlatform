package com.hozan.univer.web;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * The ExceptionAttributes interface defines the behavioral contract to be
 * implemented by concrete ExceptionAttributes classes.
 *
 * Provides attributes which describe Exceptions and the context in which they
 * occurred.
 *
 * @see DefaultExceptionAttributes
 *
 */
public interface ExceptionAttributes {

    /**
     * Returns a {@link Map} of exception attributes. The Map may be used to
     * display an error page or serialized into a {@link ResponseBody}.
     *
     * @param exception The Exception reported.
     * @param httpRequest The HttpServletRequest in which the Exception
     *        occurred.
     * @param httpStatus The HttpStatus value that will be used in the
     *        {@link HttpServletResponse}.
     * @return A Map of exception attributes.
     */
    Map<String, Object> getExceptionAttributes(Exception exception,
                                               HttpServletRequest httpRequest, HttpStatus httpStatus);

}
