import React, { useState } from 'react';
import { View, TouchableOpacity, Text, StyleSheet, ImageBackground } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../navigation/types';

type InventoryManagementNavigationProp = StackNavigationProp<RootStackParamList, 'InventoryManagement'>;

export default function InventoryManagement() {
  const navigation = useNavigation<InventoryManagementNavigationProp>();
  const [scale, setScale] = useState(1);

  return (
    <ImageBackground
      source={require('../../assets/login-bg.png')}
      style={styles.backgroundImage}
    >
      <View style={styles.overlay}>
        <View style={styles.header}>
          <Text style={styles.title}>库存管理</Text>
          <Text style={styles.subtitle}>高效管理库存流程</Text>
        </View>

        <View style={styles.menuContainer}>
          <TouchableOpacity 
            style={[styles.menuCard, { transform: [{ scale }] }]}
            onPress={() => navigation.navigate('BarcodeManagement' as never)}
            onPressIn={() => setScale(0.95)}
            onPressOut={() => setScale(1)}
          >
            <View style={styles.menuIcon}>
              <Text style={styles.iconText}>📊</Text>
            </View>
            <Text style={styles.menuTitle}>条码管理</Text>
            <Text style={styles.menuDescription}>创建和管理商品条码</Text>
          </TouchableOpacity>

          <TouchableOpacity 
            style={[styles.menuCard, { transform: [{ scale }] }]}
            onPress={() => navigation.navigate('ScanInOut' as never)}
            onPressIn={() => setScale(0.95)}
            onPressOut={() => setScale(1)}
          >
            <View style={styles.menuIcon}>
              <Text style={styles.iconText}>📱</Text>
            </View>
            <Text style={styles.menuTitle}>扫码出入库</Text>
            <Text style={styles.menuDescription}>快速处理库存变动</Text>
          </TouchableOpacity>
        </View>
      </View>
    </ImageBackground>
  );
}

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
    alignItems: 'center',
    marginVertical: 40,
  },
  title: {
    color: '#ffffff',
    fontSize: 28,
    fontWeight: 'bold',
    marginBottom: 8,
    textShadowColor: 'rgba(0, 0, 0, 0.3)',
    textShadowOffset: { width: 2, height: 2 },
    textShadowRadius: 4,
  },
  subtitle: {
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: 16,
  },
  menuContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 20,
  },
  menuCard: {
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderRadius: 12,
    padding: 25,
    marginVertical: 15,
    width: '90%',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 10,
    elevation: 8,
    transform: [{ scale: 1 }],
  },
  menuIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#1890ff',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 15,
  },
  iconText: {
    fontSize: 24,
  },
  menuTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333333',
    marginBottom: 5,
  },
  menuDescription: {
    fontSize: 14,
    color: '#666666',
    textAlign: 'center',
  },
})