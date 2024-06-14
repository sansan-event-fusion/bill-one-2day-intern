import { CommonHeader } from "src/components/templates/CommonHeader";
import { Button, ComboboxItem, Select } from "@mantine/core";
import { useCallback, useState } from "react";
import { useAuth } from "src/util/useAuth";
import { useNavigate } from "react-router-dom";
import { useRecipientsWithoutLogin } from "src/hooks/recipient/useRecipientsWithoutLogin";
import { useIssuerWithoutLogin } from "src/hooks/issuing/useIssuerWithoutLogin";

export const AccountChangePage: React.FC = () => {
  const [tenant, setTenant] = useState<string | null>(null);

  return (
    <>
      <CommonHeader />
      <div>
        <h1>アカウント選択</h1>
        <Select
          label="テナントを選択"
          data={["yonyon", "gogo", "rokuroku"]}
          onChange={setTenant}
          value={tenant}
        />
        {tenant !== null ? <AccountSelectArea tenant={tenant} /> : null}
      </div>
    </>
  );
};

// 便宜的なファイルなので、ファイル分け等してない。
const AccountSelectArea: React.FC<{ tenant: string }> = ({ tenant }) => {
  const [selectedAccount, setSelectedAccount] = useState<AuthState | null>(
    null,
  );

  const [isNewAccountIsSelected, setIsNewAccountIsSelected] =
    useState<boolean>(false);

  const recipients = useRecipientsWithoutLogin(tenant);
  const issuer = useIssuerWithoutLogin(tenant);

  const {
    setTenantNameId,
    setUserUUID,
    setCurrentIsRecipient,
    setCurrentIsIssuer,
    getCurrentIsRecipient,
    getCurrentIsIssuer,
  } = useAuth();

  const recipientItems = recipients.map((recipient) => {
    return {
      value: recipient.recipientUUID,
      label: recipient.fullName,
    };
  });
  const issuerItems = issuer.map((issuer) => {
    return {
      value: issuer.issuerUUID,
      label: issuer.fullName,
    };
  });

  const generateOptions = useOptions(recipientItems, issuerItems, tenant);
  const { options, authState } = generateOptions();

  const handleChangeAccount = (value: ComboboxItem) => {
    const account = authState.find((state) => state.userUUID === value.value);
    setSelectedAccount(account ?? null);
  };

  const handleUseAccount = () => {
    if (selectedAccount === null) {
      return;
    }
    setTenantNameId(selectedAccount.tenantNameId);
    setUserUUID(selectedAccount.userUUID);
    setCurrentIsRecipient(selectedAccount.isRecipient);
    setCurrentIsIssuer(selectedAccount.isIssuer);
    setIsNewAccountIsSelected(true);
  };
  const navigate = useNavigate();

  return (
    <div>
      {!isNewAccountIsSelected && (
        <>
          <Select
            label="アカウントを選択"
            data={options}
            placeholder="アカウントを選択"
            onChange={(_, v) => handleChangeAccount(v)}
          />
          <br />
          <Button
            disabled={selectedAccount === null}
            onClick={handleUseAccount}
          >
            このアカウントを使う
          </Button>
        </>
      )}
      {getCurrentIsRecipient() && isNewAccountIsSelected && (
        <Button onClick={() => navigate("/recipient/invoices")}>
          受領画面へ
        </Button>
      )}
      {getCurrentIsIssuer() && isNewAccountIsSelected && (
        <Button onClick={() => navigate("/issuing/invoices")}>
          発行画面へ
        </Button>
      )}
    </div>
  );
};

const useOptions = (
  recipient: ComboboxItem[],
  issuer: ComboboxItem[],
  tenant: string,
) => {
  const generateOptions = useCallback((): {
    options: ComboboxItem[];
    authState: AuthState[];
  } => {
    const optionsMap = new Map<string, ComboboxItem>();

    [...recipient, ...issuer].forEach((option) => {
      optionsMap.set(option.value, option);
    });
    const options = Array.from(optionsMap.values());
    const state = options.map((option) => {
      return {
        userUUID: option.value,
        tenantNameId: tenant,
        isRecipient:
          recipient.find((r) => r.value === option.value) !== undefined,
        isIssuer: issuer.find((r) => r.value === option.value) !== undefined,
      };
    });
    return {
      options: options,
      authState: state,
    };
  }, [recipient, issuer, tenant]);

  return generateOptions;
};
type AuthState = {
  userUUID: string;
  tenantNameId: string;
  isRecipient: boolean;
  isIssuer: boolean;
};
