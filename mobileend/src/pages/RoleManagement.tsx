import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, TextInput, Modal, ImageBackground } from 'react-native';
import { Button, Card } from 'react-native-paper';
import Icon from 'react-native-vector-icons/AntDesign';
import api from '../api';

interface Role {
  id: number;
  name: string;
  code: string;
  description?: string;
}

const RoleManagement = () => {
  const [roles, setRoles] = useState<Role[]>([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<Role | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    description: ''
  });

  useEffect(() => {
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    try {
      const { data } = await api.get<Role[]>('/api/roles');
      console.log('获取到的角色数量:', data.length);
      console.log('准备设置的角色数据:', data);
      setRoles(data);
      console.log('当前状态中的角色数据:', roles);
    } catch (error) {
      console.error('获取角色列表失败', error);
    }
  };

  const handleSubmit = async () => {
    try {
      if (current) {
        await api.put(`/api/roles/${current.id}`, formData);
      } else {
        await api.post('/api/roles', formData);
      }
      setVisible(false);
      setFormData({
        name: '',
        code: '',
        description: ''
      });
      fetchRoles();
    } catch (error) {
      console.error('操作失败', error);
    }
  };

  const handleEditRole = (role: Role) => {
    setCurrent(role);
    setFormData({
      name: role.name,
      code: role.code,
      description: role.description || ''
    });
    setVisible(true);
  };

  const handleDelete = async (id:number) => {
    await api.delete(`/api/roles/${id}`);
    fetchRoles();
  };

  return (
    <ImageBackground
      source={require('../../assets/login-bg.png')}
      style={styles.backgroundImage}
    >
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Title 
          title="角色管理" 
          left={(props) => <Icon name="team" {...props} />}
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
            新增角色
          </Button>

          <ScrollView style={{ height: 150 }}>
            {roles.map((item) => (
              <View style={styles.roleItem} key={item.id.toString()}> 
                <View>
                  <Text style={styles.roleName}>{item.name}</Text>
                  <Text style={styles.roleCode}>{item.code}</Text>
                </View>
                <View style={styles.actions}>
                  <TouchableOpacity onPress={() => handleEditRole(item)}>
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
            {current ? '编辑角色' : '新增角色'}
          </Text>

          <TextInput
            style={styles.input}
            placeholder="角色名称"
            value={formData.name}
            onChangeText={(text) => setFormData({...formData, name: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="角色编码"
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
  </ImageBackground> 
  );
};

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    width: '100%',
    height: '100%',
    resizeMode: 'cover',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: 'transparent',
  },
  card: {
    flex: 1,
  },
  addButton: {
    marginBottom: 16,
  },
  roleItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  roleName: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  roleCode: {
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

export default RoleManagement;