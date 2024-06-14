export type Result<T, E> = Success<T> | Failure<E>;

type Success<T> = {
  readonly value: T;
  readonly error: undefined;
  readonly isSuccess: true;
  readonly isFailure: false;
};
type Failure<E> = {
  readonly value: undefined;
  readonly error: E;
  readonly isSuccess: false;
  readonly isFailure: true;
};

export const createSuccess = <T = void>(value?: T): Success<T> => {
  return {
    value: value as T,
    error: undefined,
    isSuccess: true,
    isFailure: false,
  };
};

export const createFailure = <E = void>(error?: E): Failure<E> => {
  return {
    value: undefined,
    error: error as E,
    isSuccess: false,
    isFailure: true,
  };
};
