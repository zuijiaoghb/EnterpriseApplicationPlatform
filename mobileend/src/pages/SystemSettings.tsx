import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import api from '../api';
import Toast from 'react-native-toast-message';

const SystemSettings = () => {
  const [hasAdminRole, setHasAdminRole] = useState(false);
  const navigation = useNavigation();

  const baseItems = [
    { key: 'UserManagement', label: '用户管理', icon: 'person' },
    { key: 'RoleManagement', label: '角色管理', icon: 'group' },
    { key: 'PermissionManagement', label: '权限管理', icon: 'lock' },
    { key: 'ClientManagement', label: '客户端管理', icon: 'devices' }
  ];

  const [menuItems, setMenuItems] = useState(baseItems);

  useEffect(() => {
    checkUserRole();
  }, []);

  interface CheckRoleResponse {
    hasAdminRole: boolean;
  }

  const checkUserRole = async () => {
    try {
      const { data } = await api.get<CheckRoleResponse>('/auth/check-role');
      setHasAdminRole(data.hasAdminRole);
    } catch (error) {
      Toast.show({
        type: 'error',
        text1: '获取角色信息失败',
        text2: error instanceof Error ? error.message : '未知错误'
      });
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
    <View style={styles.container}>
      <View style={styles.grid}>
        {menuItems.map(item => (
          <TouchableOpacity
            key={item.key}
            style={styles.item}
            onPress={() => navigation.navigate(item.key as never)}
          >
            <MaterialIcons name={item.icon} size={48} color="#1890ff" />
            <Text style={styles.label}>{item.label}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    width: '100%',
  },
  item: {
    width: '40%',
    aspectRatio: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f0f0f0',
    borderRadius: 10,
    margin: 10,
    elevation: 3,
  },
  label: {
    marginTop: 10,
    fontSize: 16,
  },
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