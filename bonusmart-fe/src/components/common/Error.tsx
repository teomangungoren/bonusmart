import React from 'react';
import { Alert, AlertTitle } from '@mui/material';

interface ErrorProps {
  message: string;
  title?: string;
}

const Error: React.FC<ErrorProps> = ({ message, title = 'Error' }) => {
  return (
    <Alert severity="error">
      <AlertTitle>{title}</AlertTitle>
      {message}
    </Alert>
  );
};

export default Error;


