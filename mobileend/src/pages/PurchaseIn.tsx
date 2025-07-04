import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Alert, Button, ScrollView, TextInput } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import BarcodeScannerComponent from '../components/BarcodeScannerComponent';
import { Code } from 'react-native-vision-camera';
import api from '../api';

const PurchaseIn = () => {
  const [scanned, setScanned] = useState(false);
  const [scannedData, setScannedData] = useState<Code | null>(null);
  const [warehouses, setWarehouses] = useState([{ code: '', name: '请选择仓库' }]);
  const [selectedWarehouse, setSelectedWarehouse] = useState('');
  const [poQuantity, setPoQuantity] = useState(0);
  const [productName, setProductName] = useState('');
  const [inboundQuantity, setInboundQuantity] = useState('');
  const [loading, setLoading] = useState(false);

  // 获取仓库列表（前端硬编码）
  useEffect(() => {
    const warehousesData = [
      { code: '019', name: '半成品仓（建机）' },
      { code: '018', name: '半成品仓（专用）' },
      { code: '035', name: '成品库（建机）' },
      { code: '034', name: '成品库（专用）' },
      { code: '015', name: '原材料仓（建机）' },
      { code: '014', name: '原材料仓（专用）' }
    ];
    setWarehouses([{ code: '', name: '请选择仓库' }, ...warehousesData]);    
  }, []);

  // 扫描后获取采购订单信息
  useEffect(() => {
    if (scannedData) {
      fetchPOInfo();
    }
  }, [scannedData]);

  const fetchPOInfo = async () => {
    if (!scannedData) return;

    setLoading(true);
    try {
      const response = await api.get(`/api/inventory/purchase/scan-in?barcode=${scannedData.value}`);
      setPoQuantity(response.data.iQuantity);
      // 默认选中第一个仓库
      if (warehouses.length > 1) {
        setSelectedWarehouse(warehouses[1].code);
      }
      // 获取产品名称
      const parts = (scannedData.value ?? '').split('_');
      const invCode = parts.length > 1 ? parts[1] : scannedData.value;
      const inventoryResponse = await api.get(`/api/inventory/inventories/${invCode}`);
      setProductName(inventoryResponse.data.cinvName);
    } catch (error) {
      console.error('获取采购订单信息失败:', error);
      Alert.alert('错误', '获取采购订单信息失败');
    } finally {
      setLoading(false);
    }
  };

  const handleCodeScanned = (code: Code) => {
    console.log('采购入库扫描结果:', code);
    setScanned(true);
    setScannedData(code);
  };

  const resetScan = () => {
    setScanned(false);
    setScannedData(null);
    setInboundQuantity('');
    setPoQuantity(0);
  };

  const validateInput = () => {
    if (!selectedWarehouse) {
      Alert.alert('错误', '请选择仓库');
      return false;
    }

    if (!inboundQuantity || isNaN(Number(inboundQuantity))) {
      Alert.alert('错误', '请输入有效的入库数量');
      return false;
    }

    const quantity = Number(inboundQuantity);
    if (quantity <= 0) {
      Alert.alert('错误', '入库数量必须大于0');
      return false;
    }

    if (quantity > poQuantity) {
      Alert.alert('错误', `入库数量不能大于采购订单数量(${poQuantity})`);
      return false;
    }

    return true;
  };

  const handleConfirm = async () => {
    if (!scannedData) return;
    if (!validateInput()) return;

    setLoading(true);
    try {
      const response = await api.post('/api/inventory/inbound/barcode', null, {
        params: {
          barcode: scannedData.value,
          warehouseCode: selectedWarehouse,
          quantity: Number(inboundQuantity)
        }
      });

      if (response.status === 201) {
        Alert.alert('成功', `采购入库已确认\n入库单号: ${response.data.id}`);
        resetScan();
      } else {
        Alert.alert('错误', '入库失败，请重试');
      }
    } catch (error) {
      console.error('入库操作失败:', error);
      Alert.alert('错误', '网络异常，请重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>采购入库</Text>
      </View>

      {/* 扫码区域 */}
      <View style={styles.cameraContainer}>
        <BarcodeScannerComponent
          onCodeScanned={handleCodeScanned}
          isActive={!scanned}
        />
      </View>

      {/* 结果显示区域 */}
      <View style={styles.resultContainer}>
        <ScrollView style={styles.resultScroll}>
          {loading ? (
            <View style={styles.loadingState}>
              <Text style={styles.loadingText}>加载中...</Text>
            </View>
          ) : scannedData ? (
            <View style={styles.resultCard}>
              <Text style={styles.resultTitle}>扫描结果</Text>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>条码类型:</Text>
                <Text style={styles.resultValue}>{scannedData.type}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>物料编码:</Text>
                <Text style={styles.resultValue}>{scannedData.value}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>产品名称:</Text>
                <Text style={styles.resultValue}>{productName}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>采购订单数量:</Text>
                <Text style={styles.resultValue}>{poQuantity} 件</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>选择仓库:</Text>
                <Picker
                  selectedValue={selectedWarehouse}
                  style={styles.picker}
                  onValueChange={(itemValue) => setSelectedWarehouse(itemValue)}
                >
                  {warehouses.map(warehouse => (
                    <Picker.Item key={warehouse.code} label={warehouse.name} value={warehouse.code} />
                  ))}
                </Picker>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>入库数量:</Text>
                <TextInput
                  style={styles.input}
                  keyboardType="numeric"
                  value={inboundQuantity}
                  onChangeText={setInboundQuantity}
                  placeholder={`请输入(1-${poQuantity})`}
                />
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>扫描时间:</Text>
                <Text style={styles.resultValue}>{new Date().toLocaleString()}</Text>
              </View>
              <View style={styles.actionButtons}>
                <Button
                  title="确认入库"
                  onPress={handleConfirm}
                  color="#4CAF50"
                  disabled={loading}
                />
              </View>
            </View>
          ) : (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>请对准物料条码进行扫描</Text>
              <Text style={styles.hintText}>支持QR码、EAN-13、Code-128等多种格式</Text>
            </View>
          )}
        </ScrollView>

        {/* 操作按钮 */}
        {scanned && (
          <View style={styles.buttonContainer}>
            <Button
              title="再次扫描"
              onPress={resetScan}
              color="#2196F3"
            />
          </View>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f5f5f5',
  },
  header: {
    padding: 16,
    backgroundColor: '#2196F3',
    alignItems: 'center',
  },
  title: {
    fontSize: 20,
    color: '#fff',
    fontWeight: 'bold',
  },
  cameraContainer: {
    flex: 1,
    position: 'relative',
  },
  resultContainer: {
    flex: 2,
    padding: 16,
    backgroundColor: '#fff',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    marginTop: -20,
    elevation: 5,
  },
  resultScroll: {
    flex: 1,
  },
  resultCard: {
    backgroundColor: '#f9f9f9',
    borderRadius: 12,
    padding: 16,
    elevation: 2,
  },
  resultTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
    color: '#333',
    textAlign: 'center',
  },
  resultItem: {
    flexDirection: 'row',
    marginBottom: 12,
    paddingBottom: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  resultLabel: {
    fontSize: 14,
    color: '#666',
    width: '35%',
    fontWeight: '500',
  },
  resultValue: {
    fontSize: 14,
    color: '#333',
    width: '65%',
    flexWrap: 'wrap',
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    fontSize: 16,
    color: '#666',
  },
  emptyText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 8,
  },
  hintText: {
    fontSize: 12,
    color: '#999',
    textAlign: 'center',
  },
  buttonContainer: {
    marginTop: 16,
    paddingVertical: 8,
  },
  actionButtons: {
    marginTop: 16,
  },
  input: {
    height: 40,
    borderColor: '#ddd',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 8,
    width: '65%',
  },
  picker: {
    height: 60,
    width: '100%',
    color: '#333',
  },
});

export default PurchaseIn;