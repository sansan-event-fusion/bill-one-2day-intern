// 今回テナント名とアカウントを便宜上ローカルストレージに保存するため。
export const useLocalStorage = () => {
  const get = (key: string): string | null => {
    return localStorage.getItem(key);
  };

  const set = (key: string, value: string) => {
    localStorage.setItem(key, value);
  };

  const remove = (key: string) => {
    localStorage.removeItem(key);
  };

  const clear = () => {
    localStorage.clear();
  };

  const isExist = (key: string) => {
    return Object.prototype.hasOwnProperty.call(localStorage, key);
  };

  return { get, set, remove, clear, isExist };
};
