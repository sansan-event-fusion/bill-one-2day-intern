CREATE TABLE tenant
(
    tenant_name_id VARCHAR(20) PRIMARY KEY,
    tenant_uuid    uuid        NOT NULL,
    locale         VARCHAR(5)  NOT NULL,
    created_at     timestamptz NOT NULL
);

COMMENT ON TABLE tenant IS 'テナント(会社)';
