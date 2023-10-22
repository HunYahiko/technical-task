package com.gringotts.technicaltask;

import org.testcontainers.containers.PostgreSQLContainer;

public class TechnicalPostgreSQLContainer extends PostgreSQLContainer<TechnicalPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:15.3";
    private static TechnicalPostgreSQLContainer container;

    private TechnicalPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static TechnicalPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new TechnicalPostgreSQLContainer()
                    .withDatabaseName("integration-tests-db")
                    .withUsername("admin")
                    .withPassword("admin");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {

    }
}
