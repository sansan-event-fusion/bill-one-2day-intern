import { Recipient } from "src/domain/models/recipient";
import { useQuery } from "react-query";
import { recipientAPI } from "src/api/recipient/recipient-api";

// 基本的にアカウント切り替えで飲み使用する。
// 便宜hooksだけどapi呼び出しなのでここにロケーション
export const useRecipientsWithoutLogin = (
  tenantNameId: string,
): Recipient[] => {
  const result = useQuery<Recipient[], Error>(["recipients"], () =>
    recipientAPI.getRecipientsWithoutLogin(tenantNameId),
  );
  return result.data ?? [];
};
