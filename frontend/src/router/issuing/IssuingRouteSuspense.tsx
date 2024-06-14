import { FC, Suspense, useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "src/util/useAuth";

export const IssuingRouteSuspense: FC = () => {
  const navigate = useNavigate();
  const { getTenantNameId, getUserUUID, getCurrentIsIssuer } = useAuth();
  useEffect(() => {
    if (getTenantNameId() === null || getUserUUID() === null) {
      navigate("/change-account");
    }
    if (!getCurrentIsIssuer()) {
      navigate("/recipient");
    }
    if (window.location.pathname === "/issuing") {
      navigate("/issuing/invoices");
    }
  }, []);
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Outlet />
    </Suspense>
  );
};
