import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Modal, Pressable } from 'react-native';
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
  const [showLogoutModal, setShowLogoutModal] = useState(false);


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
          { key: 'EquipmentList', label: '设备管理', icon: 'list' },
          { key: 'InventoryManagement', label: '库存管理', icon: 'inventory' }
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
          onPress={() => navigation.navigate('PortalContainer' as never)}
        >
          <MaterialIcons name="apps" size={28} color="white" />
          <Text style={styles.portalText}>工作门户</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.logoutButton} onPress={() => setShowLogoutModal(true)}>
          <Text style={styles.logoutText}>退出</Text>
        </TouchableOpacity>
      </View>
      <Modal
        visible={showLogoutModal}
        transparent={true}
        animationType="fade"
        onRequestClose={() => setShowLogoutModal(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>确认退出</Text>
            <Text style={styles.modalMessage}>您确定要退出当前账号吗？</Text>
            <View style={styles.modalButtons}>
              <Pressable
                style={styles.cancelButton}
                onPress={() => setShowLogoutModal(false)}
              >
                <Text style={styles.cancelButtonText}>取消</Text>
              </Pressable>
              <Pressable
                style={styles.confirmButton}
                onPress={() => {
                  handleLogout();
                  setShowLogoutModal(false);
                }}
              >
                <Text style={styles.confirmButtonText}>确定</Text>
              </Pressable>
            </View>
          </View>
        </View>
      </Modal>
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
  modalOverlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)'
  },
  modalContainer: {
    width: '80%',
    backgroundColor: 'white',
    borderRadius: 12,
    padding: 24,
    elevation: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 8
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
    textAlign: 'center',
    color: '#333'
  },
  modalMessage: {
    fontSize: 16,
    marginBottom: 24,
    textAlign: 'center',
    color: '#666',
    lineHeight: 24
  },
  modalButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 16
  },
  cancelButton: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 8,
    backgroundColor: '#f5f5f5',
    marginRight: 12,
    alignItems: 'center'
  },
  confirmButton: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 8,
    backgroundColor: '#1890ff',
    marginLeft: 12,
    alignItems: 'center'
  },
  cancelButtonText: {
    color: '#333',
    fontSize: 16,
    fontWeight: '500'
  },
  confirmButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '500'
  },
});

export default MainLayout;