import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  Button,
  Grid,
  Paper,
} from '@mui/material';
import { useAppSelector, useAppDispatch } from '../../hooks/redux';
import { setSelectedProduct, setLoading, setError } from '../../store/slices/productSlice';
import { productService } from '../../services/api/productService';
import Loading from '../../components/common/Loading';
import Error from '../../components/common/Error';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedProduct, loading, error } = useAppSelector(
    (state) => state.product
  );

  useEffect(() => {
    if (id) {
      const fetchProduct = async () => {
        dispatch(setLoading(true));
        try {
          const product = await productService.getProductById(id);
          dispatch(setSelectedProduct(product));
        } catch (err: any) {
          dispatch(setError(err.message || 'Failed to fetch product'));
        }
      };
      fetchProduct();
    }
  }, [id, dispatch]);

  if (loading) return <Loading />;
  if (error) return <Error message={error} />;
  if (!selectedProduct) return <Error message="Product not found" />;

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Button onClick={() => navigate('/products')} sx={{ mb: 2 }}>
        ← Back to Products
      </Button>
      <Paper sx={{ p: 4 }}>
        <Grid container spacing={4}>
          <Grid item xs={12} md={6}>
            {selectedProduct.imageUrl && (
              <Box
                component="img"
                src={selectedProduct.imageUrl}
                alt={selectedProduct.name}
                sx={{ width: '100%', borderRadius: 2 }}
              />
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            <Typography variant="h4" gutterBottom>
              {selectedProduct.name}
            </Typography>
            <Typography variant="h5" color="primary" gutterBottom>
              ${selectedProduct.price.toFixed(2)}
            </Typography>
            <Typography variant="body1" paragraph>
              {selectedProduct.description}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Brand: {selectedProduct.brandName}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Status: {selectedProduct.status}
            </Typography>
          </Grid>
        </Grid>
      </Paper>
    </Container>
  );
};

export default ProductDetail;

