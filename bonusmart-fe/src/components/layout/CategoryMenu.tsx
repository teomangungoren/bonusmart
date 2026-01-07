import React, { useEffect, useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  Grid,
  List,
  ListItem,
  ListItemText,
  Divider,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAppSelector, useAppDispatch } from '../../hooks/redux';
import { setCategoryTree, setLoading, setError } from '../../store/slices/categorySlice';
import { categoryService } from '../../services/api/categoryService';
import { CategoryTree } from '../../types';

interface CategoryMenuProps {
  open: boolean;
  onClose: () => void;
  anchorEl?: HTMLElement | null;
  selectedCategoryId?: string | null;
}

const CategoryMenu: React.FC<CategoryMenuProps> = ({
  open,
  onClose,
  anchorEl,
  selectedCategoryId,
}) => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { categoryTree, loading } = useAppSelector((state) => state.category);
  const [hoveredCategory, setHoveredCategory] = useState<string | null>(
    selectedCategoryId || null
  );
  const [hoveredSubCategory, setHoveredSubCategory] = useState<string | null>(null);

  useEffect(() => {
    setHoveredCategory(selectedCategoryId || null);
  }, [selectedCategoryId]);

  useEffect(() => {
    if (open && categoryTree.length === 0 && !loading) {
      const fetchCategories = async () => {
        dispatch(setLoading(true));
        try {
          const tree = await categoryService.getCategoryTree();
          dispatch(setCategoryTree(tree));
        } catch (err: any) {
          dispatch(setError(err.message || 'Failed to fetch categories'));
        } finally {
          dispatch(setLoading(false));
        }
      };
      fetchCategories();
    }
  }, [open, categoryTree.length, loading, dispatch]);

  const handleCategoryClick = (categoryId: string) => {
    navigate(`/categories/${categoryId}`);
    onClose();
  };

  const handleCategoryHover = (categoryId: string) => {
    setHoveredCategory(categoryId);
    setHoveredSubCategory(null);
  };

  const handleSubCategoryHover = (subCategoryId: string) => {
    setHoveredSubCategory(subCategoryId);
  };

  const renderSubCategories = (categories: CategoryTree[]) => {
    if (!categories || categories.length === 0) return null;

    return (
      <List dense sx={{ py: 0 }}>
        {categories.slice(0, 5).map((category) => (
          <ListItem
            key={category.id}
            button
            onClick={() => handleCategoryClick(category.id)}
            onMouseEnter={() => handleSubCategoryHover(category.id)}
            sx={{
              py: 0.5,
              px: 1,
              backgroundColor:
                hoveredSubCategory === category.id ? 'action.hover' : 'transparent',
              '&:hover': {
                backgroundColor: 'action.hover',
                borderRadius: 1,
              },
            }}
          >
            <ListItemText
              primary={category.name}
              primaryTypographyProps={{
                variant: 'body2',
                sx: { fontSize: '0.875rem', color: 'text.primary' },
              }}
            />
          </ListItem>
        ))}
        {categories.length > 5 && (
          <ListItem
            button
            onClick={() => handleCategoryClick(categories[0].id)}
            sx={{
              py: 0.5,
              px: 1,
              color: 'primary.main',
              '&:hover': {
                backgroundColor: 'action.hover',
                borderRadius: 1,
              },
            }}
          >
            <ListItemText
              primary="Daha Fazla Gör ⌄"
              primaryTypographyProps={{
                variant: 'body2',
                sx: { fontSize: '0.875rem', fontWeight: 500 },
              }}
            />
          </ListItem>
        )}
      </List>
    );
  };

  const renderCategoryGroup = (
    title: string,
    categories: CategoryTree[],
    isHovered: boolean
  ) => {
    if (!categories || categories.length === 0) return null;

    return (
      <Box sx={{ mb: 3 }}>
        <Typography
          variant="subtitle1"
          sx={{
            fontWeight: 600,
            mb: 1.5,
            color: isHovered ? 'primary.main' : 'text.primary',
            cursor: 'pointer',
            transition: 'color 0.2s',
            '&:hover': {
              color: 'primary.main',
            },
          }}
          onClick={() => categories[0] && handleCategoryClick(categories[0].id)}
          onMouseEnter={() => categories[0] && handleSubCategoryHover(categories[0].id)}
        >
          {title} ›
        </Typography>
        <Divider sx={{ mb: 1 }} />
        {renderSubCategories(categories)}
      </Box>
    );
  };

  const renderMegaMenu = (category: CategoryTree) => {
    if (!category.children || category.children.length === 0) return null;

    const children = category.children;
    const columnCount = Math.min(4, children.length);
    const itemsPerColumn = Math.ceil(children.length / columnCount);

    const columns = [];
    for (let i = 0; i < columnCount; i++) {
      const start = i * itemsPerColumn;
      const end = Math.min(start + itemsPerColumn, children.length);
      columns.push(children.slice(start, end));
    }

    return (
      <Grid container spacing={3} sx={{ py: 2 }}>
        {columns.map((columnCategories, colIndex) => (
          <Grid item xs={12} sm={6} md={3} key={colIndex}>
            {columnCategories.map((child) => {
              const isHovered = hoveredSubCategory === child.id;
              return (
                <Box key={child.id} sx={{ mb: 3 }}>
                  {renderCategoryGroup(child.name, child.children || [], isHovered)}
                </Box>
              );
            })}
          </Grid>
        ))}
      </Grid>
    );
  };

  if (!open) return null;

  const selectedCategoryData = categoryTree.find(
    (cat) => cat.id === (hoveredCategory || selectedCategoryId)
  );

  return (
    <Paper
      sx={{
        position: 'absolute',
        top: '100%',
        left: 0,
        right: 0,
        minHeight: 400,
        maxHeight: 600,
        overflow: 'auto',
        boxShadow: 4,
        zIndex: 1300,
        backgroundColor: 'white',
        mt: 0,
      }}
      onMouseLeave={onClose}
    >
      <Box sx={{ display: 'flex', maxWidth: 1400, mx: 'auto' }}>
        <Box
          sx={{
            width: 250,
            backgroundColor: 'grey.50',
            borderRight: '1px solid',
            borderColor: 'divider',
            py: 2,
          }}
        >
          {loading ? (
            <Box sx={{ p: 2 }}>
              <Typography>Loading...</Typography>
            </Box>
          ) : (
            <List sx={{ py: 0 }}>
              {categoryTree.map((category) => {
                const isSelected = hoveredCategory === category.id;
                return (
                  <ListItem
                    key={category.id}
                    button
                    selected={isSelected}
                    onClick={() => handleCategoryClick(category.id)}
                    onMouseEnter={() => handleCategoryHover(category.id)}
                    sx={{
                      py: 1.5,
                      px: 2,
                      backgroundColor: isSelected ? 'primary.main' : 'transparent',
                      color: isSelected ? 'white' : 'text.primary',
                      '&:hover': {
                        backgroundColor: isSelected ? 'primary.dark' : 'action.hover',
                      },
                      '&.Mui-selected': {
                        backgroundColor: 'primary.main',
                        color: 'white',
                        '&:hover': {
                          backgroundColor: 'primary.dark',
                        },
                      },
                    }}
                  >
                    <ListItemText
                      primary={category.name.toUpperCase()}
                      primaryTypographyProps={{
                        variant: 'body1',
                        sx: {
                          fontWeight: isSelected ? 600 : 500,
                          fontSize: '0.9rem',
                        },
                      }}
                    />
                    {isSelected && (
                      <Typography sx={{ ml: 'auto', color: 'white' }}>›</Typography>
                    )}
                  </ListItem>
                );
              })}
            </List>
          )}
        </Box>
        <Box sx={{ flex: 1, px: 3, py: 2 }}>
          {selectedCategoryData ? (
            <>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 'bold',
                  mb: 2,
                  color: 'primary.main',
                  borderBottom: '2px solid',
                  borderColor: 'primary.main',
                  pb: 1,
                }}
              >
                {selectedCategoryData.name}
              </Typography>
              {renderMegaMenu(selectedCategoryData)}
            </>
          ) : (
            <Box sx={{ py: 4, textAlign: 'center' }}>
              <Typography color="text.secondary">
                Bir kategori seçin veya üzerine gelin
              </Typography>
            </Box>
          )}
        </Box>
      </Box>
    </Paper>
  );
};

export default CategoryMenu;
