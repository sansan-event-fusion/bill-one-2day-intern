CREATE TABLE invoice
(
    tenant_name_id   varchar(20) not null references tenant(tenant_name_id) ON DELETE CASCADE,
    invoice_uuid     uuid PRIMARY KEY,
    recipient_uuid   uuid  REFERENCES recipient(recipient_uuid) not null,
    invoice_amount   integer,
    supplier_uuid    uuid,
    supplier_name    varchar(20),
    payment_deadline date,
    registered_by    varchar(20) not null,
    registered_at    timestamptz not null DEFAULT now()
);

CREATE POLICY select_invoice_policy ON invoice FOR SELECT USING (tenant_name_id = current_setting('app.current_tenant')::varchar);

CREATE INDEX invoice_tenant_name_id_idx ON invoice(tenant_name_id);
CREATE INDEX invoice_recipient_uuid_idx ON invoice(recipient_uuid);

ALTER TABLE invoice ENABLE ROW LEVEL SECURITY;

COMMENT ON TABLE invoice IS '請求書';
