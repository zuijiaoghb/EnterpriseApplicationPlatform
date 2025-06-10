import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator } from 'react-native';
import api from '../api';

interface BarcodeItem {
  id: number;
  code: string;
  name: string;
  status: string;
}

export default function BarcodeManagement() {
  const [barcodes, setBarcodes] = useState<BarcodeItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  useEffect(() => {
    fetchBarcodes();
  }, [page, size]);

  const fetchBarcodes = async () => {
    setLoading(true);
    try {
      const response = await api.get('/api/barcodes', {
        params: { page, size }
      });
      setBarcodes(response.data.content);
    } catch (error) {
      console.error('获取条码数据失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const renderItem = ({ item }: { item: BarcodeItem }) => (
    <View style={styles.item}>
      <Text>ID: {item.id}</Text>
      <Text>条码: {item.code}</Text>
      <Text>名称: {item.name}</Text>
      <Text>状态: {item.status}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      {loading ? (
        <ActivityIndicator size="large" />
      ) : (
        <FlatList
          data={barcodes}
          renderItem={renderItem}
          keyExtractor={item => item.id.toString()}
          onEndReached={() => setPage(prev => prev + 1)}
          onEndReachedThreshold={0.5}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
  },
  item: {
    padding: 15,
    marginVertical: 5,
    backgroundColor: '#f0f0f0',
    borderRadius: 5,
  },
});