import httpClient from '../httpClient';
import { API_ENDPOINTS } from '../../constants';
import { LoginRequest, RegisterRequest, LoginResponse, User } from '../../types';

export const authService = {
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await httpClient.post<LoginResponse>(
      API_ENDPOINTS.AUTH.LOGIN,
      credentials
    );
    return response.data;
  },

  register: async (userData: RegisterRequest): Promise<User> => {
    const response = await httpClient.post<User>(
      API_ENDPOINTS.AUTH.REGISTER,
      userData
    );
    return response.data;
  },

  validateToken: async (token: string): Promise<boolean> => {
    try {
      const response = await httpClient.get<boolean>(
        API_ENDPOINTS.AUTH.VALIDATE,
        {
          headers: {
            Authorization: token,
          },
        }
      );
      return response.data;
    } catch {
      return false;
    }
  },
};

