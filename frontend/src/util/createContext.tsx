import {
  createContext as createReactContext,
  Provider,
  useContext as useReactContext,
} from "react";

export function createContext<T>(): readonly [Provider<T | null>, () => T] {
  const genericContext = createReactContext<T | null>(null);

  const useContext = () => {
    const context = useReactContext(genericContext);
    if (context === null) {
      throw new Error("this context is not used in ContextProvider");
    }
    return context;
  };

  return [genericContext.Provider, useContext] as const;
}
