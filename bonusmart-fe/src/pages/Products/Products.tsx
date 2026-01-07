import React, { useEffect } from 'react';
import { Container, Grid, Box } from '@mui/material';
import { useAppSelector, useAppDispatch } from '../../hooks/redux';
import { setProducts, setLoading, setError } from '../../store/slices/productSlice';
import { productService } from '../../services/api/productService';
import ProductCard from '../../components/product/ProductCard';
import Loading from '../../components/common/Loading';
import Error from '../../components/common/Error';

const Products: React.FC = () => {
  const dispatch = useAppDispatch();
  const { products, loading, error } = useAppSelector((state) => state.product);

  useEffect(() => {
    const fetchProducts = async () => {
      dispatch(setLoading(true));
      try {
        const response = await productService.getProducts();
        dispatch(setProducts(response.products));
      } catch (err: any) {
        dispatch(setError(err.message || 'Failed to fetch products'));
      }
    };

    fetchProducts();
  }, [dispatch]);

  if (loading) return <Loading />;
  if (error) return <Error message={error} />;

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Grid container spacing={3}>
        {products.map((product) => (
          <Grid item xs={12} sm={6} md={4} key={product.id}>
            <ProductCard product={product} />
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default Products;

