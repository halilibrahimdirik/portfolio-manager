import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button
} from '@mui/material';

const StockDialog = ({ open, handleClose, stock, handleSave }) => {
  const [formData, setFormData] = React.useState({
    symbol: '',
    quantity: '',
    price: '',
    purchasePrice: ''  // Add purchase price field
  });

  React.useEffect(() => {
    if (stock) {
      setFormData({
        symbol: stock.name || '',
        quantity: stock.quantity || '',
        price: stock.currentPrice || '',
        purchasePrice: stock.purchasePrice || ''  // Initialize purchase price
      });
    } else {
      setFormData({
        symbol: '',
        quantity: '',
        price: '',
        purchasePrice: ''  // Reset purchase price
      });
    }
  }, [stock]);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  };

  const handleSubmit = () => {
    handleSave(formData);
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{stock ? 'Edit Stock' : 'Add New Stock'}</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          name="symbol"
          label="Symbol"
          type="text"
          fullWidth
          variant="standard"
          value={formData.symbol}
          onChange={handleChange}
          disabled={!!stock}
        />
        <TextField
          margin="dense"
          name="quantity"
          label="Quantity"
          type="number"
          fullWidth
          variant="standard"
          value={formData.quantity}
          onChange={handleChange}
        />
        <TextField
          margin="dense"
          name="price"
          label="Price"
          type="number"
          fullWidth
          variant="standard"
          value={formData.price}
          onChange={handleChange}
        />
        <TextField
          margin="dense"
          name="purchasePrice"
          label="Purchase Price"
          type="number"
          fullWidth
          variant="standard"
          value={formData.purchasePrice}
          onChange={handleChange}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleSubmit}>Save</Button>
      </DialogActions>
    </Dialog>
  );
};

export default StockDialog;