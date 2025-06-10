import React, { useEffect, useState } from 'react';
import { View, TouchableOpacity, StyleSheet, Text } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import api from '../api';

interface User {
  username: string;
  roles?: string[];
}

const Portal = () => {
  const navigation = useNavigation();
  
  const baseItems = [
    { key: 'Dashboard', label: '仪表盘', icon: 'home' },
    { key: 'EquipmentList', label: '设备管理', icon: 'list' },
    { key: 'InventoryManagement', label: '库存管理', icon: 'inventory' },
  ];
  
  const [menuItems, setMenuItems] = useState(baseItems);
  
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await api.get<User>('/auth/info');
        if (response.data.roles?.some((role: string) => role === 'ADMIN' || role === 'ROLE_ADMIN')) {
          setMenuItems([...baseItems, { key: 'SystemSettings', label: '系统管理', icon: 'settings' }]);
        }
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    };
    
    fetchUserInfo();
  }, []);

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
});

export default Portal;