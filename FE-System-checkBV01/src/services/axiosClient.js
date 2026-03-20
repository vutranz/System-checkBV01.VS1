import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

axiosClient.interceptors.response.use(
  (response) => {
    if (response.data.success === false) {
      alert(response.data.message);
      return Promise.reject(response.data);
    }
    return response.data.data;
  },
  (error) => {
    alert("Lỗi server");
    return Promise.reject(error);
  },
);

export default axiosClient;
