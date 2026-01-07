import React from 'react';
import { Box, CssBaseline } from '@mui/material';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard/Dashboard';
import Sidebar from './components/Sidebar';
import Nasdaq from './components/Nasdaq/Nasdaq';
import Tefas from './components/Tefas/Tefas';
import Bist from './components/Bist/Bist';

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
            <Route path="/nasdaq" element={<Nasdaq />} />
            <Route path="/bist" element={<Bist />} />
            <Route path="/tefas" element={<Tefas source="YAPIKREDI" />} />
            <Route path="/tefas-midas" element={<Tefas source="MIDAS" />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}

export default App;
