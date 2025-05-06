import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import UserManagement from './UserManagement';
import RoleManagement from './RoleManagement';
import PermissionManagement from './PermissionManagement';
import ClientManagement from './ClientManagement';
import api from '../api';

const Tab = createMaterialTopTabNavigator();

const SystemSettings = () => {
  const [hasAdminRole, setHasAdminRole] = useState(false);

  useEffect(() => {
    checkUserRole();
  }, []);

  const checkUserRole = async () => {
    try {
      const { data } = await api.get('/auth/check-role');
      setHasAdminRole(data.hasAdminRole);
    } catch (error) {
      // 使用Toast或其他React Native提示组件
    }
  };

  if (!hasAdminRole) {
    return (
      <View style={styles.noPermissionContainer}>
        <Text style={styles.noPermissionText}>您没有权限访问系统管理功能</Text>
        <Text style={styles.noPermissionSubText}>请联系系统管理员获取权限</Text>
      </View>
    );
  }

  return (
    <Tab.Navigator
      tabBarOptions={{
        scrollEnabled: true,
        tabStyle: { width: 120 },
        labelStyle: { fontSize: 12 },
      }}
    >
      <Tab.Screen name="用户管理" component={UserManagement} />
      <Tab.Screen name="角色管理" component={RoleManagement} />
      <Tab.Screen name="权限配置" component={PermissionManagement} />
      <Tab.Screen name="客户端管理" component={ClientManagement} />
    </Tab.Navigator>
  );
};

const styles = StyleSheet.create({
  noPermissionContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  noPermissionText: {
    fontSize: 18,
    marginBottom: 10,
    fontWeight: 'bold',
  },
  noPermissionSubText: {
    fontSize: 14,
    color: '#666',
  },
});

export default SystemSettings;