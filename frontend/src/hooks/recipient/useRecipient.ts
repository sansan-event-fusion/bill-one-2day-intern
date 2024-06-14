import { useQuery } from "react-query";
import { recipientAPI } from "src/api/recipient/recipient-api";
import { Recipient } from "src/domain/models/recipient";

export const useRecipients = (): Recipient[] => {
  const result = useQuery<Recipient[], Error>(["recipients"], () =>
    recipientAPI.getRecipients(),
  );

  return result.data ?? [];
};
