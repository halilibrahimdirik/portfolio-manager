import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography,
  Box,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  TableChart as TablesIcon,
  Receipt as BillingIcon,
  Notifications as NotificationsIcon,
  Person as ProfileIcon,
  Login as SignInIcon,
  PersonAdd as SignUpIcon,
  AccountBalance as ETFsIcon,
  ShowChart as NasdaqIcon
} from '@mui/icons-material';

const drawerWidth = 240;

// Remove this duplicate import
// import { ShowChart as NasdaqIcon } from '@mui/icons-material';

const menuItems = [
  { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard' },
  { text: 'Stocks', icon: <TablesIcon />, path: '/stocks' },
  { text: 'ETFs', icon: <ETFsIcon />, path: '/etfs' },
  { text: 'NASDAQ', icon: <NasdaqIcon />, path: '/nasdaq' },
  { text: 'Billing', icon: <BillingIcon />, path: '/billing' },
  { text: 'Notifications', icon: <NotificationsIcon />, path: '/notifications' },
  { text: 'Profile', icon: <ProfileIcon />, path: '/profile' },
  { text: 'Sign In', icon: <SignInIcon />, path: '/signin' },
  { text: 'Sign Up', icon: <SignUpIcon />, path: '/signup' },
];

const Sidebar = () => {
  const navigate = useNavigate();

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
          backgroundColor: '#202940',
          color: 'white',
        },
      }}
    >
      <Box sx={{ p: 2 }}>
        <Typography variant="h6" component="div" sx={{ color: 'white' }}>
          Material Dashboard 2
        </Typography>
      </Box>
      <List>
        {menuItems.map((item) => (
          <ListItem
            button
            key={item.text}
            onClick={() => navigate(item.path)}
            sx={{
              '&:hover': {
                backgroundColor: 'rgba(255, 255, 255, 0.1)',
              },
            }}
          >
            <ListItemIcon sx={{ color: 'white' }}>
              {item.icon}
            </ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
};

export default Sidebar;