import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { Category, CategoryTree } from '../../types';

interface CategoryState {
  categories: Category[];
  categoryTree: CategoryTree[];
  selectedCategory: Category | null;
  loading: boolean;
  error: string | null;
}

const initialState: CategoryState = {
  categories: [],
  categoryTree: [],
  selectedCategory: null,
  loading: false,
  error: null,
};

const categorySlice = createSlice({
  name: 'category',
  initialState,
  reducers: {
    setCategories: (state, action: PayloadAction<Category[]>) => {
      state.categories = action.payload;
      state.loading = false;
      state.error = null;
    },
    setCategoryTree: (state, action: PayloadAction<CategoryTree[]>) => {
      state.categoryTree = action.payload;
      state.loading = false;
      state.error = null;
    },
    setSelectedCategory: (state, action: PayloadAction<Category | null>) => {
      state.selectedCategory = action.payload;
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
      state.loading = false;
    },
  },
});

export const {
  setCategories,
  setCategoryTree,
  setSelectedCategory,
  setLoading,
  setError,
} = categorySlice.actions;
export default categorySlice.reducer;

