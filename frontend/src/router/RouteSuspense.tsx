import { Outlet, useNavigate } from "react-router-dom";
import "@mantine/core/styles.css";
import { FC, Suspense, useEffect } from "react";
import { useAuth } from "src/util/useAuth";

export const RouteSuspense: FC = () => {
  const navigate = useNavigate();
  const { getTenantNameId, getUserUUID } = useAuth();
  useEffect(() => {
    if (getTenantNameId() === null || getUserUUID() === null) {
      navigate("/change-account");
    }

    if (window.location.pathname === "/recipient")
      navigate("/recipient/invoices");
    if (window.location.pathname === "/issuing") navigate("/issuing/invoices");
    if (window.location.pathname === "/") navigate("/recipient/invoices");
  }, []);
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Outlet />
    </Suspense>
  );
};
