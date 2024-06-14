import { Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "src/util/useAuth";
import { FC, Suspense, useEffect } from "react";

export const RecipientRouteSuspense: FC = () => {
  const navigate = useNavigate();
  const { getTenantNameId, getUserUUID, getCurrentIsRecipient } = useAuth();
  useEffect(() => {
    if (getTenantNameId() === null || getUserUUID() === null) {
      navigate("/change-account");
    }
    if (!getCurrentIsRecipient()) {
      navigate("/issuing");
    }
    if (window.location.pathname === "/recipient") {
      navigate("/recipient/invoices");
    }
  }, []);
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Outlet />
    </Suspense>
  );
};
