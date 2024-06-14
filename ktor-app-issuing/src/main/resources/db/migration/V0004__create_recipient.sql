-- recipient　serviceと同じ
CREATE TABLE recipient (
   tenant_name_id varchar(20) REFERENCES tenant(tenant_name_id) NOT NULL,
   recipient_uuid UUID PRIMARY KEY,
   full_name VARCHAR(50) NOT NULL,
   email VARCHAR(254) NOT NULL,
   created_at timestamptz NOT NULL DEFAULT NOW()
);

ALTER TABLE recipient ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_policy ON recipient
    USING (tenant_name_id = current_setting('app.current_tenant')::varchar);

CREATE INDEX recipient_tenant_name_id_idx ON recipient (tenant_name_id);

ALTER TABLE recipient ENABLE ROW LEVEL SECURITY;

COMMENT ON TABLE recipient IS 'テナント所属のユーザーであり、受領者なユーザー';
