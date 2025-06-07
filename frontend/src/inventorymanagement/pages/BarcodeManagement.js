import React, { useEffect, useState } from 'react';
import { Table, Button, message } from 'antd';
import api from '../../api';

export default function BarcodeManagement() {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);

  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 100,
    total: 0
  });

  const fetchBarcodeData = async () => {
    try {
      setLoading(true);
    
      const response = await api.get(`/api/hy-barcode-main`, {
        params: {
          page: pagination.current - 1,
          size: pagination.pageSize
        }
      });      
      
      setData(response.data.content);
      //console.log(response.data.content);
      setPagination({
        ...pagination,
        total: response.data.totalElements
      });
    } catch (error) {
      console.error('获取条码数据失败:', error);
      message.error('获取条码数据失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBarcodeData();
  }, [pagination.current, pagination.pageSize]);

  const columns = [
    {
      title: '条码编号',
      dataIndex: 'barCode',
      key: 'barcode',
    },
    {
      title: '条码规则',
      dataIndex: 'barCodeRule',
      key: 'barCodeRule',
    },
    {
      title: '序列号',
      dataIndex: 'cinvSN',
      key: 'cinvSN',
    },
    {
      title: '产品编码',
      dataIndex: 'cinvCode',
      key: 'cinvCode',
    },
    {
      title: '尾标',
      dataIndex: 'cfree1',
      key: 'cfree1',
    },
    {
      title: '批号',
      dataIndex: 'plot',
      key: 'plot',
    },
    {
      title: '生产订单号',
      dataIndex: 'cdefine22',
      key: 'cdefine22',
    },
    {
      title: '销售部门',
      dataIndex: 'cdefine32',
      key: 'cdefine32',
    },
    {
      title: '制单人',
      dataIndex: 'cmaker',
      key: 'cmaker',
    },
    {
      title: '制单时间',
      dataIndex: 'createDate',
      key: 'createDate',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Button type="link" onClick={() => handleEdit(record)}>
          编辑
        </Button>
      ),
    },
  ];

  const handleEdit = (record) => {
    // 编辑条码逻辑
  };

  return (
    <div className="barcode-management">
      <div style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={fetchBarcodeData} loading={loading}>
          刷新
        </Button>
      </div>
      <Table 
        columns={columns} 
        dataSource={data} 
        loading={loading}
        rowKey="barcode"
        pagination={{
          ...pagination,
          showSizeChanger: true,
          showTotal: total => `共 ${total} 条`,
          onChange: (page, pageSize) => {
            setPagination({...pagination, current: page, pageSize});
          },
          onShowSizeChange: (current, size) => {
            setPagination({...pagination, current: 1, pageSize: size});
          }
        }}
        bordered
      />
    </div>
  );
}