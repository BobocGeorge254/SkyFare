import axios from "axios";

export async function fetchAuthors(token) {
  const res = await axios.get("http://localhost:8080/api/authors", {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data || [];
}
