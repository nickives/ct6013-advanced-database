import axios from 'axios';

export default async function getJSON<T>(
  url: string,
): Promise<T> {
  const dbChoice = localStorage.getItem('dbChoice');
  return axios.get(url, {
    params: { db: dbChoice },
    responseType: 'json',
  }).then((response) => response.data);
}
