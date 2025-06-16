import React, { useEffect, useState } from 'react';
import { View, TouchableOpacity, StyleSheet, Text, ImageBackground } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { RootStackParamList } from '../navigation/types';
import api from '../api';

type PortalNavigationProp = StackNavigationProp<RootStackParamList, 'Portal'>;

interface User {
  username: string;
  roles?: string[];
  nickname?: string;
}

const Portal = () => {
  const navigation = useNavigation<PortalNavigationProp>();
  const [scale, setScale] = useState(1);
  
  const baseItems = [
    { key: 'Dashboard', label: '仪表盘', icon: 'home' },
    { key: 'EquipmentList', label: '设备管理', icon: 'list' },
    { key: 'InventoryManagement', label: '库存管理', icon: 'inventory' },
  ];
  
  const [menuItems, setMenuItems] = useState(baseItems);
  const [user, setUser] = useState<User | null>(null);
  
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await api.get<User>('/auth/info');
        setUser(response.data);
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
    <ImageBackground
      source={require('../../assets/login-bg.png')}
      style={styles.backgroundImage}
    >
      <View style={styles.overlay}>
        <View style={styles.header}>
          <Text style={styles.welcomeText}>欢迎回来，</Text>
          <Text style={styles.username}>{user?.nickname || user?.username || '用户'}</Text>
        </View>

        <View style={styles.grid}>
          {menuItems.map(item => (
            <TouchableOpacity
              key={item.key}
              style={[styles.item, { transform: [{ scale }] }]}
              onPress={() => navigation.navigate(item.key as never)}
              onPressIn={() => setScale(0.95)}
              onPressOut={() => setScale(1)}
            >
              <View style={styles.iconContainer}>
                <MaterialIcons name={item.icon} size={32} color="#ffffff" />
              </View>
              <Text style={styles.label}>{item.label}</Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    width: '100%',
    height: '100%',
    resizeMode: 'cover',
  },
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    padding: 20,
  },
  header: {
    marginTop: 60,
    marginBottom: 40,
    alignItems: 'center',
  },
  welcomeText: {
    color: 'rgba(255, 255, 255, 0.9)',
    fontSize: 18,
  },
  username: {
    color: '#ffffff',
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 4,
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 1, height: 1 },
    textShadowRadius: 2,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    paddingHorizontal: 10,
    width: '100%',
  },
  item: {
    width: '47%',
    aspectRatio: 1.1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderRadius: 16,
    marginVertical: 12,
    padding: 15,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 10,
    elevation: 8,
  },
  iconContainer: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#1890ff',
    justifyContent: 'center',
    alignItems: 'center',
  },
  label: {
    marginTop: 15,
    fontSize: 16,
    fontWeight: '500',
    color: '#333333',
  },
});

export default Portal;