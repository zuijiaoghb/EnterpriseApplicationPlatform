import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Login from './src/pages/Login';
import Dashboard from './src/pages/Dashboard';
import EquipmentList from './src/pages/EquipmentList';
import SystemSettings from './src/pages/SystemSettings';

const Stack = createNativeStackNavigator();

function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Login">
        <Stack.Screen name="Login" component={Login} options={{ title: '登录页' }} />
        <Stack.Screen name="Dashboard" component={Dashboard}  options={{ title: '仪表盘' }} />
        <Stack.Screen name="EquipmentList" component={EquipmentList} options={{ title: '设备管理' }} />
        <Stack.Screen name="SystemSettings" component={SystemSettings} options={{ title: '系统管理' }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;