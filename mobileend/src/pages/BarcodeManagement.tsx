import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator } from 'react-native';
import api from '../api';

interface BarcodeItem {
  barCode: string;
  barCodeRule: string;
  cinvSN: string;
  cinvCode: string;
  cfree1: string;
  plot: string;
  cdefine22: string;
  cdefine32: string;
  cmaker: string;
  createDate: string;
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
      const response = await api.get('/api/hy-barcode-main', {
        params: { page, size }
      });
      console.log('API响应状态:', response.status);
      console.log('API响应数据结构:', JSON.stringify(response.data, null, 2));
      console.log('内容数组长度:', response.data.content?.length);
      
      if (!response.data.content || !Array.isArray(response.data.content)) {
        console.error('API返回数据格式错误，content不是数组');
        setBarcodes([]);
        return;
      }
      
      const formattedData = response.data.content.map((item: any, index: number) => {
        console.log(`第${index}条原始数据:`, JSON.stringify(item, null, 2));
        console.log(`条码${item.barCode}字段值:`, {
          cinvSN: item.cinvSN,
          cinvCode: item.cinvCode,
          cfree1: item.cfree1,
          plot: item.plot,
          cdefine22: item.cdefine22,
          cdefine32: item.cdefine32,
          cmaker: item.cmaker
        });
        
        return {
          barCode: item.barCode || '未知条码',
          barCodeRule: item.barCodeRule || '未知规则',
          cinvSN: item.cinvSN !== undefined ? item.cinvSN : '无序列号',
          cinvCode: item.cinvCode !== undefined ? item.cinvCode : '无产品编码',
          cfree1: item.cfree1 !== undefined ? item.cfree1 : '无尾标',
          plot: item.plot !== undefined ? item.plot : '无批号',
          cdefine22: item.cdefine22 !== undefined ? item.cdefine22 : '无生产订单号',
          cdefine32: item.cdefine32 !== undefined ? item.cdefine32 : '无销售部门',
          cmaker: item.cmaker !== undefined ? item.cmaker : '无制单人',
          createDate: item.createDate || '无制单时间'
        };
      });
      
      console.log('格式化后的数据:', JSON.stringify(formattedData, null, 2));
      setBarcodes(formattedData);
    } catch (error) {
      console.error('获取条码数据失败:', error);
      setBarcodes([{
        barCode: '加载失败', 
        barCodeRule: '', 
        cinvSN: '', 
        cinvCode: '', 
        cfree1: '', 
        plot: '', 
        cdefine22: '', 
        cdefine32: '', 
        cmaker: '', 
        createDate: ''
      }]);
    } finally {
      setLoading(false);
    }
  };

  const renderItem = ({ item }: { item: BarcodeItem }) => (
    <View style={styles.item}>
      <Text>条码编号: {item.barCode}</Text>
      <Text>条码规则: {item.barCodeRule}</Text>
      <Text>序列号: {item.cinvSN}</Text>
      <Text>产品编码: {item.cinvCode}</Text>
      <Text>尾标: {item.cfree1}</Text>
      <Text>批号: {item.plot}</Text>
      <Text>生产订单号: {item.cdefine22}</Text>
      <Text>销售部门: {item.cdefine32}</Text>
      <Text>制单人: {item.cmaker}</Text>
      <Text>制单时间: {item.createDate}</Text>
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
          keyExtractor={item => item.barCode}
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