import React, { useState, useEffect, useRef } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  InputBase,
  Badge,
} from '@mui/material';
import {
  ShoppingCart,
  Person,
  Favorite,
  Search,
  Menu as MenuIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAppSelector, useAppDispatch } from '../../hooks/redux';
import { logout } from '../../store/slices/authSlice';
import { setCategoryTree, setLoading } from '../../store/slices/categorySlice';
import { categoryService } from '../../services/api/categoryService';
import CategoryMenu from './CategoryMenu';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { isAuthenticated, user } = useAppSelector((state) => state.auth);
  const { categoryTree } = useAppSelector((state) => state.category);
  const [showCategoryMenu, setShowCategoryMenu] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [hoveredCategory, setHoveredCategory] = useState<string | null>(null);
  const categoryButtonRef = useRef<HTMLButtonElement>(null);
  const categoryRefs = useRef<{ [key: string]: HTMLButtonElement | null }>({});
  const navigationBarRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (categoryTree.length === 0) {
      const fetchCategories = async () => {
        dispatch(setLoading(true));
        try {
          const tree = await categoryService.getCategoryTree();
          dispatch(setCategoryTree(tree));
        } catch (err: any) {
          console.error('Failed to fetch categories:', err);
        } finally {
          dispatch(setLoading(false));
        }
      };
      fetchCategories();
    }
  }, [categoryTree.length, dispatch]);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/products?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  const handleCategoryClick = (categoryId: string) => {
    navigate(`/categories/${categoryId}`);
    setShowCategoryMenu(false);
  };

  const handleCategoryHover = (categoryId: string) => {
    setHoveredCategory(categoryId);
    setShowCategoryMenu(true);
  };

  const handleCategoryMenuClose = () => {
    setShowCategoryMenu(false);
    setHoveredCategory(null);
  };

  return (
    <>
      <AppBar
        position="static"
        sx={{
          zIndex: 1200,
          backgroundColor: 'white',
          color: 'text.primary',
          boxShadow: 1,
        }}
      >
        <Toolbar sx={{ py: 1 }}>
          <Typography
            variant="h5"
            component="div"
            sx={{
              cursor: 'pointer',
              fontWeight: 'bold',
              color: 'primary.main',
              mr: 4,
            }}
            onClick={() => navigate('/')}
          >
            BonusMart
          </Typography>
          <Box
            component="form"
            onSubmit={handleSearch}
            sx={{
              flexGrow: 1,
              display: 'flex',
              alignItems: 'center',
              backgroundColor: 'grey.100',
              borderRadius: 2,
              px: 2,
              py: 0.5,
              maxWidth: 600,
            }}
          >
            <Search sx={{ color: 'text.secondary', mr: 1 }} />
            <InputBase
              placeholder="Ürün, kategori veya marka ara"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              sx={{ flex: 1, fontSize: '0.9rem' }}
            />
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, ml: 4 }}>
            {isAuthenticated ? (
              <>
                <Button
                  startIcon={<Person />}
                  onClick={() => navigate('/account')}
                  sx={{ textTransform: 'none', color: 'text.primary' }}
                >
                  Hesabım
                </Button>
                <IconButton color="inherit" size="small">
                  <Badge badgeContent={0} color="error">
                    <Favorite />
                  </Badge>
                </IconButton>
                <IconButton
                  color="inherit"
                  size="small"
                  onClick={() => navigate('/cart')}
                >
                  <Badge badgeContent={0} color="error">
                    <ShoppingCart />
                  </Badge>
                </IconButton>
                <Button
                  color="inherit"
                  onClick={handleLogout}
                  sx={{ textTransform: 'none' }}
                >
                  Çıkış Yap
                </Button>
              </>
            ) : (
              <>
                <Button
                  color="inherit"
                  onClick={() => navigate('/register')}
                  sx={{ textTransform: 'none' }}
                >
                  Kayıt Ol
                </Button>
                <Button
                  color="inherit"
                  onClick={() => navigate('/login')}
                  sx={{ textTransform: 'none' }}
                >
                  Giriş Yap
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
        <Box
          ref={navigationBarRef}
          sx={{
            position: 'relative',
            borderTop: '1px solid',
            borderColor: 'divider',
            backgroundColor: 'white',
            display: 'flex',
            alignItems: 'center',
            px: 2,
            py: 1,
          }}
        >
          <Button
            ref={categoryButtonRef}
            startIcon={<MenuIcon />}
            onClick={() => setShowCategoryMenu(true)}
            onMouseEnter={() => setShowCategoryMenu(true)}
            sx={{
              textTransform: 'none',
              color: 'text.primary',
              fontWeight: 500,
              mr: 2,
              '&:hover': {
                backgroundColor: 'action.hover',
              },
            }}
          >
            Kategoriler
          </Button>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {categoryTree.map((category) => (
              <Button
                key={category.id}
                ref={(el) => {
                  categoryRefs.current[category.id] = el;
                }}
                onClick={() => handleCategoryClick(category.id)}
                onMouseEnter={() => handleCategoryHover(category.id)}
                onMouseLeave={() => {
                  if (!showCategoryMenu) {
                    setHoveredCategory(null);
                  }
                }}
                sx={{
                  textTransform: 'none',
                  color: hoveredCategory === category.id ? 'primary.main' : 'text.primary',
                  fontSize: '0.9rem',
                  position: 'relative',
                  fontWeight: hoveredCategory === category.id ? 600 : 400,
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                  '&::after': {
                    content: '""',
                    position: 'absolute',
                    bottom: 0,
                    left: 0,
                    right: 0,
                    height: hoveredCategory === category.id ? '2px' : '0px',
                    backgroundColor: 'primary.main',
                    transition: 'height 0.2s',
                  },
                }}
              >
                {category.name}
              </Button>
            ))}
          </Box>
          {showCategoryMenu && (
            <CategoryMenu
              open={showCategoryMenu}
              onClose={handleCategoryMenuClose}
              anchorEl={
                hoveredCategory
                  ? categoryRefs.current[hoveredCategory]
                  : categoryButtonRef.current
              }
              selectedCategoryId={hoveredCategory}
            />
          )}
        </Box>
      </AppBar>
    </>
  );
};

export default Header;
