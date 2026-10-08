package com.lotosia.catalog.exception;

import org.springframework.http.HttpStatus;

/**
 * @author: nijataghayev
 */

public class BannerNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BannerNotFoundException(Long id) {
        super("Banner not found with id: " + id, "BANNER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}