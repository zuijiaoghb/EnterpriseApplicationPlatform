import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text, Alert } from 'react-native';
import { Camera, useCameraDevices, useCodeScanner, Code } from 'react-native-vision-camera';
import { check, request, PERMISSIONS, RESULTS } from 'react-native-permissions';

interface BarcodeScannerProps {
  onCodeScanned: (code: Code) => void;
  isActive: boolean;
}

const BarcodeScannerComponent: React.FC<BarcodeScannerProps> = ({ onCodeScanned, isActive }) => {
  const [hasPermission, setHasPermission] = useState(false);
  const devices = useCameraDevices();
  const device = devices.find(device => device.position === 'back');

  const codeScanner = useCodeScanner({
    codeTypes: ['qr', 'ean-13', 'code-128', 'code-39', 'ean-8', 'upc-a', 'upc-e'],
    onCodeScanned: (codes) => {
      if (codes.length > 0) {
        onCodeScanned(codes[0]);
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

  if (device == null) {
    return <View style={styles.loadingContainer}><Text>正在加载相机...</Text></View>;
  }

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFill}
        device={device}
        isActive={isActive && hasPermission}
        codeScanner={codeScanner}
        enableZoomGesture={true}
        onError={(error) => {
          console.error('相机错误:', error);
          Alert.alert('相机初始化失败', `错误信息: ${error.message}\n请确保条码模块已正确下载并配置`);
        }}
      />
      {/* 扫描框指示器 */}
      <View style={styles.scanFrame}>
        <View style={[styles.scanCorner, styles.topLeft]}></View>
        <View style={[styles.scanCorner, styles.topRight]}></View>
        <View style={[styles.scanCorner, styles.bottomLeft]}></View>
        <View style={[styles.scanCorner, styles.bottomRight]}></View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    position: 'relative',
    backgroundColor: '#000',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
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
});

export default BarcodeScannerComponent;