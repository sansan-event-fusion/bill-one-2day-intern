import { useLocalStorage } from "src/util/useLocalStorage";

// 便宜的にutilに配置する。
// 今日通過したかっただけ。
export const useAuth = () => {
  const { get, set } = useLocalStorage();

  const setTenantNameId = (tenantNameId: string) => {
    set("tenantNameId", tenantNameId);
  };

  const getTenantNameId = () => {
    return get("tenantNameId");
  };

  const setUserUUID = (userUUID: string) => {
    set("userUUID", userUUID);
  };

  const getUserUUID = () => {
    return get("userUUID");
  };

  const setCurrentIsRecipient = (value: boolean) => {
    set("isRecipient", value.toString());
  };

  const setCurrentIsIssuer = (value: boolean) => {
    set("isIssuer", value.toString());
  };

  const getCurrentIsRecipient = () => {
    return get("isRecipient") === "true";
  };

  const getCurrentIsIssuer = () => {
    return get("isIssuer") === "true";
  };

  return {
    setTenantNameId,
    setUserUUID,
    setCurrentIsRecipient,
    setCurrentIsIssuer,
    getTenantNameId,
    getUserUUID,
    getCurrentIsRecipient,
    getCurrentIsIssuer,
  };
};
