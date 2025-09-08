import axios from "axios";

const BASE_URL = "http://localhost:8080/api/books";

export async function fetchBooks(token, { page = 0, size = 10, sortBy = "id", direction = "asc", authorId = null, categoryId = null } = {}) {
  const params = { page, size, sortBy, direction };

  if (authorId && authorId !== "all") {
    params.authorId = authorId;
  }
  if (categoryId && categoryId !== "all") {
    params.categoryId = categoryId;
  }

  const res = await axios.get(BASE_URL, {
    headers: { Authorization: `Bearer ${token}` },
    params,
  });

  return res.data.content || [];
}

export async function fetchBookById(token, id) {
  const res = await axios.get(`${BASE_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
}

export async function createBook(token, newBook) {
  const formData = new FormData();
  formData.append("title", newBook.title);
  formData.append("authorId", newBook.authorId);
  formData.append("categoryId", newBook.categoryId);
  if (newBook.image) formData.append("image", newBook.image);

  return axios.post(BASE_URL, formData, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function updateBook(token, id, newBook) {
  const formData = new FormData();
  if (newBook.title) formData.append("title", newBook.title);
  if (newBook.authorId) formData.append("authorId", newBook.authorId);
  if (newBook.categoryId) formData.append("categoryId", newBook.categoryId);
  if (newBook.image) formData.append("image", newBook.image);

  return axios.put(`${BASE_URL}/${id}`, formData, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function deleteBook(token, id) {
  return axios.delete(`${BASE_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
}
