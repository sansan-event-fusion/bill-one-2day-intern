set schema 'recipient';

BEGIN;

INSERT INTO tenant (tenant_name_id, tenant_uuid, locale, created_at)
VALUES ('yonyon', 'ab36c77f-a654-4a2e-8d4d-a99c24394484', 'JA', '2021-01-01 00:00:00+09:00'),
       ('gogo', '4de049f7-ff8c-4061-9041-7f75f4fd8864', 'JA', '2021-01-02 00:00:00+09:00'),
       ('rokuroku', '9a4bd7bc-b0b2-4a2c-82b1-de3c98ece3fa', 'EN', '2021-01-03 00:00:00+09:00');

INSERT INTO recipient (tenant_name_id, recipient_uuid, full_name, email)
VALUES ('yonyon', '015bdd8a-9aee-4545-8e4b-95b48a482559', 'yonyon太郎', 'taro@yonyon.com'),
       ('yonyon', '72666a07-04bc-4791-a996-f19eab4772a2', 'yonyon次郎', 'jiro@yonyon.com'),
       ('gogo', 'dc00f277-838a-4c6b-a21e-2c5e01054ee9', 'gogoどっちも', 'hogehoge@gogo.com'),
       ('rokuroku', '6cc70cff-1900-407a-9faa-a8be6575727d', 'rokuroku花子', 'hanako@rokurokul.com');


INSERT INTO issuer (tenant_name_id, issuer_uuid, full_name, email)
VALUES ('gogo', '2731b723-2aea-446e-b718-6b9626e103bc', 'gogo発行のみ', 'issuer@gogo.com'),
       ('gogo', 'dc00f277-838a-4c6b-a21e-2c5e01054ee9', 'gogoどっちも', 'hogehoge@gogo.com'),
       ('yonyon', '015bdd8a-9aee-4545-8e4b-95b48a482559', 'yonyon太郎', 'taro@yonyon.com'),
       ('yonyon', '72666a07-04bc-4791-a996-f19eab4772a2', 'yonyon次郎', 'jiro@yonyon.com'),
       ('rokuroku', 'a587cd2b-f77c-407d-bb45-f7db7c395758', 'rokuroku小太郎', 'kotaro@rokuroku.com');

INSERT INTO invoice (tenant_name_id, invoice_uuid, recipient_uuid, registered_by, registered_at)
VALUES ('yonyon', 'd6cefb8c-ecb0-438e-b025-28d092049259', '015bdd8a-9aee-4545-8e4b-95b48a482559', 'RECEIVE',
        '2021-01-01 00:00:00+09:00');

COMMIT;
