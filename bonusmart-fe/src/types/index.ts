export interface User {
  id: string;
  email: string;
  name?: string;
}

export interface Product {
  id: string;
  name: string;
  description?: string;
  brandId: string;
  brandName: string;
  price: number;
  status: string;
  isActive: boolean;
  imageUrl?: string;
  specifications?: Record<string, any>;
  categoryIds: string[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Category {
  id: string;
  name: string;
  description?: string;
  parentId?: string;
  childrenCount: number;
  isActive: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface CategoryTree extends Category {
  children: CategoryTree[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name?: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface ApiError {
  message: string;
  status?: number;
}


