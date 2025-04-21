import React from 'react';
import { Paper, Box, Typography } from '@mui/material';
import { styled } from '@mui/material/styles';

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
}));

const StatCard = ({ title, value, increase, subtitle, color }) => {
  return (
    <StyledPaper elevation={2}>
      <Box>
        <Typography variant="subtitle2" color="textSecondary">
          {title}
        </Typography>
        <Typography variant="h4" color={color}>
          {value}
        </Typography>
      </Box>
      <Box>
        {increase && (
          <Typography variant="body2" color="success.main" component="span">
            {increase}
          </Typography>
        )}
        <Typography variant="caption" color="textSecondary" display="block">
          {subtitle}
        </Typography>
      </Box>
    </StyledPaper>
  );
};

export default StatCard;