import axios from "axios";

const API = "/api/v1/lich-lam-viec-theo-thu";

export const getAll = async () => {
  const res = await axios.get(API);
  return res.data.data;
};

export const create = async (data) => {
  const res = await axios.post(API, data);
  return res.data.data;
};

export const update = async (id, data) => {
  const res = await axios.put(`${API}/${id}`, data);
  return res.data.data;
};

export const remove = async (id) => {
  await axios.delete(`${API}/${id}`);
};
