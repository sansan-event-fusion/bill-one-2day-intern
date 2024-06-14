import { useQuery } from "react-query";
import { Tenant } from "src/domain/models/tenant";
import { tenantAPI } from "src/api/recipient/tenant-api";

export const useSuppliers = (): Tenant[] => {
  const result = useQuery<Tenant[], Error>(["tenant"], () =>
    tenantAPI.getSuppliers(),
  );

  return result.data ?? [];
};
