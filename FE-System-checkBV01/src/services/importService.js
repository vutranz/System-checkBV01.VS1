import axios from "axios";

const API_URL = "http://localhost:8080/api/import";

export const importHoSo = async (files) => {
  const formData = new FormData();
  formData.append("xml1", files.xml1);
  formData.append("xml2", files.xml2);
  formData.append("xml3", files.xml3);
  formData.append("xml4", files.xml4);

  return await axios.post(`${API_URL}/ho-so`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const getAllHoSo = async () => {
  const res = await axios.get(`${API_URL}/ho-so`);
  return res.data;
};

export const getByMaLk = async (maLk) => {
  const res = await axios.get(`${API_URL}/ho-so/${maLk}`);
  return res.data;
};
