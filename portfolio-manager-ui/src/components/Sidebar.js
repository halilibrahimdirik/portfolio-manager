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
  Notifications as NotificationsIcon,
  Person as ProfileIcon,
  Login as SignInIcon,
  PersonAdd as SignUpIcon,
  ShowChart as NasdaqIcon,
  Assessment as TefasIcon,
  TrendingUp as BistIcon
} from '@mui/icons-material';

const drawerWidth = 244;

const menuItems = [
  { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard' },
  { text: 'Tefas', icon: <TefasIcon />, path: '/tefas' },
  { text: 'Bist', icon: <BistIcon />, path: '/bist' },
  { text: 'Nasdaq', icon: <NasdaqIcon />, path: '/nasdaq' },
  { text: 'Notifications', icon: <NotificationsIcon />, path: '/notifications' },
  { text: 'Profile', icon: <ProfileIcon />, path: '/profile' },
  { text: 'Sign In', icon: <SignInIcon />, path: '/signin' },
  { text: 'Sign Up', icon: <SignUpIcon />, path: '/signup' }
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