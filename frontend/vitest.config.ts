import { mergeConfig } from "vite";
import { defineConfig } from "vitest/config";
import viteConfig from "./vite.config";

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      include: [
        "src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}",
        "**/__tests__/**/*.{js,mjs,cjs,ts,mts,cts,jsx,tsx}",
      ],
      globals: true,
      environment: "jsdom",
      setupFiles: ["./setupTests.ts"],
    },
  }),
);
