import { useWindowSizeToPx } from "./useWindowSizeToPx";
import { renderHook } from "@testing-library/react";
import { expect } from "vitest";

describe("useWindowSizeToPx", () => {
  it("should return the correct height and width in px", () => {
    const { result } = renderHook(() => useWindowSizeToPx(50, 50));

    expect(result.current.height).toBe(window.innerHeight / 2);
    expect(result.current.width).toBe(window.innerWidth / 2);
  });

  it("should handle different heights and widths", () => {
    const { result } = renderHook(() => useWindowSizeToPx(25, 75));

    expect(result.current.height).toBe(window.innerHeight / 4);
    expect(result.current.width).toBe((window.innerWidth * 3) / 4);
  });

  // 0の場合
  it("should throw error with zero height", () => {
    expect(() => {
      renderHook(() => useWindowSizeToPx(10, 0));
    }).toThrow("Width must be greater than 0, but got 0");
  });
  // 0の場合
  it("should throw error with zero width", () => {
    expect(() => {
      renderHook(() => useWindowSizeToPx(10, 0));
    }).toThrow("Width must be greater than 0, but got 0");
  });

  //マイナスの場合
  it("should throw error with illegal width value", () => {
    expect(() => {
      renderHook(() => useWindowSizeToPx(10, -5));
    }).toThrow("Width must be greater than 0, but got -5");
  });
  //マイナスの場合
  it("should throw error with illegal height value", () => {
    expect(() => {
      renderHook(() => useWindowSizeToPx(-10, 75));
    }).toThrow("Height must be greater than 0, but got -10");
  });
});
