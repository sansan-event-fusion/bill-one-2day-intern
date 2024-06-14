import { fetchJSONOrNull } from "src/api/util";
import { Issuer } from "src/domain/models/issuer";
import { Recipient } from "src/domain/models/recipient";

export const issuingApi = {
  getIssuer: async (): Promise<Issuer[]> => {
    const response = await fetchJSONOrNull<{ issuers: Issuer[] }>(
      "application/json",
      "http://localhost:8082/api/issuing/issuer",
      {
        method: "GET",
      },
    );
    return response?.issuers ?? [];
  },
  getIssuerWithoutLogin: async (tenantNameId: string): Promise<Issuer[]> => {
    const response = await fetchJSONOrNull<{ issuers: Issuer[] }>(
      "application/json",
      `http://localhost:8082/api/issuing/issuer/${tenantNameId}`,
      {
        method: "GET",
      },
    );
    return response?.issuers ?? [];
  },
  getRecipient: async (tenantNameId: string): Promise<Recipient[]> => {
    const response = await fetchJSONOrNull<{ row: Recipient[] }>(
      "application/json",
      `http://localhost:8082/api/issuing/recipients/${tenantNameId}`,
      {
        method: "GET",
      },
    );
    return response?.row ?? [];
  },
};
