import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, TextInput, Modal } from 'react-native';
import { Button, Card } from 'react-native-paper';
import Icon from 'react-native-vector-icons/AntDesign';
import api from '../api';

interface Permission {
  id: number;
  name: string;
  code: string;
  description?: string;
}

const PermissionManagement = () => {
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<Permission | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    description: ''
  });

  useEffect(() => {
    fetchPermissions();
  }, []);

  const fetchPermissions = async () => {
    try {
      const { data } = await api.get<Permission[]>('/api/permissions');
      setPermissions(data);
    } catch (error) {
      console.error('获取权限列表失败', error);
    }
  };

  const handleSubmit = async () => {
    try {
      if (current) {
        await api.put(`/api/permissions/${current.id}`, formData);
      } else {
        await api.post('/api/permissions', formData);
      }
      setVisible(false);
      setFormData({
        name: '',
        code: '',
        description: ''
      });
      fetchPermissions();
    } catch (error) {
      console.error('操作失败', error);
    }
  };

  const handleEditPermission = (permission: Permission) => {
    setCurrent(permission);
    setFormData({
      name: permission.name,
      code: permission.code,
      description: permission.description || ''
    });
    setVisible(true);
  };

  const handleDelete = async (id:number) => {
    await api.delete(`/api/permissions/${id}`);
    fetchPermissions();
  };

  return (
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Title 
          title="权限管理" 
          left={(props) => <Icon name="lock"  {...props} />}
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
            新增权限
          </Button>

          <ScrollView style={{ height: 150 }}>
            {permissions.map((item) => (
              <View style={styles.permissionItem} key={item.id.toString()}>
                <View>
                  <Text style={styles.permissionName}>{item.name}</Text>
                  <Text style={styles.permissionCode}>{item.code}</Text>
                </View>
                <View style={styles.actions}>
                  <TouchableOpacity onPress={() => handleEditPermission(item)}>
                    <Icon name="edit" size={20} color="#1890ff" />
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => handleDelete(item.id)}>
                    <Icon name="delete" size={20} color="#ff4d4f" />
                  </TouchableOpacity>
                </View>
              </View>
            ))}
          </ScrollView>
        </Card.Content>
      </Card>

      <Modal visible={visible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>
            {current ? '编辑权限' : '新增权限'}
          </Text>

          <TextInput
            style={styles.input}
            placeholder="权限名称"
            value={formData.name}
            onChangeText={(text) => setFormData({...formData, name: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="权限编码"
            value={formData.code}
            onChangeText={(text) => setFormData({...formData, code: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="描述"
            value={formData.description}
            onChangeText={(text) => setFormData({...formData, description: text})}
            multiline
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
  permissionItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  permissionName: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  permissionCode: {
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

export default PermissionManagement;