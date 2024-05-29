import axios from 'axios';

const BASE_URL = '/api'; // 后端 URL

/**
 * GET 请求函数
 * @param {string} endpoint - API 的终端路径
 * @param {Object} [params={}] - 查询参数
 * @returns {Promise<Object>} - 响应数据
 */
export const getData = async (endpoint, params = {}) => {
    try {
        // 将数据作为表单形式传递
        const formData = new FormData();
        for (const key in data) {
            formData.append(key, data[key]);
        }
        const response = await axios.get(`${BASE_URL}/${endpoint}`, { params });
        return response.data.data;
    } catch (error) {
        throw new Error(`Failed to fetch data: ${error.response ? error.response.data.message : error.message}`);
    }
};

/**
 * POST 请求函数
 * @param {string} endpoint - API 的终端路径
 * @param {Object} data - 要发送到服务器的数据
 * @returns {Promise<Object>} - 响应数据
 */
export const postData = async (endpoint, data) => {
    try {
        // 将数据作为表单形式传递
        const formData = new FormData();
        for (const key in data) {
            formData.append(key, data[key]);
        }
        const response = await axios.post(`${BASE_URL}/${endpoint}`, formData);
        return response.data.data;
    } catch (error) {
        throw new Error(`Failed to post data: ${error.response ? error.response.data.message : error.message}`);
    }
};