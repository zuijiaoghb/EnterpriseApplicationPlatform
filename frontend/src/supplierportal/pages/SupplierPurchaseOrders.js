import React, { useState, useEffect } from 'react';
import { Table, Card, Typography, Spin, message, Form, Input, DatePicker, Button } from 'antd';
import { useNavigate } from 'react-router-dom';
import api from '../../api';

const { Title } = Typography;

const SupplierPurchaseOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(50);
  const [total, setTotal] = useState(0);
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({});
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        // 获取当前登录用户信息
        const userResponse = await api.get('/auth/info');
        const vendorCode = userResponse.data.username;        

        if (!vendorCode) {
          message.error('无法获取供应商信息');
          return;
        }

        // 调用后端API获取供应商已审核的采购订单
        const response = await api.get('/api/inventory/purchase/vendor/audited-orders', {
          params: {
            vendorCode,
            pageNum: currentPage,
            pageSize,
            cPOID: searchParams.cPOID,
            dPODate: searchParams.dPODate,
            cInvCode: searchParams.cInvCode,
            cItemName: searchParams.cItemName
          }
        });
        setOrders(response.data.records);
        setTotal(response.data.total);
      } catch (error) {
        message.error('获取采购订单失败: ' + (error.response?.data?.message || error.message));
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [currentPage, pageSize]);

  const columns = [
    {
      title: '订单编号',
      dataIndex: 'cPOID',
      key: 'cPOID',
      sorter: (a, b) => a.cPOID.localeCompare(b.cPOID),
    },
    {
      title: '订单日期',
      dataIndex: 'dPODate',
      key: 'dPODate',
      render: (date) => date ? new Date(date).toLocaleString() : '',
      sorter: (a, b) => new Date(a.dPODate) - new Date(b.dPODate),
    },
    {
      title: '供应商代码',
      dataIndex: 'cVenCode',
      key: 'cVenCode',
      sorter: (a, b) => a.cVenCode.localeCompare(b.cVenCode),
    },
    {
      title: '供应商名称',
      dataIndex: 'supplierName',
      key: 'supplierName',
      sorter: (a, b) => a.supplierName.localeCompare(b.supplierName),
    },
    {
      title: '执行公司',
      dataIndex: 'cDefine1',
      key: 'cDefine1',
      sorter: (a, b) => a.cDefine1.localeCompare(b.cDefine1),
    },
    {
      title: '采购员',
      dataIndex: 'cPersonCode',
      key: 'cPersonCode',
      sorter: (a, b) => a.cPersonCode.localeCompare(b.cPersonCode),
    },
    {
      title: '采购部门',
      dataIndex: 'cDepCode',
      key: 'cDepCode',
      sorter: (a, b) => a.cDepCode.localeCompare(b.cDepCode),
    },
    {
      title: '存货编码',
      dataIndex: 'cInvCode',
      key: 'cInvCode',
      sorter: (a, b) => a.cInvCode.localeCompare(b.cInvCode),
    },
    {
      title: '存货名称',
      dataIndex: 'cItemName',
      key: 'cItemName',
      sorter: (a, b) => a.cItemName.localeCompare(b.cItemName),
    },
    {
      title: '订单数量',
      dataIndex: 'iQuantity',
      key: 'iQuantity',
      render: (quantity) => (quantity ?? 0).toFixed(2),
      sorter: (a, b) => (a.iQuantity ?? 0) - (b.iQuantity ?? 0),
    },    
    {
      title: '单位名称',
      dataIndex: 'unitName',
      key: 'unitName',
      sorter: (a, b) => a.unitName.localeCompare(b.unitName),
    },
    {
      title: '计划到货日期',
      dataIndex: 'dArriveDate',
      key: 'dArriveDate',
      render: (date) => date ? new Date(date).toLocaleString() : '',
      sorter: (a, b) => new Date(a.dArriveDate) - new Date(b.dArriveDate),
    },
    { title: '剩余未入库数量', dataIndex: 'remainingQuantity', key: 'remainingQuantity', render: (quantity) => (quantity ?? 0).toFixed(2), sorter: (a, b) => (a.remainingQuantity ?? 0) - (b.remainingQuantity ?? 0), },
    { title: '已入库数量', dataIndex: 'receivedQuantity', key: 'receivedQuantity', render: (quantity) => (quantity ?? 0).toFixed(2), sorter: (a, b) => (a.receivedQuantity ?? 0) - (b.receivedQuantity ?? 0), },
    { title: '条码值', dataIndex: 'barcode', key: 'barcode', sorter: (a, b) => a.barcode.localeCompare(b.barcode), },
    {
      title: '批号',
      dataIndex: 'batchNumber',
      key: 'batchNumber',
      sorter: (a, b) => a.batchNumber.localeCompare(b.batchNumber),
    },
    {
      title: '订单行号',
      dataIndex: 'irowno',
      key: 'irowno',
      sorter: (a, b) => (a.irowno ?? 0) - (b.irowno ?? 0),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <a onClick={() => navigate(`/supplierportal/purchase-orders/${record.cPOID}`)}>
          查看详情
        </a>
      ),
    },
  ];

  const handleSearch = (values) => {
    setSearchParams(values);
    setCurrentPage(1);
  };

  return (
    <div className="supplier-purchase-orders">
      <Title level={2}>供应商采购订单管理</Title>
      <Card>
        <Spin spinning={loading} tip="加载采购订单...">
          <Form form={form} onFinish={handleSearch} layout="inline" style={{ marginBottom: 16 }}>
            <Form.Item name="cPOID" label="订单编号">
              <Input placeholder="请输入订单编号" />
            </Form.Item>
            <Form.Item name="dPODate" label="订单日期">
              <DatePicker placeholder="选择订单日期" />
            </Form.Item>
            <Form.Item name="cInvCode" label="存货编码">
              <Input placeholder="请输入存货编码" />
            </Form.Item>
            <Form.Item name="cItemName" label="存货名称">
              <Input placeholder="请输入存货名称" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit">搜索</Button>
            </Form.Item>
          </Form>
          <Table
            columns={columns}
            dataSource={orders}
            pagination={{
              current: currentPage,
              pageSize: pageSize,
              total: total,
              onChange: (page, pageSize) => {
                setCurrentPage(page);
                setPageSize(pageSize);
              },
              showSizeChanger: true,
              showTotal: (total) => `共 ${total} 条记录`
            }}
            rowKey={record => `${record.cPOID}_${record.irowno}`}
            tableLayout="auto"
            scroll={{ x: 'max-content', y: 'calc(100vh - 320px)' }}
          />
        </Spin>
      </Card>
    </div>
  );
};

export default SupplierPurchaseOrders;