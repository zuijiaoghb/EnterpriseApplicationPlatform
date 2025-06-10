import React from 'react';
import { View, TouchableOpacity, Text, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';

export default function InventoryManagement() {
  const navigation = useNavigation();

  return (
    <View style={styles.container}>
      <TouchableOpacity 
        style={styles.button}
        onPress={() => navigation.navigate('BarcodeManagement' as never)}
      >
        <Text style={styles.buttonText}>条码管理</Text>
      </TouchableOpacity>
      
      <TouchableOpacity 
        style={styles.button}
        onPress={() => navigation.navigate('ScanInOut' as never)}
      >
        <Text style={styles.buttonText}>扫码出入库</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  button: {
    backgroundColor: '#1890ff',
    padding: 15,
    borderRadius: 5,
    marginVertical: 10,
    width: '80%',
    alignItems: 'center',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
});