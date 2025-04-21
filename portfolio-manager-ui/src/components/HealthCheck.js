import React, { useEffect, useState } from 'react';
import { Box, Typography, Alert } from '@mui/material';
import { healthCheck } from '../services/api';

const HealthCheck = () => {
    const [status, setStatus] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        healthCheck()
            .then(response => setStatus(response.data))
            .catch(err => setError(err.message));
    }, []);

    return (
        <Box sx={{ p: 2 }}>
            {status && (
                <Alert severity="success">
                    {status}
                </Alert>
            )}
            {error && (
                <Alert severity="error">
                    Service Error: {error}
                </Alert>
            )}
        </Box>
    );
};

export default HealthCheck;