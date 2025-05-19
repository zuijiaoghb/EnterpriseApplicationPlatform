import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Card } from 'react-native-paper';

const Dashboard = () => {
  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Card style={styles.card}>
          <Card.Title title="设备总数" />
          <Card.Content>
            <Text style={styles.number}>128</Text>
          </Card.Content>
        </Card>
        <Card style={styles.card}>
          <Card.Title title="运行中设备" />
          <Card.Content>
            <Text style={styles.number}>98</Text>
          </Card.Content>
        </Card>
      </View>
      <View style={styles.row}>
        <Card style={styles.card}>
          <Card.Title title="待维护设备" />
          <Card.Content>
            <Text style={styles.number}>15</Text>
          </Card.Content>
        </Card>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  card: {
    width: '48%',
  },
  number: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
  },
});

export default Dashboard;