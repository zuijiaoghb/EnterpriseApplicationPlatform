import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, TextInput, Modal } from 'react-native';
import { Button, Card } from 'react-native-paper';
import Icon from 'react-native-vector-icons/AntDesign';
import api from '../api';

interface Client {
  clientId: string;
  clientName: string;
  scopes?: string;
}

const ClientManagement = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<Client | null>(null);
  const [formData, setFormData] = useState({
    clientName: '',
    scopes: ''
  });

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    try {
      const { data } = await api.get<Client[]>('/api/clients/all');
      setClients(data);
    } catch (error) {
      console.error('获取客户端列表失败', error);
    }
  };

  const handleSubmit = async () => {
    try {
      if (current) {
        await api.put(`/api/clients/${current.clientId}`, formData);
      } else {
        await api.post('/api/clients', formData);
      }
      setVisible(false);
      setFormData({
        clientName: '',
        scopes: ''
      });
      fetchClients();
    } catch (error) {
      console.error('操作失败', error);
    }
  };

  const handleEditClient = (client:Client) => {
    setCurrent(client);
    setFormData({
      clientName: client.clientName,
      scopes: client.scopes || ''
    });
    setVisible(true);
  };

  const handleDelete = async (clientId:string) => {
    await api.delete(`/api/clients/${clientId}`);
    fetchClients();
  };

  return (
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Title 
          title="客户端管理" 
          left={(props) => <Icon name="idcard"  {...props} />}
        />
        <Card.Content>
          <Button 
            mode="contained" 
            onPress={() => {
              setCurrent(null);
              setVisible(true);
            }}
            style={styles.addButton}
          >
            新增客户端
          </Button>

          <FlatList
            data={clients}
            keyExtractor={(item) => item.clientId}
            renderItem={({ item }) => (
              <View style={styles.clientItem}>
                <View>
                  <Text style={styles.clientName}>{item.clientName}</Text>
                  <Text style={styles.clientId}>{item.clientId}</Text>
                </View>
                <View style={styles.actions}>
                  <TouchableOpacity onPress={() => handleEditClient(item)}>
                    <Icon name="edit" size={20} color="#1890ff" />
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => handleDelete(item.clientId)}>
                    <Icon name="delete" size={20} color="#ff4d4f" />
                  </TouchableOpacity>
                </View>
              </View>
            )}
          />
        </Card.Content>
      </Card>

      <Modal visible={visible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>
            {current ? '编辑客户端' : '新增客户端'}
          </Text>

          <TextInput
            style={styles.input}
            placeholder="客户端名称"
            value={formData.clientName}
            onChangeText={(text) => setFormData({...formData, clientName: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="权限范围"
            value={formData.scopes}
            onChangeText={(text) => setFormData({...formData, scopes: text})}
          />

          <View style={styles.buttonGroup}>
            <Button 
              mode="contained" 
              onPress={handleSubmit}
              style={styles.submitButton}
            >
              提交
            </Button>
            <Button 
              mode="outlined" 
              onPress={() => setVisible(false)}
              style={styles.cancelButton}
            >
              取消
            </Button>
          </View>
        </View>
      </Modal>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  card: {
    flex: 1,
  },
  addButton: {
    marginBottom: 16,
  },
  clientItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  clientName: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  clientId: {
    color: '#666',
  },
  actions: {
    flexDirection: 'row',
    gap: 16,
  },
  modalContainer: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 24,
    textAlign: 'center',
  },
  input: {
    height: 40,
    borderColor: '#d9d9d9',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 8,
    marginBottom: 16,
  },
  buttonGroup: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 24,
  },
  submitButton: {
    flex: 1,
    marginRight: 8,
  },
  cancelButton: {
    flex: 1,
    marginLeft: 8,
  },
});

export default ClientManagement;