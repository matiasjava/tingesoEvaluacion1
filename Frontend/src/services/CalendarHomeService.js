import axios from 'axios';

const API_URL = `${import.meta.env.VITE_PAYROLL_BACKEND_SERVER}/api/reserves/`;

/**
 * 
 * @returns {Promise<Array>} 
 */
export const getReservas = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data; 
  } catch (error) {
    console.error('Error al obtener las reservas:', error);
    throw error;
  }
};