import { Recipient } from "src/domain/models/recipient";
import { issuingApi } from "src/api/issuing/issuing-api";
import { useQuery } from "react-query";

export const useSupplier = (tenantNameId: string): Recipient[] => {
  const result = useQuery<Recipient[], Error>(["recipients"], () =>
    issuingApi.getRecipient(tenantNameId),
  );

  return result.data ?? [];
};
