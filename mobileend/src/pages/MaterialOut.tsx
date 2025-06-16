import React, { useState } from 'react';
import { StyleSheet, Text, View, Alert, Button, ScrollView } from 'react-native';
import BarcodeScannerComponent from '../components/BarcodeScannerComponent';
import { Code } from 'react-native-vision-camera';

const MaterialOut = () => {
  const [scanned, setScanned] = useState(false);
  const [scannedData, setScannedData] = useState<Code | null>(null);

  const handleCodeScanned = (code: Code) => {
    console.log('材料出库扫描结果:', code);
    setScanned(true);
    setScannedData(code);
    // 这里可以添加材料出库的业务逻辑处理
  };

  const resetScan = () => {
    setScanned(false);
    setScannedData(null);
  };

  const handleConfirm = () => {
    if (!scannedData) return;
    // 材料出库确认逻辑
    Alert.alert('成功', `材料出库已确认\n条码: ${scannedData.value}`);
    resetScan();
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>材料出库</Text>
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
                <Text style={styles.resultLabel}>材料编码:</Text>
                <Text style={styles.resultValue}>{scannedData.value}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>扫描时间:</Text>
                <Text style={styles.resultValue}>{new Date().toLocaleString()}</Text>
              </View>
              <View style={styles.actionButtons}>
                <Button
                  title="确认出库"
                  onPress={handleConfirm}
                  color="#FF9800"
                />
              </View>
            </View>
          ) : (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>请对准材料条码进行扫描</Text>
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
    backgroundColor: '#FF9800',
    alignItems: 'center',
  },
  title: {
    fontSize: 20,
    color: '#fff',
    fontWeight: 'bold',
  },
  cameraContainer: {
    flex: 2,
    position: 'relative',
  },
  resultContainer: {
    flex: 1,
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
});

export default MaterialOut;