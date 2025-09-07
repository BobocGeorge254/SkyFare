import axios from "axios";

const BASE_URL = "http://localhost:8080/api/reviews";

export async function fetchReviewsForBook(token, bookId) {
  const res = await axios.get(`${BASE_URL}/book/${bookId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data || [];
}

export async function fetchReviewsByUser(token, userProfileId) {
  const res = await axios.get(`${BASE_URL}/user/${userProfileId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data || [];
}

export async function createReview(token, bookId, userProfileId, rating, comment) {
  const payload = { bookId, userProfileId, rating, comment };
  const res = await axios.post(BASE_URL, payload, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
}

export async function deleteReview(token, id) {
  return axios.delete(`${BASE_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
}
