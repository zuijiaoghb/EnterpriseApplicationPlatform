import React, { useState } from 'react';
import { Tabs } from 'antd';
import BarcodeManagement from './BarcodeManagement';
import ScanInOut from './ScanInOut';

const { TabPane } = Tabs;

export default function InventoryManagement() {
  const [activeKey, setActiveKey] = useState('barcode');

  return (
    <div className="inventory-management">
      <Tabs 
        activeKey={activeKey}
        onChange={setActiveKey}
        tabBarStyle={{ marginBottom: 24 }}
      >
        <TabPane tab="条码管理" key="barcode">
          <BarcodeManagement />
        </TabPane>
        <TabPane tab="扫码出入库" key="scan">
          <ScanInOut />
        </TabPane>
      </Tabs>
    </div>
  );
}