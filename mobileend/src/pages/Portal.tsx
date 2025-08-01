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
  cnname?: string;
}

const Portal = () => {
  const navigation = useNavigation<PortalNavigationProp>();
  const [scale, setScale] = useState(1);
  
  interface MenuItem {
    key: string;
    label: string;
    icon: string;
  }

  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [user, setUser] = useState<User | null>(null);
  
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        // 先获取auth info
        const authResponse = await api.get('/auth/info');
        const username = authResponse.data.username;
        console.log('Auth Info Username:', username);

        // 再使用username调用新API获取用户详细信息
        const userResponse = await api.get(`/api/users/username/${username}`);
        setUser(userResponse.data);

        // 根据角色权限生成菜单
        const roles = authResponse.data.roles || [];
        const isAdmin = roles.some((role: string) => role === 'ADMIN' || role === 'ROLE_ADMIN');
        const newMenuItems = [];

        // 仪表盘菜单 - 仪表盘管理员或管理员可见
        if (isAdmin || roles.some((role: string) => role === 'ROLE_YBPGL')) {
          newMenuItems.push({ key: 'Dashboard', label: '仪表盘', icon: 'home' });
        }

        // 设备管理菜单 - 设备管理员或管理员可见
        if (isAdmin || roles.some((role: string) => role === 'ROLE_SBGL')) {
          newMenuItems.push({ key: 'EquipmentList', label: '设备管理', icon: 'list' });
        }

        // 库存管理菜单 - 仓库管理员或管理员可见
        if (isAdmin || roles.some((role: string) => role === 'ROLE_CKGLY')) {
          newMenuItems.push({ key: 'InventoryManagement', label: '库存管理', icon: 'inventory' });
        }

        // 系统管理菜单 - 仅管理员可见
        if (isAdmin) {
          newMenuItems.push({ key: 'SystemSettings', label: '系统管理', icon: 'settings' });
        }

        setMenuItems(newMenuItems);
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
          <Text style={styles.username}>{user?.cnname || user?.username || '用户'}</Text>
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
    flexDirection: 'column',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    padding: 20,
  },
  header: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  welcomeText: {
    color: 'rgba(255, 255, 255, 0.9)',
    fontSize: 18,
  },
  username: {
    color: '#ffffff',
    fontSize: 15,
    fontWeight: 'bold',
    marginTop: 4,
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 1, height: 1 },
    textShadowRadius: 2,
  },
  grid: {
    flex: 4,
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