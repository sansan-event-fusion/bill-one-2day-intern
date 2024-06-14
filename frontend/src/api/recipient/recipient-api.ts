import {Recipient} from "src/domain/models/recipient";
import {fetchJSONOrNull} from "../util";

export const recipientAPI = {
    getRecipients: async (): Promise<Recipient[]> => {
        const response = await fetchJSONOrNull<{ recipients: Recipient[] }>(
            "application/json",
            "http://localhost:8081/api/recipient/recipients",
            {
                method: "GET",
            },
        );
        return response?.recipients ?? [];
    },

    getRecipientsWithoutLogin: async (tenantNameId: string): Promise<Recipient[]> => {
        const response = await fetchJSONOrNull<{ recipients: Recipient[] }>(
            "application/json",
            `http://localhost:8081/api/recipient/recipients/${tenantNameId}`,
            {
                method: "GET",
            },
        );
        return response?.recipients ?? [];
    }
};
