import React, { useEffect } from 'react';
import { Container, Typography, Box, List, ListItem, ListItemText } from '@mui/material';
import { useAppSelector, useAppDispatch } from '../../hooks/redux';
import { setCategoryTree, setLoading, setError } from '../../store/slices/categorySlice';
import { categoryService } from '../../services/api/categoryService';
import Loading from '../../components/common/Loading';
import Error from '../../components/common/Error';

const Categories: React.FC = () => {
  const dispatch = useAppDispatch();
  const { categoryTree, loading, error } = useAppSelector(
    (state) => state.category
  );

  useEffect(() => {
    const fetchCategories = async () => {
      dispatch(setLoading(true));
      try {
        const tree = await categoryService.getCategoryTree();
        dispatch(setCategoryTree(tree));
      } catch (err: any) {
        dispatch(setError(err.message || 'Failed to fetch categories'));
      }
    };

    fetchCategories();
  }, [dispatch]);

  if (loading) return <Loading />;
  if (error) return <Error message={error} />;

  const renderCategory = (category: any, level: number = 0) => (
    <Box key={category.id} sx={{ ml: level * 2 }}>
      <ListItem>
        <ListItemText primary={category.name} secondary={category.description} />
      </ListItem>
      {category.children && category.children.length > 0 && (
        <List>
          {category.children.map((child: any) => renderCategory(child, level + 1))}
        </List>
      )}
    </Box>
  );

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" gutterBottom>
        Categories
      </Typography>
      <List>
        {categoryTree.map((category) => renderCategory(category))}
      </List>
    </Container>
  );
};

export default Categories;


