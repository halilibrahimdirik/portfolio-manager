import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Button, IconButton, Grid, Tooltip, Dialog, DialogContent } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import StockDialog from '../shared/StockDialog';
import FundDetail from './FundDetail';

const Tefas = () => {
  const [open, setOpen] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [stocks, setStocks] = useState([]);
  const [profitSummary, setProfitSummary] = useState(null);
  const [detailOpen, setDetailOpen] = useState(false);
  const [selectedFundCode, setSelectedFundCode] = useState(null);

  const handleRowClick = (fundCode) => {
    setSelectedFundCode(fundCode);
    setDetailOpen(true);
  };

  const handleDetailClose = () => {
    setDetailOpen(false);
    setSelectedFundCode(null);
  };

  const fetchStocks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/assets/TEFAS');
      setStocks(response.data);
    } catch (error) {
      console.error('Error fetching TEFAS funds:', error);
    }
  };

  const fetchProfitSummary = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/assets/TEFAS/profit-summary');
      setProfitSummary(response.data);
    } catch (error) {
      console.error('Error fetching profit summary:', error);
    }
  };

  const handleClose = () => {
    setSelectedStock(null);
    setOpen(false);
  };

  const handleClickOpen = (stock = null) => {
    setSelectedStock(stock);
    setOpen(true);
  };

  useEffect(() => {
    fetchStocks();
    fetchProfitSummary();
  }, []);

  const handleSave = async (stockData) => {
    try {
      const formattedStock = {
        id: selectedStock ? selectedStock.id : crypto.randomUUID(),
        assetName: selectedStock ? selectedStock.assetName : stockData.symbol,
        assetCode: selectedStock ? selectedStock.assetCode : stockData.symbol,
        quantity: parseFloat(stockData.quantity),
        currentPrice: parseFloat(stockData.price),
        purchasePrice: parseFloat(stockData.purchasePrice),
        purchaseDate: selectedStock ? selectedStock.purchaseDate : new Date().toISOString().split('T')[0],
        type: 'TEFAS'
      };

      // Convert to string to preserve decimal precision
      formattedStock.currentPrice = formattedStock.currentPrice.toString();
      formattedStock.purchasePrice = formattedStock.purchasePrice.toString();

      if (selectedStock) {
        await axios.put(`http://localhost:8080/api/assets/${selectedStock.id}`, formattedStock);
      } else {
        await axios.post('http://localhost:8080/api/assets', formattedStock);
      }
      fetchStocks();
      fetchProfitSummary();
      handleClose();
    } catch (error) {
      console.error('Error saving TEFAS fund:', error);
    }
  };

  const handleDelete = async (stockToDelete) => {
    try {
      await axios.delete(`http://localhost:8080/api/assets/${stockToDelete.id}`);
      fetchStocks();
      fetchProfitSummary(); // Refresh summary after delete
    } catch (error) {
      console.error('Error deleting TEFAS fund:', error);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* Add Summary Section */}
      {profitSummary && (
        <Box sx={{ mb: 3, p: 2, bgcolor: 'background.paper', borderRadius: 1 }}>
          <Grid container spacing={4}>
            <Grid item>
              <Typography variant="subtitle1" color="text.secondary">Total Portfolio Value</Typography>
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                ₺{Number(profitSummary.totalValue).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </Typography>
            </Grid>
            <Grid item>
              <Typography variant="subtitle1" color="text.secondary">Total Profit/Loss</Typography>
              <Typography variant="h6" sx={{ 
                fontWeight: 'bold', 
                color: profitSummary.profitPercentage >= 0 ? 'success.main' : 'error.main' 
              }}>
                ₺{Number(profitSummary.totalProfit).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} ({Number(profitSummary.profitPercentage).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}%)
              </Typography>
            </Grid>
          </Grid>
        </Box>
      )}

      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5">TEFAS Funds</Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleClickOpen()}
        >
          Add New TEFAS Fund
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Fund Name</TableCell>
              <TableCell>Fund Code</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Current Price</TableCell>
              <TableCell>Purchase Price</TableCell>
              <TableCell>Total Value</TableCell>
              <TableCell>Total Profit</TableCell>
              <TableCell>Monthly Increase</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {stocks.map((stock) => {
              const profitPercentage = ((stock.currentPrice - stock.purchasePrice) / stock.purchasePrice) * 100;
              const profitColor = profitPercentage >= 0 ? 'success.main' : 'error.main';
              
              return (
                <TableRow 
                  key={stock.id}
                  onClick={() => handleRowClick(stock.assetCode)}
                  sx={{ cursor: 'pointer', '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.04)' } }}
                >
                  <TableCell>{stock.assetName}</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>{stock.assetCode}</TableCell>
                  <TableCell>{stock.quantity}</TableCell>
                  <TableCell>
                    <Tooltip title={stock.priceDate ? new Date(stock.priceDate).toLocaleString('tr-TR') : 'No date available'} arrow placement="top">
                      <span>₺{Number(stock.currentPrice).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 6 })}</span>
                    </Tooltip>
                  </TableCell>
                  <TableCell>₺{Number(stock.purchasePrice).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 6 })}</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>₺{Number(stock.currentPrice * stock.quantity).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</TableCell>
                  <TableCell sx={{ color: profitColor, fontWeight: 'bold' }}>
                    {profitPercentage.toFixed(2)}%
                  </TableCell>
                  <TableCell sx={{ color: stock.monthlyIncrease >= 0 ? 'success.main' : 'error.main', fontWeight: 'bold' }}>
                    {stock.monthlyIncrease ? `${stock.monthlyIncrease.toFixed(2)}%` : '-'}
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex' }}>
                      <IconButton 
                        size="small" 
                        onClick={(e) => { e.stopPropagation(); handleClickOpen(stock); }}
                        color="primary"
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton 
                        size="small" 
                        onClick={(e) => { e.stopPropagation(); handleDelete(stock); }}
                        color="error"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog
        open={detailOpen}
        onClose={handleDetailClose}
        maxWidth="md"
        fullWidth
      >
        <DialogContent>
          <FundDetail fundCode={selectedFundCode} />
        </DialogContent>
      </Dialog>
      <StockDialog
        open={open}
        handleClose={handleClose}
        stock={selectedStock}
        handleSave={handleSave}
      />
    </Box>
  );
};

export default Tefas;