import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, ScrollView, RefreshControl, ImageBackground} from 'react-native';
import { Card } from 'react-native-paper';

const Dashboard = () => {
  const [refreshing, setRefreshing] = React.useState(false);
  const [deviceData, setDeviceData] = React.useState({
    total: 128,
    running: 98,
    maintenance: 15,
    offline: 15
  });

  // 下拉刷新处理
  const onRefresh = React.useCallback(() => {
    setRefreshing(true);
    // 模拟数据刷新
    setTimeout(() => {
      setDeviceData(prev => ({
        ...prev,
        running: Math.min(prev.total, prev.running + Math.floor(Math.random() * 5 - 2)),
        maintenance: Math.max(0, prev.maintenance + Math.floor(Math.random() * 3 - 1))
      }));
      setRefreshing(false);
    }, 1000);
  }, []);

  // 设备状态数据
  const statusData = [
    { name: '运行中', value: deviceData.running, color: '#4CAF50' },
    { name: '待维护', value: deviceData.maintenance, color: '#FF9800' },
    { name: '离线', value: deviceData.offline, color: '#F44336' },
  ];

  return (
    <ImageBackground
      source={require('../../assets/login-bg.png')}
      style={styles.backgroundImage}
    >
    <View style={styles.container}>
      {/* 顶部导航栏 */}
      <View style={styles.header}>
        <Text style={styles.title}>设备监控中心</Text>
      </View>

      <ScrollView
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor="#1976d2" />
        }
        contentContainerStyle={styles.scrollContent}
      >
        {/* 状态概览卡片 */}
        <View style={styles.cardContainer}>
          <Card style={styles.statusCard} elevation={5}>
            <Card.Content style={styles.statusContent}>
              <Text style={styles.statusTitle}>设备状态概览</Text>
              <View style={styles.statusList}>
                {statusData.map((item, index) => (
                  <View key={index} style={styles.statusItem}>
                    <View style={[styles.statusIndicator, { backgroundColor: item.color }]} />
                    <Text style={styles.statusText}>{item.name}: {item.value}台</Text>
                  </View>
                ))}
              </View>
            </Card.Content>
          </Card>
        </View>

        {/* 数据卡片行 */}
        <View style={styles.row}>
          {/* 总数卡片 */}
          <View style={styles.cardItem}>
            <Card style={[styles.dataCard, styles.totalCard]} elevation={4}>
              <Card.Content>
                <Text style={styles.cardLabel}>设备总数</Text>
                <Text style={styles.cardValue}>{deviceData.total}</Text>
              </Card.Content>
            </Card>
          </View>

          {/* 运行中卡片 */}
          <View style={styles.cardItem}>
            <Card style={[styles.dataCard, styles.runningCard]} elevation={4}>
              <Card.Content>
                <Text style={styles.cardLabel}>运行中设备</Text>
                <Text style={styles.cardValue}>{deviceData.running}</Text>
              </Card.Content>
            </Card>
          </View>
        </View>

        <View style={styles.row}>
          {/* 待维护卡片 */}
          <View style={styles.cardItem}>
            <Card style={[styles.dataCard, styles.maintenanceCard]} elevation={4}>
              <Card.Content>
                <Text style={styles.cardLabel}>待维护设备</Text>
                <Text style={styles.cardValue}>{deviceData.maintenance}</Text>
              </Card.Content>
            </Card>
          </View>

          {/* 离线卡片 */}
          <View style={styles.cardItem}>
            <Card style={[styles.dataCard, styles.offlineCard]} elevation={4}>
              <Card.Content>
                <Text style={styles.cardLabel}>离线设备</Text>
                <Text style={styles.cardValue}>{deviceData.offline}</Text>
              </Card.Content>
            </Card>
          </View>
        </View>
      </ScrollView>
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
  },
  container: {
    flex: 1,
  },
  header: {
    padding: 20,
    paddingTop: 40,
  },
  title: {
    color: '#2d3436',
    fontWeight: 'bold',
  },
  scrollContent: {
    padding: 16,
  },
  cardContainer: {
    marginBottom: 20,
  },
  statusCard: {
    borderRadius: 16,
    backgroundColor: '#ffffff',
  },
  statusContent: {
    alignItems: 'center',
    paddingVertical: 20,
  },
  statusList: {
    width: '100%',
    marginTop: 10,
  },
  statusItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 8,
    paddingHorizontal: 10,
  },
  statusIndicator: {
    width: 16,
    height: 16,
    borderRadius: 3,
    marginRight: 12,
  },
  statusText: {
    fontSize: 16,
    color: '#2d3436',
  },
  statusTitle: {
    marginBottom: 16,
    color: '#2d3436',
  },
  chartContainer: {
    alignItems: 'center',
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  cardItem: {
    width: '48%',
  },
  dataCard: {
    borderRadius: 12,
    padding: 16,
  },
  totalCard: {
    backgroundColor: '#e3f2fd',
  },
  runningCard: {
    backgroundColor: '#e8f5e9',
  },
  maintenanceCard: {
    backgroundColor: '#fff8e1',
  },
  offlineCard: {
    backgroundColor: '#ffebee',
  },
  cardLabel: {
    fontSize: 14,
    color: '#616161',
    marginBottom: 8,
  },
  cardValue: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#2d3436',
  },
});

export default Dashboard;