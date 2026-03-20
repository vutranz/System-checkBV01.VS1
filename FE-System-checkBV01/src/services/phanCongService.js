import axios from "axios";

const BASE_URL = "/api/v1/phan-cong-nang-luc";

export const getByBacSi = async (id) => {
  const res = await axios.get(`${BASE_URL}/bac-si/${id}`);
  return res.data.data; // 👈 QUAN TRỌNG
};

export const create = (data) => axios.post(BASE_URL, data);

export const remove = (id) => axios.delete(`${BASE_URL}/${id}`);

export const filter = (thu, caLamViecId) =>
  axios.get(`${BASE_URL}/filter`, {
    params: { thu, caLamViecId },
  });
