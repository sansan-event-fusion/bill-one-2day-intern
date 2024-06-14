CREATE TABLE issuer_invoice
(
    tenant_name_id             varchar(20) NOT NULL REFERENCES tenant (tenant_name_id),
    issuer_invoice_uuid        UUID PRIMARY KEY,
    supplier_tenant_name_id    varchar(20) NOT NULL REFERENCES tenant (tenant_name_id),
    issuer_uuid                UUID        NOT NULL REFERENCES issuer (issuer_uuid),
    recipient_uuid             UUID        NOT NULL,
    invoice_amount             INT         NOT NULL,
    payment_due_date           DATE        NOT NULL,
    uploaded_file_storage_path varchar(255)         DEFAULT NULL,
    created_at                 TIMESTAMP   NOT NULL DEFAULT NOW()
);
CREATE INDEX issuer_invoice_issuer_uuid_idx ON issuer_invoice (issuer_uuid);

ALTER TABLE issuer_invoice
    RENAME COLUMN payment_due_date TO "payment_deadline";