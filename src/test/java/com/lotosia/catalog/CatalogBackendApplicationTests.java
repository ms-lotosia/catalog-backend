package com.lotosia.catalog;

import org.junit.jupiter.api.Test;

/**
 * @author: nijataghayev
 * Placeholder — real unit tests are in service-level test classes.
 * Full integration tests require a running PostgreSQL instance.
 */
class CatalogBackendApplicationTests {

    @Test
    void main_InstantiatesWithoutError() {
        CatalogBackendApplication app = new CatalogBackendApplication();
        org.assertj.core.api.Assertions.assertThat(app).isNotNull();
    }
}
