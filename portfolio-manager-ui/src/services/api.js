import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const healthCheck = async () => {
    try {
        const response = await api.get('/health');
        console.log('Health check response:', response);
        return response;
    } catch (error) {
        console.error('Health check error:', error);
        throw error;
    }
};

export default api;