import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import MainLayout from './src/components/MainLayout';
import Login from './src/pages/Login';
import Portal from './src/pages/Portal';
import Dashboard from './src/pages/Dashboard';
import EquipmentList from './src/pages/EquipmentList';
import SystemSettings from './src/pages/SystemSettings';

type RootStackParamList = {
  Login: undefined;
  Dashboard: undefined;
  EquipmentList: undefined;
  SystemSettings: undefined;
  Main: undefined;
  Portal: undefined;
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
              <Stack.Navigator>
                <Stack.Screen name="Portal" component={Portal} />
                <Stack.Screen name="Dashboard" component={Dashboard} />
                <Stack.Screen name="EquipmentList" component={EquipmentList} />
                <Stack.Screen name="SystemSettings" component={SystemSettings} />
              </Stack.Navigator>
            </MainLayout>
          )}
        </Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;