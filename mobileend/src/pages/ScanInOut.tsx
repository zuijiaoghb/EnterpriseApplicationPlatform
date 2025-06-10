import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Alert, Button } from 'react-native';
import { Camera, useCameraDevices } from 'react-native-vision-camera';
import { useScanBarcodes, BarcodeFormat } from 'vision-camera-code-scanner';
import { check, request, PERMISSIONS, RESULTS } from 'react-native-permissions';

const ScanInOut = () => {
  const [hasPermission, setHasPermission] = useState(false);
  const [scanned, setScanned] = useState(false);
  const devices = useCameraDevices();
  const device = devices.find(device => device.position === 'back');

  const [frameProcessor, barcodes] = useScanBarcodes([BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128, BarcodeFormat.EAN_13], {
    checkInverted: true
  });

  useEffect(() => {
    (async () => {
      const permissionStatus = await check(PERMISSIONS.ANDROID.CAMERA);
      const status = permissionStatus === RESULTS.GRANTED ? 'authorized' : 'unauthorized';
      setHasPermission(status === 'authorized');
    })();
  }, []);

  useEffect(() => {
    if (barcodes && barcodes.length > 0 && !scanned) {
      const barcode = barcodes[0];
      setScanned(true);
      Alert.alert(
        '条码扫描结果',
        `类型: ${barcode.format}\n数据: ${barcode.displayValue}`,
        [
          { text: '确定', onPress: () => setScanned(false) }
        ]
      );
    }
  }, [barcodes, scanned]);

  if (hasPermission === null) {
    return <Text>请求相机权限...</Text>;
  }
  if (hasPermission === false) {
    return <Text>无相机权限。</Text>;
  }
  if (device == null) {
    return <Text>正在加载相机...</Text>;
  }

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFill}
        device={device}
        isActive={!scanned}
        frameProcessor={frameProcessor as any}
      />
      {scanned && (
        <Button title={'再次扫描'} onPress={() => setScanned(false)} />
      )}
    </View>
  );
};

// 保留原有的styles定义
const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
  },
});

export default ScanInOut;