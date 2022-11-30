import { useState } from 'react';

interface RESTError {
  error?: string;
  detail?: string;
}

type ErrorReturn = {
  message: string;
  statusCode: number;
};

type RestSubmitHook<T, V> = [
  loading: boolean,
  error: ErrorReturn | undefined,
  data: T | undefined,
  submitFn: (variables: V, path: string) => void
];

/**
 * Submit hook for our REST endpoints
 *
 * @template T Type for data object
 * @template V Values for submit function
 * @returns loading, error, data, submitFn
 */
function useRESTSubmit<T, V>(): RestSubmitHook<T, V> {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<ErrorReturn | undefined>(undefined);
  const [data, setData] = useState<T | undefined>(undefined);

  async function submitFn(variables: V, url: string) {
    setLoading(true);
    setData(undefined);
    setError(undefined);

    try {
      const { location } = window;
      const uriPrefix = `${location.protocol}//${location.host}`;
      const response = await window.fetch(`${uriPrefix}${url}`, {
        method: 'POST',
        headers: {
          'content-type': 'application/json;charset=UTF-8',
        },
        body: JSON.stringify(variables),
      });
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      const decoded: T & RESTError = await response.json();
      if (decoded?.error) {
        const message = decoded?.detail
          ? decoded.detail
          : decoded?.error
            ? decoded.error
            : `Error: Response status ${response.status} ${response.statusText}`;
        setError({
          message: message,
          statusCode: response.status,
        });
      } else {
        setData(decoded);
      }
    } catch (err) {
      setError({
        message: (err as any).message,
        statusCode: 0,
      });
      setData(undefined);
    }
    setLoading(false);
  }
  return [loading, error, data, submitFn];
}

export default useRESTSubmit;
