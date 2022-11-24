export default async function getJSON<T>(
  url: string,
  params?: string | string[][] | Record<string, string> | URLSearchParams,
): Promise<T> {
  return fetch(url + new URLSearchParams(params), {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  }).then((response) => response.json());
}
