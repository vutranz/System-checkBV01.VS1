import axios from "axios";

const doiSoatService = {
  exportCompareTxt: async (winSoftFile, gateBHYTFile) => {
    const formData = new FormData();
    formData.append("winSoftFile", winSoftFile);
    formData.append("gateBHYTFile", gateBHYTFile);

    const response = await axios.post(
      "http://localhost:8080/api/v1/hsba/export-compare-txt",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        responseType: "blob", // Nhận chuẩn dữ liệu file blob
      },
    );
    return response.data; // Trả về blob chuẩn từ axios gốc
  },
};

export default doiSoatService;
