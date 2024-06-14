import { Tenant } from "src/domain/models/tenant";
import { fetchJSONOrNull } from "src/api/util";

export const tenantAPI = {
  getSuppliers: async (): Promise<Tenant[]> => {
    const response = await fetchJSONOrNull<{ value: Tenant[] }>(
      "application/json",
      "http://localhost:8081/api/recipient/supplier",
      {
        method: "GET",
      },
    );

    return response?.value ?? [];
  },
};
