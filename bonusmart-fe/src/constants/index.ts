export const API_BASE_URL = 'http://localhost:8082';

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/users/login',
    REGISTER: '/users/register',
    VALIDATE: '/token/validate',
  },
  PRODUCTS: {
    BASE: '/v1/products',
    SEARCH: '/v1/products/search',
    BY_CATEGORY: '/v1/products/categories',
  },
  CATEGORIES: {
    BASE: '/api/v1/categories',
    TREE: '/api/v1/categories/tree',
    ANCESTORS: '/api/v1/categories',
    DESCENDANTS: '/api/v1/categories',
  },
} as const;

export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
} as const;

