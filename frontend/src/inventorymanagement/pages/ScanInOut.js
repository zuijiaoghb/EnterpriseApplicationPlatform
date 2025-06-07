import React, { useState } from 'react';
import { Button, Input, message } from 'antd';

export default function ScanInOut() {
  const [barcode, setBarcode] = useState('');
  const [scanType, setScanType] = useState('in'); // 'in'或'out'

  const handleScan = () => {
    if (!barcode) {
      message.warning('请输入条码');
      return;
    }
    
    // 调用扫码API
    message.success(`已${scanType === 'in' ? '入库' : '出库'}条码: ${barcode}`);
    setBarcode('');
  };

  return (
    <div className="scan-in-out">
      <div style={{ marginBottom: 16 }}>
        <Button 
          type={scanType === 'in' ? 'primary' : 'default'} 
          onClick={() => setScanType('in')}
        >
          入库
        </Button>
        <Button 
          type={scanType === 'out' ? 'primary' : 'default'} 
          onClick={() => setScanType('out')}
          style={{ marginLeft: 8 }}
        >
          出库
        </Button>
      </div>
      
      <Input.Search
        placeholder="扫描或输入条码"
        enterButton={scanType === 'in' ? '入库' : '出库'}
        value={barcode}
        onChange={(e) => setBarcode(e.target.value)}
        onSearch={handleScan}
      />
    </div>
  );
}