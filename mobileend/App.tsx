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

type RootStackParamList = {
  Login: undefined;
  Dashboard: undefined;
  EquipmentList: undefined;
  SystemSettings: undefined;
  Main: undefined;
  Portal: undefined;
  UserManagement: undefined;
};

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
          name="Main" 
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
              </Stack.Navigator>
            </MainLayout>
          )}
        </Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;