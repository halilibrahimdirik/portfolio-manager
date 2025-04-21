import React, { useState } from 'react';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Button, IconButton } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import StockDialog from '../Tables/StockDialog';

const NASDAQ = () => {
  const [open, setOpen] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [stocks, setStocks] = useState([
    { 
      symbol: 'AAPL', 
      quantity: '100', 
      price: '$180.25', 
      dailyChange: '+2.5%',
      monthlyChange: '+8.3%',
      value: '$18,025'
    },
    { 
      symbol: 'MSFT', 
      quantity: '50', 
      price: '$375.80', 
      dailyChange: '-0.8%',
      monthlyChange: '+5.2%',
      value: '$18,790'
    },
  ]);

  // ... same handlers as ETFs component ...
  const handleClickOpen = (stock = null) => {
    setSelectedStock(stock);
    setOpen(true);
  };

  const handleClose = () => {
    setSelectedStock(null);
    setOpen(false);
  };

  const handleSave = (stockData) => {
    if (selectedStock) {
      setStocks(stocks.map(stock => 
        stock.symbol === selectedStock.symbol ? { ...stock, ...stockData } : stock
      ));
    } else {
      setStocks([...stocks, {
        ...stockData,
        dailyChange: '+0.0%',
        monthlyChange: '+0.0%',
        value: `$${(parseFloat(stockData.price) * parseInt(stockData.quantity)).toFixed(2)}`
      }]);
    }
  };

  const handleDelete = (stockToDelete) => {
    setStocks(stocks.filter(stock => stock.symbol !== stockToDelete.symbol));
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5">NASDAQ Stocks</Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleClickOpen()}
        >
          Add New NASDAQ Stock
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Stock</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Price</TableCell>
              <TableCell>Daily Change</TableCell>
              <TableCell>Monthly Change</TableCell>
              <TableCell>Total Value</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {stocks.map((stock) => (
              <TableRow key={stock.symbol}>
                <TableCell>{stock.symbol}</TableCell>
                <TableCell>{stock.quantity}</TableCell>
                <TableCell>{stock.price}</TableCell>
                <TableCell>
                  <Box
                    sx={{
                      backgroundColor: stock.dailyChange.startsWith('+') ? '#4CAF50' : '#FF5252',
                      color: 'white',
                      borderRadius: '4px',
                      padding: '4px 8px',
                      display: 'inline-block'
                    }}
                  >
                    {stock.dailyChange}
                  </Box>
                </TableCell>
                <TableCell>
                  <Box
                    sx={{
                      backgroundColor: stock.monthlyChange.startsWith('+') ? '#4CAF50' : '#FF5252',
                      color: 'white',
                      borderRadius: '4px',
                      padding: '4px 8px',
                      display: 'inline-block'
                    }}
                  >
                    {stock.monthlyChange}
                  </Box>
                </TableCell>
                <TableCell>{stock.value}</TableCell>
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
            ))}
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

export default NASDAQ;