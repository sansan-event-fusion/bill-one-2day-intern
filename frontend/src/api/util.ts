import { useAuth } from "src/util/useAuth";

export const fetchJSONOrNull = async <T>(
  contentType: ContentType,
  path: string,
  options: RequestInit,
  allowedStatusCodes?: number[],
): Promise<T | null> => {
  allowedStatusCodes = allowedStatusCodes || [404];
  const res = await fetchURL(contentType, path, options, allowedStatusCodes);

  if (res.status === 404 || allowedStatusCodes.includes(res.status)) {
    return null;
  }

  return (await res.json()) as T;
};

export const fetchURL = async (
  contentType: ContentType,
  path: string,
  options: RequestInit,
  allowedStatusCodes?: number[],
): Promise<Response> => {
  options.headers = requiredHeader(contentType, options.headers);
  allowedStatusCodes = allowedStatusCodes || [];

  const res: Response = await fetch(path, {
    credentials: "same-origin",
    ...options,
  });

  if (res.ok || allowedStatusCodes.includes(res.status)) {
    return res;
  }

  throw new Error(`Failed to fetch: ${options.method} ${path}`);
};

// ここの引数前二つは,call.principal()に対応している。
// 実際はbff等で認証ロジックが挟まるが今回は便宜上ここでheaderをセットする。
const requiredHeader = (contentType: ContentType, options?: HeadersInit) => {
  const { getTenantNameId, getUserUUID } = useAuth();
  if (contentType === null) {
    return {
      "X-App-Request": "1",
      "X-App-Tenant-Name-Id": getTenantNameId() ?? "",
      "X-App-User-UUID": getUserUUID() ?? "",
      ...options,
    };
  }
  return {
    "X-App-Request": "1",
    "Content-Type": contentType,
    "X-App-Tenant-Name-Id": getTenantNameId() ?? "",
    "X-App-User-UUID": getUserUUID() ?? "",
    ...options,
  };
};

export type ContentType = "application/json" | "application/pdf" | null;
