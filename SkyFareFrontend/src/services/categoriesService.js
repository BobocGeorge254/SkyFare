import axios from "axios";

export async function fetchCategories(token) {
  const res = await axios.get("http://localhost:8080/api/categories", {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data || [];
}

export async function addCategory(token, name) {
  return axios.post(
    "http://localhost:8080/api/categories",
    null,
    {
      params: { name },
      headers: { Authorization: `Bearer ${token}` },
    }
  );
}

export async function updateCategory(token, id, name) {
  return axios.put(
    `http://localhost:8080/api/categories/${id}`,
    null,
    {
      params: { name },
      headers: { Authorization: `Bearer ${token}` },
    }
  );
}

export async function deleteCategory(token, id) {
  return axios.delete(`http://localhost:8080/api/categories/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
}
