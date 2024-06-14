import { useQuery } from "react-query";
import { issuingApi } from "src/api/issuing/issuing-api";
import { Issuer } from "src/domain/models/issuer";

export const useIssuer = (): Issuer[] => {
  const result = useQuery<Issuer[], Error>(["issuer"], () =>
    issuingApi.getIssuer(),
  );
  return result.data ?? [];
};
