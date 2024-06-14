CREATE TABLE issuer(
    tenant_name_id varchar(20) NOT NULL REFERENCES tenant(tenant_name_id),
    issuer_uuid    UUID PRIMARY KEY,
    issuer_name    VARCHAR(50)  NOT NULL,
    issuer_email   VARCHAR(254) NOT NULL,
    created_at     timestamptz  NOT NULL DEFAULT NOW()
);