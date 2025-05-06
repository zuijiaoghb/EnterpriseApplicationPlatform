import React, { useEffect } from 'react';
import { View, Text, Modal, StyleSheet, TextInput, TouchableOpacity } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';
import api from '../api';

interface EquipmentFormProps {
  visible: boolean;
  onClose: () => void;
  onSuccess: () => void;
  current: any;
}

const EquipmentForm: React.FC<EquipmentFormProps> = ({ visible, onClose, onSuccess, current }) => {
  const [name, setName] = React.useState('');
  const [model, setModel] = React.useState('');
  const [status, setStatus] = React.useState('RUNNING');
  const [qrCode, setQrCode] = React.useState('');
  const [lastMaintenance, setLastMaintenance] = React.useState(new Date());
  const [showDatePicker, setShowDatePicker] = React.useState(false);

  useEffect(() => {
    if (current) {
      setName(current.name || '');
      setModel(current.model || '');
      setStatus(current.status || 'RUNNING');
      setQrCode(current.qrCode || '');
      setLastMaintenance(current.lastMaintenance ? new Date(current.lastMaintenance) : new Date());
    } else {
      setName('');
      setModel('');
      setStatus('RUNNING');
      setQrCode('');
      setLastMaintenance(new Date());
    }
  }, [current]);

  const handleSubmit = async () => {
    try {
      const values = {
        name,
        model,
        status,
        qrCode,
        lastMaintenance: lastMaintenance.toISOString()
      };

      if (current) {
        await api.put(`/api/equipments/${current.id}`, values);
      } else {
        await api.post('/api/equipments', values);
      }
      onSuccess();
      onClose();
    } catch (error) {
      console.error('操作失败:', error);
    }
  };

  const onChangeDate = (event: any, selectedDate?: Date) => {
    const currentDate = selectedDate || lastMaintenance;
    setShowDatePicker(false);
    setLastMaintenance(currentDate);
  };

  return (
    <Modal
      animationType="slide"
      transparent={false}
      visible={visible}
      onRequestClose={onClose}
    >
      <View style={styles.container}>
        <Text style={styles.title}>{current ? '编辑设备' : '新增设备'}</Text>
        
        <Text style={styles.label}>设备名称</Text>
        <TextInput
          style={styles.input}
          value={name}
          onChangeText={setName}
          placeholder="请输入设备名称"
        />
        
        <Text style={styles.label}>设备型号</Text>
        <TextInput
          style={styles.input}
          value={model}
          onChangeText={setModel}
          placeholder="请输入设备型号"
        />
        
        <Text style={styles.label}>状态</Text>
        <Picker
          selectedValue={status}
          onValueChange={(itemValue) => setStatus(itemValue)}
          style={styles.picker}
        >
          <Picker.Item label="运行中" value="RUNNING" />
          <Picker.Item label="维护中" value="MAINTENANCE" />
          <Picker.Item label="故障" value="FAULT" />
        </Picker>
        
        <Text style={styles.label}>二维码</Text>
        <TextInput
          style={styles.input}
          value={qrCode}
          onChangeText={setQrCode}
          placeholder="请输入二维码"
        />
        
        <Text style={styles.label}>最后维护时间</Text>
        <TouchableOpacity onPress={() => setShowDatePicker(true)}>
          <Text style={styles.dateText}>
            {lastMaintenance.toLocaleDateString()}
          </Text>
        </TouchableOpacity>
        
        {showDatePicker && (
          <DateTimePicker
            value={lastMaintenance}
            mode="date"
            display="default"
            onChange={onChangeDate}
          />
        )}
        
        <View style={styles.buttonContainer}>
          <TouchableOpacity style={styles.button} onPress={onClose}>
            <Text style={styles.buttonText}>取消</Text>
          </TouchableOpacity>
          <TouchableOpacity style={[styles.button, styles.submitButton]} onPress={handleSubmit}>
            <Text style={styles.buttonText}>提交</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    marginTop: 16,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
    padding: 10,
    fontSize: 16,
  },
  picker: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
  },
  dateText: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
    padding: 10,
    fontSize: 16,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 30,
  },
  button: {
    padding: 15,
    borderRadius: 5,
    backgroundColor: '#ddd',
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 10,
  },
  submitButton: {
    backgroundColor: '#2196F3',
  },
  buttonText: {
    color: '#000',
    fontSize: 16,
  },
});

export default EquipmentForm;