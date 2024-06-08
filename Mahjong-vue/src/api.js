import axios from 'axios';

const BASE_URL = '/api'; // Backend URL

/**
 * POST Request function
 * @param {string} endpoint - The terminal path of the API
 * @param {Object} data - Data to be sent to the server
 * @returns {Promise<Object>} - Response data
 */
export const postData = async (endpoint, data) => {
    try {
        // Pass the data as a form
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