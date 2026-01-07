import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { Box, Grid, Paper, Typography, Button, TextField } from '@mui/material';
import { styled } from '@mui/material/styles';
import StatCard from './StatCard';
import LineChart from './LineChart';

const Item = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  color: theme.palette.text.secondary,
}));

const Dashboard = () => {
  const [isUpdating, setIsUpdating] = useState(false);
  const [statusText, setStatusText] = useState('');
  const pollRef = useRef(null);

  const fetchCrawlStatus = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/fund-prices/crawl/status');
      const inProgress = !!res.data?.inProgress;
      setIsUpdating(inProgress);
      if (inProgress) {
        setStatusText('devam ediyor ...');
      } else if (statusText === 'devam ediyor ...') {
        setStatusText('fiyat güncellendi');
      }
    } catch (e) {
      // ignore
    }
  };

  useEffect(() => {
    fetchCrawlStatus();
  }, []);

  useEffect(() => {
    if (isUpdating && !pollRef.current) {
      pollRef.current = setInterval(fetchCrawlStatus, 5000);
    }
    if (!isUpdating && pollRef.current) {
      clearInterval(pollRef.current);
      pollRef.current = null;
    }
    return () => {
      if (pollRef.current) {
        clearInterval(pollRef.current);
        pollRef.current = null;
      }
    };
  }, [isUpdating]);

  const handleTriggerUpdate = async () => {
    if (isUpdating) return;
    try {
      const res = await axios.post('http://localhost:8080/api/fund-prices/crawl');
      if (res.status === 202 || String(res.data).toLowerCase().includes('started')) {
        setIsUpdating(true);
        setStatusText('devam ediyor ...');
        fetchCrawlStatus();
      } else if (res.status === 409) {
        setIsUpdating(true);
        setStatusText('devam ediyor ...');
      }
    } catch (e) {
      // if already in progress
      if (e.response?.status === 409) {
        setIsUpdating(true);
        setStatusText('devam ediyor ...');
      }
    }
  };

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Button variant="contained" color="primary" disabled={isUpdating} onClick={handleTriggerUpdate}>
          Fon Fiyatlarını Güncelle
        </Button>
        <TextField
          value={statusText}
          size="small"
          sx={{ ml: 2, width: 280 }}
          inputProps={{ readOnly: true }}
        />
      </Box>
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
