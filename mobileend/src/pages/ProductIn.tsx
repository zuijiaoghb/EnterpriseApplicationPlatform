import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Alert, Button, ScrollView, TextInput } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import BarcodeScannerComponent from '../components/BarcodeScannerComponent';
import { Code } from 'react-native-vision-camera';
import api from '../api';

const ProductIn = () => {
  const [scanned, setScanned] = useState(false);
  const [scannedData, setScannedData] = useState<Code | null>(null);
  const [orderQuantity, setOrderQuantity] = useState<number | null>(null);
  const [qualifiedInQty, setQualifiedInQty] = useState<number | null>(null);
  const [remainingQuantity, setRemainingQuantity] = useState<number | null>(null);
  const [cwhcode, setCwhcode] = useState('');
  const [productName, setProductName] = useState('');
  const [iQuantity, setIQuantity] = useState('');
  const [loading, setLoading] = useState(false);
  const [warehouses, setWarehouses] = useState([{ code: '', name: '请选择仓库' }]);

  // 初始化仓库列表
  useEffect(() => {
    const warehousesData = [    
      { code: '046', name: '半成品仓（50亩）' },
      { code: '035', name: '成品库（建机）' },
      { code: '034', name: '成品库（专用）' },
      { code: '015', name: '原材料仓（建机）' },
      { code: '014', name: '原材料仓（专用）' }
    ];
    setWarehouses([{ code: '', name: '请选择仓库' }, ...warehousesData]);        
  }, []);

  const handleCodeScanned = async (code: Code) => {
    console.log('产成品入库扫描结果:', code);
    setScanned(true);
    setScannedData(code);
    
    try {
      const response = await api.get(`/api/inventory/mom-orders/barcode/detail/${code.value}`);
      setOrderQuantity(response.data.iquantity);
      setQualifiedInQty(response.data.qualifiedInQty);
      setRemainingQuantity(response.data.iquantity - response.data.qualifiedInQty);

      const parts = (code.value ?? '').split('_');
      const invCode = parts.length > 1 ? parts[1] : code.value;
      const inventoryResponse = await api.get(`/api/inventory/inventories/${invCode}`);
      setProductName(inventoryResponse.data.cinvName);
    } catch (error) {
      console.error('获取订单明细失败:', error);
      Alert.alert('错误', '无法获取订单信息');
    }
  };

  const resetScan = () => {
    setScanned(false);
    setScannedData(null);
    setCwhcode('');
    setIQuantity('');
  };

  const handleConfirm = async () => {
    if (!scannedData || !cwhcode || !iQuantity.trim()) {
      Alert.alert('错误', '请选择仓库并输入有效的入库数量');
      return;
    }

    const quantity = parseInt(iQuantity, 10);
    if (isNaN(quantity) || quantity <= 0) {
      Alert.alert('错误', '请输入有效的入库数量');
      return;
    }

    if (orderQuantity === null || qualifiedInQty === null || remainingQuantity === null) {
      Alert.alert('错误', '订单信息不完整，请重新扫描');
      return;
    }

    if (quantity > remainingQuantity) {
      Alert.alert('错误', `入库数量不能超过剩余可入库数量: ${remainingQuantity}`);
      return;
    }

    try {
      setLoading(true);
      const response = await api.post('/api/inventory/products/barcode/generate/' + scannedData.value, null, {
        params: {
          cwhcode: cwhcode,
          iQuantity: quantity
        }
      });
      Alert.alert('成功', `产成品入库已确认\n入库单号: ${response.data.id}`);
      resetScan();
    } catch (error) {
      console.error('入库失败:', error);
      Alert.alert('失败', '生成入库单失败，请重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>产成品入库</Text>
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
          {scannedData ? (
            <View style={styles.resultCard}>
              <Text style={styles.resultTitle}>扫描结果</Text>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>条码类型:</Text>
                <Text style={styles.resultValue}>{scannedData.type}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>产品编码:</Text>
                <Text style={styles.resultValue}>{scannedData.value}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>产品名称:</Text>
                <Text style={styles.resultValue}>{productName}</Text>
              </View>
              {orderQuantity !== null && (
                <View style={styles.resultItem}>
                  <Text style={styles.resultLabel}>订单数量:</Text>
                  <Text style={styles.resultValue}>{orderQuantity}</Text>
                </View>
              )}
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>扫描时间:</Text>
                <Text style={styles.resultValue}>{new Date().toLocaleString()}</Text>
              </View>
              <View style={styles.formGroup}>
                <Text style={styles.formLabel}>仓库选择:</Text>
                <Picker
                  style={styles.picker}
                  selectedValue={cwhcode}
                  onValueChange={(itemValue) => setCwhcode(itemValue)}
                >
                  {warehouses.map((warehouse, index) => (
                    <Picker.Item
                      key={index}
                      label={warehouse.name}
                      value={warehouse.code}
                    />
                  ))}
                </Picker>
              </View>
              <View style={styles.formGroup}>
                <Text style={styles.formLabel}>入库数量:</Text>
                <TextInput
                  style={styles.formInput}
                  value={iQuantity}
                  onChangeText={setIQuantity}
                  placeholder={`请输入入库数量 (最大可入库: ${remainingQuantity || 0})`}
                  keyboardType="numeric"
                />
                {remainingQuantity !== null && (
                  <Text style={styles.quantityHint}>
                    剩余可入库数量: {remainingQuantity}
                  </Text>
                )}
              </View>
              <View style={styles.actionButtons}>
                <Button
                  title={loading ? "处理中..." : "确认入库"}
                  onPress={handleConfirm}
                  color="#4CAF50"
                  disabled={loading}
                />
              </View>
            </View>
          ) : (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>请对准产品条码进行扫描</Text>
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
    backgroundColor: '#4CAF50',
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
  formGroup: {
    marginBottom: 16,
  },
  formLabel: {
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
    fontWeight: '500',
  },
  formInput: {
    height: 40,
    borderColor: '#ddd',
    borderWidth: 1,
    borderRadius: 6,
    paddingHorizontal: 12,
    fontSize: 14,
  },
  picker: {
    height: 60,
    borderColor: '#ddd',
    borderWidth: 1,
    borderRadius: 6,
    fontSize: 14,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
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
  quantityHint: {
    fontSize: 12,
    color: '#666',
    marginTop: 4,
    fontStyle: 'italic'
  }
});

export default ProductIn;