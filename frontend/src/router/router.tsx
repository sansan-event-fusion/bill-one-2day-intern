import React, { FC, lazy } from "react";
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import { ErrorBoundary } from "src/components/pages/ErrorPage";
import { NotFoundPage } from "src/components/pages/NotFound";
import { AccountChangePage } from "src/components/pages/AccountChangePage";
import { RouteSuspense } from "src/router/RouteSuspense";
const RecipientRouter = lazy(() => import("./recipient"));
const IssuingRouter = lazy(() => import("./issuing"));

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route
      path={"/"}
      element={<RouteSuspense />}
      errorElement={<ErrorBoundary />}
    >
      <Route path={"/recipient/*"} element={<RecipientRouter />} />
      <Route path={"/issuing/*"} element={<IssuingRouter />} />
      <Route path={"/change-account"} element={<AccountChangePage />} />
      <Route path="*" element={<NotFoundPage />} />
    </Route>,
  ),
);
export const AppRoute: FC = () => {
  return <RouterProvider router={router} />;
};
