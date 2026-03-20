import axios from "axios";

const API = "http://localhost:8080/api/v1/dich-vu-ky-thuat";

export const getAll = async () => {
  const res = await axios.get(API);
  return res.data.data; // ⚠ vì dùng RestResponse
};

export const getById = async (id) => {
  const res = await axios.get(`${API}/${id}`);
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
  return axios.delete(`${API}/${id}`);
};

export const getTree = async () => {
  const res = await axios.get(`${API}/tree`);
  return res.data.data;
};

export const getRoot = async () => {
  const res = await axios.get(`${API}/root`);
  return res.data.data;
};
