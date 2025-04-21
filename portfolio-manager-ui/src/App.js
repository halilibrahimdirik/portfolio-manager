import React from 'react';
import { Box, CssBaseline } from '@mui/material';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard/Dashboard';
import Tables from './components/Tables/Tables';
import Sidebar from './components/Sidebar';
import ETFs from './components/ETFs/ETFs';
import NASDAQ from './components/NASDAQ/NASDAQ';

function App() {
  return (
    <Router>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Sidebar />
        <Box component="main" sx={{ flexGrow: 1 }}>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/stocks" element={<Tables />} />
            <Route path="/etfs" element={<ETFs />} />
            <Route path="/nasdaq" element={<NASDAQ />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}

export default App;
