import React from 'react';
import { View, TouchableOpacity, StyleSheet, Text } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';

const Portal = () => {
  const navigation = useNavigation();
  
  const menuItems = [
    { key: 'Dashboard', label: '仪表盘', icon: 'home' },
    { key: 'EquipmentList', label: '设备管理', icon: 'list' },
    { key: 'SystemSettings', label: '系统管理', icon: 'settings' },
  ];

  return (
    <View style={styles.container}>
      <View style={styles.grid}>
        {menuItems.map(item => (
          <TouchableOpacity
            key={item.key}
            style={styles.item}
            onPress={() => navigation.navigate(item.key as never)}
          >
            <Ionicons name={item.icon as keyof typeof Ionicons.glyphMap} size={48} color="#1890ff" />
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