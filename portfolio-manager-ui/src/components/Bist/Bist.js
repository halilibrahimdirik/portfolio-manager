import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Button, IconButton, Tooltip } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import StockDialog from '../shared/StockDialog';

const Bist = () => {
  const [open, setOpen] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [stocks, setStocks] = useState([]);

  useEffect(() => {
    fetchStocks();
  }, []);

  const fetchStocks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/assets/BIST');
      setStocks(response.data);
    } catch (error) {
      console.error('Error fetching BIST stocks:', error);
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
        name: stockData.symbol,
        quantity: parseFloat(stockData.quantity),
        currentPrice: parseFloat(stockData.price),
        purchasePrice: parseFloat(stockData.price),
        purchaseDate: new Date().toISOString().split('T')[0],
        type: 'BIST'
      };

      if (selectedStock) {
        await axios.put(`http://localhost:8080/api/assets/${selectedStock.id}`, formattedStock);
      } else {
        await axios.post('http://localhost:8080/api/assets', formattedStock);
      }
      fetchStocks();
      handleClose();
    } catch (error) {
      console.error('Error saving BIST stock:', error);
    }
  };

  const handleDelete = async (stockToDelete) => {
    try {
      await axios.delete(`http://localhost:8080/api/assets/${stockToDelete.id}`);
      fetchStocks();
    } catch (error) {
      console.error('Error deleting BIST stock:', error);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5">BIST Stocks</Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleClickOpen()}
        >
          Add New BIST Stock
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Symbol</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Current Price</TableCell>
              <TableCell>Purchase Price</TableCell>
              <TableCell>Total Value</TableCell>
              <TableCell>Purchase Date</TableCell>
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
                  <TableCell>{stock.name}</TableCell>
                  <TableCell>{stock.quantity}</TableCell>
                  <TableCell>
                    <Tooltip title={stock.priceDate ? new Date(stock.priceDate).toLocaleString('tr-TR') : 'No date available'} arrow placement="top">
                      <span>₺{Number(stock.currentPrice).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 6 })}</span>
                    </Tooltip>
                  </TableCell>
                  <TableCell>₺{Number(stock.purchasePrice).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</TableCell>
                  <TableCell>₺{Number(stock.currentPrice * stock.quantity).toLocaleString('tr-TR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</TableCell>
                  <TableCell>{new Date(stock.purchaseDate).toLocaleDateString()}</TableCell>
                  <TableCell sx={{ color: profitColor }}>
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

export default Bist;