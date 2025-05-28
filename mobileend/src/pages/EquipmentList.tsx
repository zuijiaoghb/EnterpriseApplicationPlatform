import React, { useState, useEffect } from 'react';
import { View, Text, Button, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import api from '../api';
import EquipmentForm from './EquipmentForm';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface Equipment {
  id: number;
  name: string;
  model: string;
  status: string;
  qrCode: string;
  lastMaintenance: string;
}

const EquipmentList = () => {
  const [equipments, setEquipments] = useState<Equipment[]>([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<Equipment | null>(null);

  useEffect(() => {
    const checkToken = async () => {
      const token = await AsyncStorage.getItem('token');
      if (!token) {
        // 改为使用React Navigation的导航
        return;
      }
      fetchEquipments();
    };
    checkToken();
  }, []);

  const fetchEquipments = async () => {
    try {
      console.log('正在请求设备数据...');
      const { data } = await api.get<Equipment[]>('/api/equipments');
      console.log('获取到的数据:', data);
      setEquipments(data);
    } catch (error) {
      console.error('API请求失败:', error);
      // 使用Toast或其他React Native提示组件
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await api.delete(`/api/equipments/${id}`);
      // 使用Toast或其他React Native提示组件
      fetchEquipments();
    } catch (error) {
      // 使用Toast或其他React Native提示组件
    }
  };

  const renderItem = ({ item }: { item: Equipment }) => (
    <View style={styles.itemContainer}>
      <Text>ID: {item.id}</Text>
      <Text>名称: {item.name}</Text>
      <Text>型号: {item.model}</Text>
      <Text>状态: {item.status}</Text>
      <Text>二维码: {item.qrCode}</Text>
      <Text>最后维护: {item.lastMaintenance}</Text>
      <View style={styles.buttonContainer}>
        <Button 
          title="编辑" 
          onPress={() => { setCurrent(item); setVisible(true); }} 
        />
        <Button 
          title="删除" 
          onPress={() => handleDelete(item.id)}
          color="red"
        />
      </View>
    </View>
  );

  return (
    <View style={styles.container}>
      <Button 
        title="新增设备" 
        onPress={() => { setCurrent(null); setVisible(true); }}
      />
      <ScrollView style={{ height: 150 }}>
        {equipments.map((item) => (
          <View style={styles.itemContainer} key={item.id.toString()}>
            <Text>ID: {item.id}</Text>
            <Text>名称: {item.name}</Text>
            <Text>型号: {item.model}</Text>
            <Text>状态: {item.status}</Text>
            <Text>二维码: {item.qrCode}</Text>
            <Text>最后维护: {item.lastMaintenance}</Text>
            <View style={styles.buttonContainer}>
              <Button 
                title="编辑" 
                onPress={() => { setCurrent(item); setVisible(true); }} 
              />
              <Button 
                title="删除" 
                onPress={() => handleDelete(item.id)}
                color="red"
              />
            </View>
          </View>
        ))}
        <View style={{ height: 25 }} />
      </ScrollView>
      <EquipmentForm 
        visible={visible}
        onClose={() => setVisible(false)}
        onSuccess={fetchEquipments}
        current={current}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  itemContainer: {
    padding: 16,
    marginBottom: 16,
    backgroundColor: '#f9f9f9',
    borderRadius: 8,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 8,
  },
});

export default EquipmentList;