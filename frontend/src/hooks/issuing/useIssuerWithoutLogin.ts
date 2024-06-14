import { Issuer } from "src/domain/models/issuer";
import { useQuery } from "react-query";
import { issuingApi } from "src/api/issuing/issuing-api";

// 基本的にアカウント切り替えで飲み使用する。
// 便宜hooksだけどapi呼び出しなのでここにロケーション
export const useIssuerWithoutLogin = (tenantNameId: string): Issuer[] => {
  const result = useQuery<Issuer[], Error>(["issuer"], () =>
    issuingApi.getIssuerWithoutLogin(tenantNameId),
  );
  return result.data ?? [];
};
