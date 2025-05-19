import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Button, IconButton } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import StockDialog from '../shared/StockDialog';

const Tefas = () => {
  const [open, setOpen] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [stocks, setStocks] = useState([]);

  useEffect(() => {
    fetchStocks();
  }, []);

  const fetchStocks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/assets/TEFAS');
      setStocks(response.data);
    } catch (error) {
      console.error('Error fetching TEFAS funds:', error);
    }
  };

  const handleClickOpen = (stock = null) => {
    setSelectedStock(stock);
    setOpen(true);
  };

  const handleClose = () => {
    setSelectedStock(null);
    setOpen(false);
  };

  const handleSave = async (stockData) => {
    try {
      const formattedStock = {
        id: stockData.symbol,
        assetName: stockData.symbol,
        assetCode: stockData.symbol,
        quantity: parseFloat(stockData.quantity),
        currentPrice: parseFloat(stockData.price),
        purchasePrice: parseFloat(stockData.purchasePrice),
        purchaseDate: new Date().toISOString().split('T')[0],
        type: 'TEFAS'
      };

      if (selectedStock) {
        await axios.put(`http://localhost:8080/api/assets/${selectedStock.id}`, formattedStock);
      } else {
        await axios.post('http://localhost:8080/api/assets', formattedStock);
      }
      fetchStocks();
      handleClose();
    } catch (error) {
      console.error('Error saving TEFAS fund:', error);
    }
  };

  const handleDelete = async (stockToDelete) => {
    try {
      await axios.delete(`http://localhost:8080/api/assets/${stockToDelete.id}`);
      fetchStocks();
    } catch (error) {
      console.error('Error deleting TEFAS fund:', error);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
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
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {stocks.map((stock) => {
              const profitPercentage = ((stock.currentPrice - stock.purchasePrice) / stock.purchasePrice) * 100;
              const profitColor = profitPercentage >= 0 ? 'success.main' : 'error.main';
              
              return (
                <TableRow key={stock.id}>
                  <TableCell>{stock.assetName}</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>{stock.assetCode}</TableCell>
                  <TableCell>{stock.quantity}</TableCell>
                  <TableCell>₺{stock.currentPrice}</TableCell>
                  <TableCell>₺{stock.purchasePrice}</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>₺{(stock.currentPrice * stock.quantity).toFixed(2)}</TableCell>
                  <TableCell sx={{ color: profitColor, fontWeight: 'bold' }}>
                    {profitPercentage.toFixed(2)}%
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex' }}>
                      <IconButton 
                        size="small" 
                        onClick={() => handleClickOpen(stock)}
                        color="primary"
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton 
                        size="small" 
                        onClick={() => handleDelete(stock)}
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