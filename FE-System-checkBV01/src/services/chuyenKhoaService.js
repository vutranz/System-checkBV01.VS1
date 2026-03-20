import axiosClient from "./axiosClient";

const API = "/api/v1/chuyen-khoa";

export const getAll = () => axiosClient.get(API);
export const getById = (id) => axiosClient.get(`${API}/${id}`);
export const create = (data) => axiosClient.post(API, data);
export const update = (id, data) => axiosClient.put(`${API}/${id}`, data);
export const remove = (id) => axiosClient.delete(`${API}/${id}`);
