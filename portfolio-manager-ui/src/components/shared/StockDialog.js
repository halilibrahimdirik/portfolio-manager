import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material';

const StockDialog = ({ open, handleClose, stock, handleSave }) => {
  const [formData, setFormData] = useState({
    symbol: '',
    quantity: '',
    price: '',
    purchasePrice: ''
  });

  useEffect(() => {
    if (stock) {
      setFormData({
        symbol: stock.assetCode || '',
        quantity: stock.quantity?.toString() || '',
        price: stock.currentPrice?.toString() || '',
        purchasePrice: stock.purchasePrice?.toString() || ''
      });
    } else {
      setFormData({
        symbol: '',
        quantity: '',
        price: '',
        purchasePrice: ''
      });
    }
  }, [stock]);

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{stock ? 'Edit TEFAS Fund' : 'Add New TEFAS Fund'}</DialogTitle>
      <DialogContent>
        <TextField
          margin="dense"
          label="Fund Code"
          type="text"
          fullWidth
          value={formData.symbol}
          onChange={(e) => setFormData({ ...formData, symbol: e.target.value })}
          disabled={!!stock}
        />
        <TextField
          margin="dense"
          label="Quantity"
          type="number"
          fullWidth
          value={formData.quantity}
          onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
          inputProps={{ step: "any" }}
        />
        <TextField
          margin="dense"
          label="Current Price"
          type="number"
          fullWidth
          value={formData.price}
          onChange={(e) => setFormData({ ...formData, price: e.target.value })}
          inputProps={{ 
            step: "0.00001",
            style: { textAlign: 'right' }
          }}
        />
        <TextField
          margin="dense"
          label="Purchase Price"
          type="number"
          fullWidth
          value={formData.purchasePrice}
          onChange={(e) => setFormData({ ...formData, purchasePrice: e.target.value })}
          inputProps={{ 
            step: "0.00001",
            style: { textAlign: 'right' }
          }}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={() => handleSave(formData)}>Save</Button>
      </DialogActions>
    </Dialog>
  );
};

export default StockDialog;