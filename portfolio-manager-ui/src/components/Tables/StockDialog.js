import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box
} from '@mui/material';

const StockDialog = ({ open, handleClose, stock, handleSave }) => {
  const [formData, setFormData] = React.useState({
    symbol: '',
    quantity: '',
    price: '',
    ...stock
  });

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  };

  const onSubmit = () => {
    handleSave(formData);
    handleClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>{stock ? 'Edit Stock' : 'Add New Stock'}</DialogTitle>
      <DialogContent>
        <Box sx={{ pt: 2 }}>
          <TextField
            fullWidth
            label="Stock Symbol"
            name="symbol"
            value={formData.symbol}
            onChange={handleChange}
            margin="normal"
          />
          <TextField
            fullWidth
            label="Quantity"
            name="quantity"
            type="number"
            value={formData.quantity}
            onChange={handleChange}
            margin="normal"
          />
          <TextField
            fullWidth
            label="Price"
            name="price"
            type="number"
            value={formData.price}
            onChange={handleChange}
            margin="normal"
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={onSubmit} variant="contained" color="primary">
          {stock ? 'Save Changes' : 'Add Stock'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default StockDialog;