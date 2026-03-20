import axios from "axios";

const API = "http://localhost:8080/api/v1/nang-luc-chuyen-mon";

// Lấy tất cả
export const getAll = async () => {
  const res = await axios.get(API);
  return res.data.data;
};

// Lấy theo bác sĩ
export const getByBacSi = async (bacSiId) => {
  const res = await axios.get(`${API}/bac-si/${bacSiId}`);
  return res.data.data;
};

// Lấy theo DVKT
export const getByDvkt = async (dvktId) => {
  const res = await axios.get(`${API}/dvkt/${dvktId}`);
  return res.data.data;
};

// Tạo mới
export const create = async (data) => {
  const res = await axios.post(API, data);
  return res.data.data;
};

// Cập nhật
export const update = async (id, data) => {
  const res = await axios.put(`${API}/${id}`, data);
  return res.data.data;
};

// Xoá
export const remove = async (id) => {
  return axios.delete(`${API}/${id}`);
};
