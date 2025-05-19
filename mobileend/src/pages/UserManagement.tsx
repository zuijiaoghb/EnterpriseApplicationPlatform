import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, TextInput, Modal } from 'react-native';
import { Button, Card } from 'react-native-paper';
import Icon from 'react-native-vector-icons/AntDesign';
import api from '../api';

const UserManagement = () => {
  interface User {
  id: number;
  username: string;
  email: string;
  roles?: Role[];
}

interface Role {
  id: number;
  name: string;
  code: string;
}

interface ApiResponse<T> {
  content: T[];
}

const [users, setUsers] = useState<User[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<User | null>(null);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    roleIds: [] as number[]
  });

  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, []);

  const fetchUsers = async () => {
    try {
      const { data } = await api.get<ApiResponse<User>>('/api/users');
      setUsers(data.content || []);
    } catch (error) {
      console.error('获取用户列表失败', error);
    }
  };

  const fetchRoles = async () => {
    const { data } = await api.get<Role[]>('/api/roles');
    setRoles(data);
  };

  const handleSubmit = async () => {
    try {
      if (current) {
        await api.put(`/api/users/${current.id}`, formData);
      } else {
        await api.post('/api/users', formData);
      }
      setVisible(false);
      setFormData({
        username: '',
        email: '',
        password: '',
        roleIds: []
      });
      fetchUsers();
    } catch (error) {
      console.error('操作失败', error);
    }
  };

  const handleEditUser = (user: User) => {
    setCurrent(user);
    setFormData({
      username: user.username,
      email: user.email,
      password: '',
      roleIds: user.roles?.map(role => role.id) || []
    });
    setVisible(true);
  };

  const handleDelete = async (id: number) => {
    await api.delete(`/api/users/${id}`);
    fetchUsers();
  };

  return (
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Title 
          title="用户管理" 
          left={(props) => <Icon name="user" {...props} />}
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
            新增用户
          </Button>

          <FlatList
            data={users}
            keyExtractor={(item) => item.id.toString()}
            renderItem={({ item }) => (
              <View style={styles.userItem}>
                <Text style={styles.userName}>{item.username}</Text>
                <Text style={styles.userEmail}>{item.email}</Text>
                <View style={styles.actions}>
                  <TouchableOpacity onPress={() => handleEditUser(item)}>
                    <Icon name="edit" size={20} color="#1890ff" />
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => handleDelete(item.id)}>
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
            {current ? '编辑用户' : '新增用户'}
          </Text>

          <TextInput
            style={styles.input}
            placeholder="用户名"
            value={formData.username}
            onChangeText={(text) => setFormData({...formData, username: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="邮箱"
            value={formData.email}
            onChangeText={(text) => setFormData({...formData, email: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="密码"
            secureTextEntry
            value={formData.password}
            onChangeText={(text) => setFormData({...formData, password: text})}
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
  userItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  userName: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  userEmail: {
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

export default UserManagement;