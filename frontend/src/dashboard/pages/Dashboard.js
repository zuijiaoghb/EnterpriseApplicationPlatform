import React from 'react';
import { Card, Row, Col } from 'antd';

const Dashboard = () => {
  return (
    <div className="dashboard">
      <Row gutter={[16, 16]}>
        <Col span={8}>
          <Card title="设备总数" variant={false}>
            128
          </Card>
        </Col>
        <Col span={8}>
          <Card title="运行中设备" bordered={false}>
            98
          </Card>
        </Col>
        <Col span={8}>
          <Card title="待维护设备" bordered={false}>
            15
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;