import httpClient from '../httpClient';
import { API_ENDPOINTS } from '../../constants';
import { Product } from '../../types';

export interface ProductFilters {
  q?: string;
  categoryIds?: string[];
  brandIds?: string[];
  status?: string;
  minPrice?: number;
  maxPrice?: number;
  specifications?: Record<string, any>;
  sortBy?: string;
  page?: number;
  size?: number;
}

export interface ProductSearchResponse {
  products: Product[];
  total: number;
  page: number;
  size: number;
}

export const productService = {
  getProducts: async (filters?: ProductFilters): Promise<ProductSearchResponse> => {
    const response = await httpClient.get<ProductSearchResponse>(
      API_ENDPOINTS.PRODUCTS.SEARCH,
      { params: filters }
    );
    return response.data;
  },

  getProductById: async (id: string): Promise<Product> => {
    const response = await httpClient.get<Product>(
      `${API_ENDPOINTS.PRODUCTS.BASE}/${id}`
    );
    return response.data;
  },

  getProductsByCategory: async (
    categoryId: string,
    page: number = 0,
    size: number = 20
  ): Promise<ProductSearchResponse> => {
    const response = await httpClient.get<ProductSearchResponse>(
      `${API_ENDPOINTS.PRODUCTS.BY_CATEGORY}/${categoryId}`,
      { params: { page, size } }
    );
    return response.data;
  },

  createProduct: async (productData: Partial<Product>): Promise<Product> => {
    const response = await httpClient.post<Product>(
      API_ENDPOINTS.PRODUCTS.BASE,
      productData
    );
    return response.data;
  },

  updateProduct: async (
    id: string,
    productData: Partial<Product>
  ): Promise<Product> => {
    const response = await httpClient.put<Product>(
      `${API_ENDPOINTS.PRODUCTS.BASE}/${id}`,
      productData
    );
    return response.data;
  },

  deleteProduct: async (id: string): Promise<void> => {
    await httpClient.delete(`${API_ENDPOINTS.PRODUCTS.BASE}/${id}`);
  },
};


