import axios from "axios";

const BASE_URL = "http://localhost:8080/api/wishlist";

export async function addBookToWishlist(token, userProfileId, bookId) {
  const res = await axios.post(
    `${BASE_URL}/${userProfileId}/books/${bookId}`,
    null,
    {
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  return res.data;
}

export async function removeBookFromWishlist(token, userProfileId, bookId) {
  const res = await axios.delete(
    `${BASE_URL}/${userProfileId}/books/${bookId}`,
    {
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  return res.data;
}

export async function getWishlistBooks(token, userProfileId) {
  const res = await axios.get(
    `${BASE_URL}/${userProfileId}/books`,
    {
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  return res.data || [];
}
