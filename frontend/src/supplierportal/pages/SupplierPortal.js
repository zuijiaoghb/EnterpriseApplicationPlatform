import React from 'react';
import { Card, Typography, List, Button } from 'antd';
import { Link } from 'react-router-dom';
import { ShoppingOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

const SupplierPortal = () => {
  return (
    <div className="supplier-portal">
      <Title level={2}>供应商门户</Title>
      <Card>
        <List
          dataSource={[
            {
              title: '采购订单管理',
              description: '查看和处理您的采购订单',
              icon: <ShoppingOutlined />,
              path: '/supplierportal/purchase-orders'
            }
          ]}
          renderItem={item => (
            <List.Item
              actions={[
                <Link to={item.path}>
                  <Button type="primary" size="small">
                    进入
                  </Button>
                </Link>
              ]}
            >
              <List.Item.Meta
                avatar={item.icon}
                title={<Text strong>{item.title}</Text>}
                description={item.description}
              />
            </List.Item>
          )}
        />
      </Card>
    </div>
  );
};

export default SupplierPortal;