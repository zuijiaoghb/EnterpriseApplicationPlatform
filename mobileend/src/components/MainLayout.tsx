import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import api from '../api';
import AsyncStorage from '@react-native-async-storage/async-storage';

const MainLayout = ({ children }: { children: React.ReactNode }) => {
  const navigation = useNavigation();
  interface User {
    username: string;
    roles?: string[];
  }

  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [menuItems, setMenuItems] = useState<{key: string; label: string; icon: string;}[]>([]);


  const handleLogout = () => {
    try {
      // 清除token和用户信息
      setCurrentUser(null);
      setMenuItems([]);
      // 使用AsyncStorage清除token
      AsyncStorage.removeItem('token');
      // 跳转到登录页面
      navigation.navigate('Login' as never);
    } catch (error) {
      console.error('退出登录失败', error);
    }
  };

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await api.get<User>('/auth/info');
        setCurrentUser(response.data);

        const baseItems = [
          { key: 'Dashboard', label: '仪表盘', icon: 'home' },
          { key: 'EquipmentList', label: '设备管理', icon: 'list' }
        ];

        if (response.data.roles?.some(role => role === 'ADMIN' || role === 'ROLE_ADMIN')) {
          baseItems.push({ key: 'SystemSettings', label: '系统管理', icon: 'settings' });
        }

        setMenuItems(baseItems);
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    };

    fetchUserInfo();
  }, []);

  return (
    <View style={{...styles.container, paddingBottom: 50 }}>
      <View style={styles.content}>
        {children}
      </View>
      
      <View style={styles.bottomTab}>
        <TouchableOpacity 
          style={styles.portalButton}
          onPress={() => navigation.navigate('Portal' as never)}
        >
          <MaterialIcons name="apps" size={28} color="white" />
          <Text style={styles.portalText}>工作门户</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Text style={styles.logoutText}>退出</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flex: 1,
    padding: 15,
    paddingBottom: 60,
  },
  bottomTab: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
    backgroundColor: '#001529',
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
  },
  portalButton: {
    alignItems: 'center',
    padding: 10,
  },
  portalText: {
    color: 'white',
    fontSize: 12,
    marginTop: 5,
  },
  logoutButton: {
    padding: 10,
    backgroundColor: '#ff4444',
    borderRadius: 5,
    margin: 10,
  },
  logoutText: {
    color: 'white',
    textAlign: 'center',
  },
});

export default MainLayout;