import httpClient from '../httpClient';
import { API_ENDPOINTS } from '../../constants';
import { Category, CategoryTree } from '../../types';

export const categoryService = {
  getCategories: async (): Promise<Category[]> => {
    const response = await httpClient.get<Category[]>(
      API_ENDPOINTS.CATEGORIES.BASE
    );
    return response.data;
  },

  getCategoryById: async (id: string): Promise<Category> => {
    const response = await httpClient.get<Category>(
      `${API_ENDPOINTS.CATEGORIES.BASE}/${id}`
    );
    return response.data;
  },

  getCategoryTree: async (rootCategoryId?: string): Promise<CategoryTree[]> => {
    const params = rootCategoryId ? { rootCategoryId } : {};
    const response = await httpClient.get<CategoryTree[]>(
      API_ENDPOINTS.CATEGORIES.TREE,
      { params }
    );
    return response.data;
  },

  getAncestorIds: async (categoryId: string): Promise<string[]> => {
    const response = await httpClient.get<string[]>(
      `${API_ENDPOINTS.CATEGORIES.ANCESTORS}/${categoryId}/ancestors`
    );
    return response.data;
  },

  getDescendantIds: async (categoryId: string): Promise<string[]> => {
    const response = await httpClient.get<string[]>(
      `${API_ENDPOINTS.CATEGORIES.DESCENDANTS}/${categoryId}/descendants`
    );
    return response.data;
  },
};


