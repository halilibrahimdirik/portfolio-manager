import React from 'react';
import { Box, Grid, Paper, Typography } from '@mui/material';
import { styled } from '@mui/material/styles';
import StatCard from './StatCard';
import LineChart from './LineChart';

const Item = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  color: theme.palette.text.secondary,
}));

const Dashboard = () => {
  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Grid container spacing={3}>
        {/* Stats Cards */}
        <Grid item xs={12} container spacing={3}>
          <Grid item xs={3}>
            <StatCard
              title="Total Value"
              value="281"
              increase="+55%"
              subtitle="than last week"
              color="primary"
            />
          </Grid>
          <Grid item xs={3}>
            <StatCard
              title="Daily Gain/Lost"
              value="2,300"
              increase="+5%"
              subtitle="than last month"
              color="info"
            />
          </Grid>
          <Grid item xs={3}>
            <StatCard
              title="Revenue"
              value="34k"
              increase="+1%"
              subtitle="than yesterday"
              color="success"
            />
          </Grid>
          <Grid item xs={3}>
            <StatCard
              title="Followers"
              value="+91"
              subtitle="Just updated"
              color="secondary"
            />
          </Grid>
        </Grid>

        {/* Charts */}
        <Grid item xs={12} container spacing={3}>
          <Grid item xs={4}>
            <Item>
              <Typography variant="h6" gutterBottom>Website Views</Typography>
              <LineChart color="primary" />
              <Typography variant="caption">Last Campaign Performance</Typography>
            </Item>
          </Grid>
          <Grid item xs={4}>
            <Item>
              <Typography variant="h6" gutterBottom>Daily Sales</Typography>
              <LineChart color="success" />
              <Typography variant="caption">+15% increase in today sales</Typography>
            </Item>
          </Grid>
          <Grid item xs={4}>
            <Item>
              <Typography variant="h6" gutterBottom>Completed Tasks</Typography>
              <LineChart color="secondary" />
              <Typography variant="caption">Last Campaign Performance</Typography>
            </Item>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;