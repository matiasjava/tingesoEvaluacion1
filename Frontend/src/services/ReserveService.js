import axios from 'axios';

const API_URL = `${import.meta.env.VITE_PAYROLL_BACKEND_SERVER}/api/reserves/`;
const USER_API_URL = `${import.meta.env.VITE_PAYROLL_BACKEND_SERVER}/api/users/`;

export const getReserves = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data;
  } catch (error) {
    console.error('Error fetching reserves:', error);
    throw error;
  }
};

export const createReserve = async (reserve) => {
  try {
    const response = await axios.post(API_URL, reserve, {
      headers: { 'Content-Type': 'application/json' },
    });
    return response.data;
  } catch (error) {
    console.error('Error creating reserve:', error);
    throw error;
  }
};

export const confirmReserve = async (reserve) => {
  try {
    const response = await axios.post(`${API_URL}confirmar`, reserve, {
      headers: { 'Content-Type': 'application/json' },
    });
    return response.data;
  } catch (error) {
    console.error('Error confirming reserve:', error);
    throw error;
  }
};

export const getUserByRut = async (rut) => {
  try {
    const response = await axios.get(`${USER_API_URL}findByRut/${rut}`);
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 404) {
      return null;
    }
    console.error('Error fetching user by RUT:', error);
    throw error;
  }
};

export const createUser = async (user) => {
  try {
    const response = await axios.post(USER_API_URL, user, {
      headers: { 'Content-Type': 'application/json' },
    });
    return response.data;
  } catch (error) {
    console.error('Error creating user:', error);
    throw error;
  }
};