import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Paper, Typography, Grid } from '@mui/material';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const FundDetail = ({ fundCode, fundName }) => {
  const [priceData, setPriceData] = useState([]);
  const [returns, setReturns] = useState({});

  useEffect(() => {
    const currentDate = new Date().toISOString().split('T')[0];
    const fetchData = async () => {
      try {
        const [pricesResponse, returnsResponse] = await Promise.all([
          axios.get(`http://localhost:8080/api/fund-prices/${fundCode}/yearly-prices?currentDate=${currentDate}`),
          axios.get(`http://localhost:8080/api/fund-prices/${fundCode}/returns?currentDate=${currentDate}`)
        ]);
        // Filter out any data points with null or undefined prices
        const validPriceData = pricesResponse.data.filter(price => price && price.price != null && price.priceDate != null);
        console.log('Returns data:', returnsResponse.data);
        setPriceData(validPriceData);
        setReturns(returnsResponse.data.returns || {});
      } catch (error) {
        console.error('Error fetching fund data:', error);
      }
    };
    fetchData();
  }, [fundCode]);

  const chartData = {
    labels: priceData.map(price => new Date(price.priceDate).toLocaleDateString()),
    datasets: [{
      label: 'Fund Price',
      data: priceData.map(price => price.price),
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.4
    }]
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Historical Price Chart'
      }
    },
    scales: {
      y: {
        beginAtZero: false
      }
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>{fundName || fundCode}</Typography>
          <Paper sx={{ p: 2 }}>
        <Line data={chartData} options={chartOptions} />
      </Paper>
      <Typography variant="h6" gutterBottom>Returns</Typography>
      {Object.entries(returns).map(([period, value]) => (
        <Typography key={period} variant="body1">{`${period}: ${value ? value.toFixed(2) + '%' : 'N/A'}`}</Typography>
      ))}
    </Box>
  );
};

export default FundDetail;