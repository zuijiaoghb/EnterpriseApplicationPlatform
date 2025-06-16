import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Alert, Button, ScrollView } from 'react-native';
import { Camera, useCameraDevices, useCodeScanner, Code } from 'react-native-vision-camera';
import { check, request, PERMISSIONS, RESULTS } from 'react-native-permissions';

const ScanInOut = () => {
  const [hasPermission, setHasPermission] = useState(false);
  const [scanned, setScanned] = useState(false);
  const [scannedData, setScannedData] = useState<Code | null>(null); // 修复类型定义
  const devices = useCameraDevices();
  const device = devices.find(device => device.position === 'back');

  const codeScanner = useCodeScanner({
    codeTypes: ['qr', 'ean-13', 'code-128', 'code-39', 'ean-8', 'upc-a', 'upc-e'],
    onCodeScanned: (codes) => {
      console.log('扫描到的条码数据:', JSON.stringify(codes));
      if (codes.length > 0 && !scanned) {
        setScanned(true);
        setScannedData(codes[0]); // 存储扫描结果
      }
    }
  });

  useEffect(() => {
    (async () => {
      const checkPermission = async () => {
        const status = await check(PERMISSIONS.ANDROID.CAMERA);
        if (status === RESULTS.GRANTED) return true;
        const requestStatus = await request(PERMISSIONS.ANDROID.CAMERA);
        return requestStatus === RESULTS.GRANTED;
      };

      const permissionGranted = await checkPermission();
      setHasPermission(permissionGranted);
      if (!permissionGranted) {
        Alert.alert('权限不足', '请在应用设置中启用相机权限');
      }
    })();
  }, []);

  // 重置扫描状态
  const resetScan = () => {
    setScanned(false);
    setScannedData(null);
  };

  if (device == null) {
    return <Text>正在加载相机...</Text>;
  }

  return (
    <View style={styles.container}>
      {/* 上半部分：扫码区域 */}
      <View style={styles.cameraContainer}>
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive={!scanned}
          codeScanner={codeScanner}
          enableZoomGesture={true}
          onError={(error) => {
            console.error('相机错误:', error);
            Alert.alert('相机初始化失败', `错误信息: ${error.message}\n请确保条码模块已正确下载并配置`);
          }}
        />
        {/* 扫描框指示器 */}
        {!scanned && (
          <View style={styles.scanFrame}>
            <View style={[styles.scanCorner, styles.topLeft]}></View>
            <View style={[styles.scanCorner, styles.topRight]}></View>
            <View style={[styles.scanCorner, styles.bottomLeft]}></View>
            <View style={[styles.scanCorner, styles.bottomRight]}></View>
          </View>
        )}
      </View>

      {/* 下半部分：结果显示区域 */}
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
                <Text style={styles.resultLabel}>条码数据:</Text>
                <Text style={styles.resultValue}>{scannedData.value}</Text>
              </View>
              <View style={styles.resultItem}>
                <Text style={styles.resultLabel}>扫描时间:</Text>
                <Text style={styles.resultValue}>{new Date().toLocaleString()}</Text>
              </View>
            </View>
          ) : (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>请对准条码进行扫描</Text>
              <Text style={styles.hintText}>支持QR码、EAN-13、Code-128等多种格式</Text>
            </View>
          )}
        </ScrollView>

        {/* 操作按钮 */}
        <View style={styles.buttonContainer}>
          <Button
            title={scanned ? '再次扫描' : '取消扫描'}
            onPress={resetScan}
            color={scanned ? '#2196F3' : '#f44336'}
          />
        </View>
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
  cameraContainer: {
    flex: 2, // 上半部分占2/3高度
    position: 'relative',
    backgroundColor: '#000',
  },
  resultContainer: {
    flex: 1, // 下半部分占1/3高度
    padding: 16,
    backgroundColor: '#fff',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    marginTop: -20,
    elevation: 5, // 添加阴影效果
  },
  scanFrame: {
    position: 'absolute',
    top: '30%',
    left: '15%',
    width: '70%',
    height: '40%',
    borderWidth: 2,
    borderColor: '#2196F3',
    borderStyle: 'dashed',
  },
  scanCorner: {
    position: 'absolute',
    width: 20,
    height: 20,
    borderWidth: 3,
    borderColor: '#2196F3',
  },
  topLeft: {
    top: -2,
    left: -2,
    borderRightWidth: 0,
    borderBottomWidth: 0,
  },
  topRight: {
    top: -2,
    right: -2,
    borderLeftWidth: 0,
    borderBottomWidth: 0,
  },
  bottomLeft: {
    bottom: -2,
    left: -2,
    borderRightWidth: 0,
    borderTopWidth: 0,
  },
  bottomRight: {
    bottom: -2,
    right: -2,
    borderLeftWidth: 0,
    borderTopWidth: 0,
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
});

export default ScanInOut;