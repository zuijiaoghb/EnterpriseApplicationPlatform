import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import MainLayout from './src/components/MainLayout';
import Login from './src/pages/Login';
import Portal from './src/pages/Portal';
import Dashboard from './src/pages/Dashboard';
import EquipmentList from './src/pages/EquipmentList';
import SystemSettings from './src/pages/SystemSettings';
import UserManagement from './src/pages/UserManagement';
import RoleManagement from './src/pages/RoleManagement';
import PermissionManagement from './src/pages/PermissionManagement';
import ClientManagement from './src/pages/ClientManagement';
import InventoryManagement from './src/pages/InventoryManagement';
import BarcodeManagement from './src/pages/BarcodeManagement';
import ScanInOut from './src/pages/ScanInOut';

import { RootStackParamList } from './src/navigation/types';

// 移除重复的 RootStackParamList 定义，使用统一的定义

const Stack = createNativeStackNavigator<RootStackParamList>();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Login">
        <Stack.Screen 
          name="Login" 
          component={Login} 
          options={{ headerShown: false }}
        />
        
        <Stack.Screen 
          name="Portal" 
          options={{ headerShown: false }}
        >
          {() => (
            <MainLayout>
              <Stack.Navigator initialRouteName="Portal">
                <Stack.Screen name="Portal" component={Portal} options={{ title: '工作门户' }} />
                <Stack.Screen name="Dashboard" component={Dashboard} options={{ title: '仪表盘' }} />
                <Stack.Screen name="EquipmentList" component={EquipmentList} options={{ title: '设备管理' }}/>
                <Stack.Screen name="SystemSettings" component={SystemSettings} options={{ title: '系统管理' }} />
                <Stack.Screen name="UserManagement" component={UserManagement} options={{ title: '用户管理' }} />
                <Stack.Screen name="RoleManagement" component={RoleManagement} options={{ title: '角色管理' }} />
                <Stack.Screen name="PermissionManagement" component={PermissionManagement} options={{ title: '权限管理' }} />
                <Stack.Screen name="ClientManagement" component={ClientManagement} options={{ title: '客户端管理' }} />
                <Stack.Screen name="InventoryManagement" component={InventoryManagement} options={{ title: '库存管理' }} />
                <Stack.Screen name="BarcodeManagement" component={BarcodeManagement} options={{ title: '条码管理' }} />
                <Stack.Screen name="ScanInOut" component={ScanInOut} options={{ title: '扫码出入库' }} />
              </Stack.Navigator>
            </MainLayout>
          )}
        </Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;