CREATE TABLE issuer
(
    tenant_name_id varchar(20) REFERENCES tenant (tenant_name_id) NOT NULL,
    issuer_uuid    UUID PRIMARY KEY,
    full_name      VARCHAR(50)                                    NOT NULL,
    email          VARCHAR(254)                                   NOT NULL,
    created_at     timestamptz                                    NOT NULL DEFAULT NOW()
);

ALTER TABLE issuer
    ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_policy ON issuer
    USING (tenant_name_id = current_setting('app.current_tenant')::varchar);

CREATE INDEX issuer_tenant_name_id_idx ON issuer (tenant_name_id);

ALTER TABLE issuer
    ENABLE ROW LEVEL SECURITY;

COMMENT ON TABLE issuer IS 'テナント所属のユーザーであり、発行者なユーザー';
